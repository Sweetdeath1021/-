package org.grade.utils;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * 支持解压gz、zip、7z、tar、bz2等多格式压缩包
 */
@Component
public class CompressUtil {

    // 将文件拓展名导向对应函数
    public Map<String, InputStream> decompress(Path tempFile, String fileName) {
        // 查找后缀名
        String extension = fileName
                .substring(fileName.lastIndexOf('.') + 1);
        switch (extension) {
            case "zip":
                return decompressZip(tempFile);
            case "gz":
                return decompressGzip(tempFile);
            case "7z":
                return decompress7z(tempFile);
            case "tar":
                return decompressTar(tempFile);
            case "bz2":
                return decompressBzip2(tempFile);
            default:
                return decompressXz(tempFile);
        }
    }

    // gz格式解压
    private Map<String, InputStream> decompressGzip(Path inputFile) {
        try (InputStream is = Files.newInputStream(inputFile.toFile().toPath());
             CompressorInputStream cis = new CompressorStreamFactory().createCompressorInputStream(CompressorStreamFactory.GZIP, is);
             ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, cis)) {
            return extractFiles(ais);
        } catch (ArchiveException | IOException | CompressorException e) {
            throw new RuntimeException(e);
        }
    }

    // zip格式解压
    private Map<String, InputStream> decompressZip(Path inputFile) {
        try (InputStream is = Files.newInputStream(inputFile.toFile().toPath());
             ZipArchiveInputStream zis = new ZipArchiveInputStream(is)) {
            return extractFiles(zis);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 7z格式解压
    private Map<String, InputStream> decompress7z(Path inputFile) {
        try {
            SevenZFile sevenZFile = new SevenZFile(inputFile.toFile());
            return extractFiles(sevenZFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // tar格式解压
    private Map<String, InputStream> decompressTar(Path inputFile) {
        try (InputStream is = Files.newInputStream(inputFile.toFile().toPath());
             ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, is)) {
            return extractFiles(ais);
        } catch (ArchiveException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    //bz2格式解压
    private Map<String, InputStream> decompressBzip2(Path inputFile) {
        try (InputStream is = Files.newInputStream(inputFile.toFile().toPath());
             CompressorInputStream cis = new CompressorStreamFactory().createCompressorInputStream(CompressorStreamFactory.BZIP2, is);
             ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, cis)) {
            return extractFiles(ais);
        } catch (ArchiveException | IOException | CompressorException e) {
            throw new RuntimeException(e);
        }
    }
    //xz格式解压
    private Map<String, InputStream> decompressXz(Path inputFile) {
        try (InputStream is = Files.newInputStream(inputFile.toFile().toPath());
             CompressorInputStream cis = new CompressorStreamFactory().createCompressorInputStream(CompressorStreamFactory.XZ, is);
             ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, cis)) {
            return extractFiles(ais);
        } catch (ArchiveException | IOException | CompressorException e) {
            throw new RuntimeException(e);
        }

    }

    // 解压后操作
    private Map<String, InputStream> extractFiles(ArchiveInputStream ais) {
        Map<String, InputStream> fileStreams;
        try {
            fileStreams = new HashMap<>();
            ArchiveEntry entry;
            while ((entry = ais.getNextEntry()) != null) {
                // 如果解压后是文件夹，跳过
                if (entry.isDirectory()) {
                    continue;
                }

                String entryName = entry.getName();
                // 删除文件名中的 '/' 以及其之前的部分
                int lastSlashIndex = entryName.lastIndexOf('/');
                if (lastSlashIndex >= 0) {
                    entryName = entryName.substring(lastSlashIndex + 1);
                }

                // 将文件内容读取到 ByteArrayOutputStream 中
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[(int) entry.getSize()];

                // 从 ais 中读取文件内容到 buffer
                int bytesRead;
                while ((bytesRead = ais.read(buffer)) != -1) {
                    baos.write(buffer, 0, bytesRead);
                }

                // 将 ByteArrayOutputStream 转换为 InputStream，并放入 Map 中
                InputStream fileInputStream = new ByteArrayInputStream(baos.toByteArray());
                fileStreams.put(entryName, fileInputStream);

                baos.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return fileStreams;
    }


    // 重载方法，适配7z解压
    private Map<String, InputStream> extractFiles(SevenZFile sevenZFile) {
        Map<String, InputStream> fileStreams;
        try {
            fileStreams = new HashMap<>();

            SevenZArchiveEntry entry;
            while ((entry = sevenZFile.getNextEntry()) != null) {
                // 如果解压后是文件夹，跳过
                if (entry.isDirectory()) {
                    continue;
                }
                String entryName = entry.getName();
                // 删除文件名中的 '/' 以及其之前的部分
                int lastSlashIndex = entryName.lastIndexOf('/');
                if (lastSlashIndex >= 0) {
                    entryName = entryName.substring(lastSlashIndex + 1);
                }

                // 将文件内容读取到 ByteArrayOutputStream 中
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[(int) entry.getSize()];

                // 从 ais 中读取文件内容到 buffer
                int bytesRead;
                while ((bytesRead = sevenZFile.read(buffer)) != -1) {
                    baos.write(buffer, 0, bytesRead);
                }

                // 将 ByteArrayOutputStream 转换为 InputStream，并放入 Map 中
                InputStream fileInputStream = new ByteArrayInputStream(baos.toByteArray());
                fileStreams.put(entryName, fileInputStream);

                baos.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return fileStreams;
    }
}
