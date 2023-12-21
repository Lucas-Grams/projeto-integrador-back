package br.com.pnipapi.service.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AwsS3StorageService implements StorageService {
    private final S3Client s3;
    private final String bucketName;

    private static final Logger LOG = LoggerFactory.getLogger(AwsS3StorageService.class);

    public AwsS3StorageService(AwsCredentialsProvider awsCredentialsProvider, String bucketName) {
        this.s3 = S3Client.builder()
                          .region(Region.US_EAST_1)
                          .credentialsProvider(awsCredentialsProvider)
                          .build();

        this.bucketName = this.ensureBucket(bucketName);
    }

    /**
     * Construtor para testes com localstack, recebe a uri para o endpoint aws
     */
    public AwsS3StorageService(AwsCredentialsProvider awsCredentialsProvider, String bucketName, URI uri) {
        this.s3 = S3Client.builder()
                          .region(Region.US_EAST_1)
                          .endpointOverride(uri)
                          .credentialsProvider(awsCredentialsProvider)
                          .build();

        this.bucketName = this.ensureBucket(bucketName);
    }

    @Override
    public void save(Path p, byte[] bytes) {
        this.s3.putObject(r -> r.bucket(bucketName)
                          .key(p.toString()),
            RequestBody.fromBytes(bytes));
    }

    @Override
    public Optional<StorageObject> read(Path p) {
        try {
            return Optional.of(new StorageObject(p,
                this.s3.getObject(req -> req.bucket(bucketName)
                                            .key(p.toString()),
                    ResponseTransformer.toBytes())
                       .asByteArray()));
        } catch (NoSuchKeyException ignored) {
            return Optional.empty();
        }
    }

    @Override
    public List<StorageObject> readAll(Path p) {
        return this.listWithPrefix(p)
            .parallelStream()
            .map(obj -> Paths.get(obj.key()))
            .filter(objPath -> p.equals(objPath ) || p.equals(objPath.getParent()))
            .map(this::read)
            .map(so -> so.orElseThrow(() -> new IllegalStateException("Objeto deixou de existir após ser listado")))
            .collect(Collectors.toList());
    }

    @Override
    public boolean delete(Path p) {
        return this.deleteAll(p, false) == 1;
    }

    @Override
    public long deleteAllIn(Path p) {
        return this.deleteAll(p, true);
    }


    private long deleteAll(Path p, boolean recursive) {
        final List<S3Object> pathMatches = this.listWithPrefix(p);

        if (recursive) {
            final List<ObjectIdentifier> objs =
                pathMatches.stream()
                           .map(os -> ObjectIdentifier.builder()
                                                      .key(os.key())
                                                      .build())
                           .collect(Collectors.toList());

            if (!objs.isEmpty()) {
                this.s3.deleteObjects(req -> req.bucket(this.bucketName)
                                                .delete(d -> d.objects(objs)));
            }
            return pathMatches.size();
        } else if (pathMatches.size() == 0) {
            return 0;
        } else if (pathMatches.size() == 1) {
            this.s3.deleteObject(req -> req.bucket(this.bucketName)
                                           .key(p.toString()));
            return 1;
        } else {
            throw new IllegalArgumentException("Diretório ainda contém arquivos");
        }
    }

    private List<S3Object> listWithPrefix(Path p) {
        return this.s3.listObjects(req -> req.bucket(this.bucketName)
                                   .prefix(p.toString()))
                      .contents();
    }

    private String ensureBucket(String bucketName) {
        try {
            this.s3.headBucket(req -> req.bucket(bucketName));
        } catch (NoSuchBucketException ex) {
            this.s3.createBucket(req -> req.bucket(bucketName)
                                           .acl(BucketCannedACL.PRIVATE)
                                           .createBucketConfiguration(b ->
                                               b.locationConstraint(BucketLocationConstraint.fromValue("us-east-1"))));
            LOG.info("Bucket {}, não existia e foi criado", bucketName);
        } catch (S3Exception ex) {
            switch (ex.awsErrorDetails().sdkHttpResponse().statusCode()) {
                case 301:
                    throw new IllegalArgumentException("Bucket "
                        + bucketName
                        + " não está na mesma região do cliente, região do bucket: "
                        + ex.awsErrorDetails().sdkHttpResponse().headers().get("x-amz-bucket-region")
                        , ex);
                case 403:
                    throw new IllegalArgumentException("Acesso negado ao bucket "
                        +  bucketName, ex);
                default:
                    throw new RuntimeException("Erro ao verificar se bucket "
                        + bucketName + " existe", ex);
            }
        }
        return bucketName;
    }
}
