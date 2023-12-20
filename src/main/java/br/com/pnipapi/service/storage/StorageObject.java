package br.com.pnipapi.service.storage;

import java.nio.file.Path;
import java.util.Base64;

public class StorageObject {
    private final Path   path;
    private final byte[] bytes;

    StorageObject(final Path path, final byte[] bytes) {
        this.path = path;
        this.bytes = bytes;
    }

    public Path getPath() {
        return path;
    }

    public byte[] toByteArray() {
        return bytes;
    }

    public String toBase64String() {
        return Base64.getEncoder().encodeToString(this.bytes);
    }
}
