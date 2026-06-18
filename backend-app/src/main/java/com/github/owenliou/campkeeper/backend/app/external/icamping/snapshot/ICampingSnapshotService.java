package com.github.owenliou.campkeeper.backend.app.external.icamping.snapshot;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.owenliou.campkeeper.backend.app.external.icamping.dto.ICampingStoreExternalLinkDto;
import com.github.owenliou.campkeeper.backend.app.external.icamping.dto.ICampingStoreListDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@Component
public class ICampingSnapshotService {

    private static final String STORES_FILE = "icamping-stores.json";
    private static final String LINKS_FILE = "icamping-links.json";

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Path snapshotDir;

    public ICampingSnapshotService(@Value("${icamping.snapshot-dir}") String snapshotDir) {
        this.snapshotDir = Paths.get(snapshotDir);
        try {
            Files.createDirectories(this.snapshotDir);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to create snapshot directory: " + snapshotDir, e);
        }
    }

    public void saveStores(List<ICampingStoreListDto> stores) {
        try {
            objectMapper.writeValue(storesFile(), stores);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to save stores snapshot", e);
        }
    }

    public List<ICampingStoreListDto> loadStores() {
        File file = storesFile();
        if (!file.exists()) {
            throw new IllegalStateException(
                "Stores snapshot not found at " + file.getAbsolutePath() + ". Run saveSnapshot() first.");
        }
        try {
            return objectMapper.readValue(file, new TypeReference<List<ICampingStoreListDto>>() {});
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to load stores snapshot", e);
        }
    }

    public void saveLinks(Map<String, List<ICampingStoreExternalLinkDto>> links) {
        try {
            objectMapper.writeValue(linksFile(), links);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to save links snapshot", e);
        }
    }

    public Map<String, List<ICampingStoreExternalLinkDto>> loadLinks() {
        File file = linksFile();
        if (!file.exists()) {
            throw new IllegalStateException(
                "Links snapshot not found at " + file.getAbsolutePath() + ". Run saveLinksSnapshot() first.");
        }
        try {
            return objectMapper.readValue(file, new TypeReference<Map<String, List<ICampingStoreExternalLinkDto>>>() {});
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to load links snapshot", e);
        }
    }

    private File storesFile() {
        return snapshotDir.resolve(STORES_FILE).toFile();
    }

    private File linksFile() {
        return snapshotDir.resolve(LINKS_FILE).toFile();
    }
}
