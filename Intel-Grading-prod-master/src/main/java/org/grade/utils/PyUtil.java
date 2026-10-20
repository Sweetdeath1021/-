package org.grade.utils;

import org.grade.common.ServiceException;

import java.io.*;

public class PyUtil {
    public static String QrScript(String pythonScriptPath, String imageUrl) {
        String line;
        String result = "";
        try {
            // 设置Python脚本的路径
            ProcessBuilder pb = new ProcessBuilder("python", pythonScriptPath, imageUrl);
            Process process = pb.start();

            // 读取脚本的输出
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                result = line;
            }

            // 等待脚本执行完成
            int exitCode = process.waitFor();
            System.out.println("Python脚本返回码："+ exitCode);
            // 若result为空
            if (exitCode == 2) {
                throw new ServiceException("划分脚本执行错误");
            }
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
        return result;
    }

    public static byte[] SegScript(String pythonScriptPath, String json, String imageUrl) {
        try {
            // 将JSON数据写入临时文件
            File tempJsonFile = File.createTempFile("temp-json", ".json");
            tempJsonFile.deleteOnExit();

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempJsonFile))) {
                writer.write(json);
            }

            // 创建并启动Python脚本进程
            ProcessBuilder pb = new ProcessBuilder("python", pythonScriptPath, "-json_file", tempJsonFile.getAbsolutePath(), "-image_url", imageUrl);
            Process process = pb.start();

            // 读取脚本的标准输出
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            try (InputStream inputStream = process.getInputStream()) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }

            // 读取标准错误输出
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String line;
            while ((line = errorReader.readLine()) != null) {
                System.err.println(line);
            }

            // 等待脚本执行完成
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new ServiceException("Python script exited with error code: " + exitCode);
            }

            // 返回脚本的输出作为字节数组
            return outputStream.toByteArray();

        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }
}
