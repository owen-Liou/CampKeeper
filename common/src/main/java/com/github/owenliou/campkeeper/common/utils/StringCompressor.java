package com.github.owenliou.campkeeper.common.utils;

import com.github.luben.zstd.Zstd;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

@Slf4j
public class StringCompressor {

    /**
     * @param bytes
     * @return base64 encoded string
     * @throws IOException
     */
    public static String compress(byte[] bytes) throws IOException {
        if (bytes == null || bytes.length == 0) {
            return null;
        }

        int len = bytes.length;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             GZIPOutputStream gzip = new GZIPOutputStream(baos)) {
            gzip.write(bytes);
            gzip.finish();
            String result = Base64.getEncoder().encodeToString(baos.toByteArray());
            LogUtils.devLogInfo("壓縮前 -> 後長度: {} -> {}", len, result.length());
            return result;
        }
    }

    /**
     * @param str
     * @return base64 encoded string
     * @throws IOException
     */
    public static String compress(String str) throws IOException {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        return compress(str.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * @param compressedStr base64 encoded string
     * @return byte array
     */
    public static byte[] decompress(String compressedStr) {
        if (compressedStr == null || compressedStr.isEmpty()) {
            return null;
        }

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(Base64.getDecoder().decode(compressedStr)))) {

            byte[] buffer = new byte[1024];
            int len;
            while ((len = gis.read(buffer)) > 0) {
                baos.write(buffer, 0, len);
            }
            return baos.toByteArray();
        } catch (IOException e) {
            log.error("Failed to decompress string: {}, {}", e.getLocalizedMessage(), compressedStr);
            return null;
        }
    }

    /**
     * @param compressedStr base64 encoded string
     * @return String
     */
    public static String decompressToString(String compressedStr) {
        byte[] bytes = decompress(compressedStr);
        if (bytes == null) {
            return null;
        }
        return new String(bytes, StandardCharsets.UTF_8);
    }

    /**
     * @param compressedStr base64 encoded string
     * @return base64 encoded string，視需要解壓縮後再自行 Base64 decode 成需要的格式
     */
    public static String decompressToBase64(String compressedStr) {
        byte[] bytes = decompress(compressedStr);
        if (bytes == null) {
            return null;
        }
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * 使用 Zstd 壓縮，回傳 base64 字串
     */
    public static String compressZstd(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        int len = bytes.length;
        byte[] compressed = Zstd.compress(bytes);
        String result = Base64.getEncoder().encodeToString(compressed);
        LogUtils.devLogInfo("壓縮前 -> 後長度: {} -> {}", len, result.length());
        return result;
    }

    /**
     * 使用 Zstd 壓縮，回傳 base64 字串
     */
    public static String compressZstd(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        return compressZstd(str.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Zstd 解壓縮 base64 字串
     * @return String
     */
    public static byte[] decompressZstd(String compressedStr) {
        if (compressedStr == null || compressedStr.isEmpty()) {
            return null;
        }
        byte[] compressed = Base64.getDecoder().decode(compressedStr);
        long decompressedSize = Zstd.decompressedSize(compressed);
        return Zstd.decompress(compressed, (int) decompressedSize);
    }

    /**
     * Zstd 解壓縮 base64 字串
     * @return String
     */
    public static String decompressZstdToString(String compressedStr) {
        byte[] decompressed = decompressZstd(compressedStr);
        return new String(decompressed, StandardCharsets.UTF_8);
    }

}
