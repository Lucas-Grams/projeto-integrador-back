package br.com.pnipapi.service.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FsStorageService implements StorageService {
    private final Path basePath;

    private static final Logger LOG = LoggerFactory.getLogger(FsStorageService.class);

    public FsStorageService(Path basePath) {
        this.basePath = basePath;
    }

    @Override
    public void save(Path p, byte[] bytes) {
        try {
            Files.createDirectories(this.resolvePath(p).getParent());
            Files.write(this.resolvePath(p),
                bytes,
                StandardOpenOption.CREATE,
                StandardOpenOption.WRITE);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Optional<StorageObject> read(Path p) {
        try {
            return Optional.of(new StorageObject(p,
                Files.readAllBytes(this.resolvePath(p))));
        } catch (NoSuchFileException ignored) {
            return Optional.empty();
        } catch(IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<StorageObject> readAll(Path p) {
        try (final Stream<Path> paths = Files.list(this.resolvePath(p))) {
            return paths.map(fp ->
                this.read(this.basePath.relativize(fp))
                    .orElseThrow(() ->
                        new IllegalStateException("Objeto deixou de existir"
                            + " após ser listado")))
                        .collect(Collectors.toList());

        } catch (NoSuchFileException ignored) {
            return new ArrayList<>(0);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public boolean delete(Path p) {
        try {
            return Files.deleteIfExists(this.resolvePath(p));
        } catch (IOException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    @Override
    public long deleteAllIn(Path p) {
        try {
            return Files.walk(this.resolvePath(p))
                        .sorted(Comparator.reverseOrder())
                        .mapToInt(path -> {
                            try {
                                Files.delete(path);
                                return 1;
                            } catch (IOException ex) {
                                throw new IllegalArgumentException(ex);
                            }
                        }).sum();
        } catch (NoSuchFileException ignored) {
	     return 0;
        } catch (IOException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    private Path resolvePath(Path p) {
        if (p.isAbsolute()) {
            return this.basePath.resolve(p.subpath(0, p.getNameCount()));
        } else {
            return this.basePath.resolve(p);
        }
    }
}
