package com.github.owenliou.campkeeper.base.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileUtils {

    public static File saveToFile(byte[] data, String localFolder, String fileName) {
        if (!createFolder(localFolder)) {
            return null;
        }

        String localFilePath = localFolder + File.separator + fileName;
        File file = new File(localFilePath);
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(data);
            return file;
        } catch (IOException e) {
            String message = "檔案下載失敗";
            log.error("{}, {}", e.getLocalizedMessage(), message);
            return null;
        }
    }

    public static boolean createFolder(String folder) {
        Path path = Path.of(folder);
        if (!Files.exists(path)) {
            return path.toFile().mkdirs();
        }
        return true;
    }

    /**
     * 從 ClassPath 取得檔案，如：/templates/thymeleaf/test.html
     * @param fileName
     * @return
     * @throws IOException
     */
    public static String resourceToString(String fileName) throws IOException {
        try {
            return IOUtils.resourceToString(fileName, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IOException(fileName, e);
        }
    }

    public static InputStream resourceToInputStream(String fileName) throws IOException {
        try {
            return IOUtils.resourceToURL(fileName).openStream();
        } catch (IOException e) {
            throw new IOException(fileName, e);
        }
    }

    public static File resourceToFile(String fileName) throws IOException {
        try {
            return Path.of(IOUtils.resourceToURL(fileName).toURI()).toFile();
        } catch (IOException e) {
            throw new IOException(fileName, e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

}
