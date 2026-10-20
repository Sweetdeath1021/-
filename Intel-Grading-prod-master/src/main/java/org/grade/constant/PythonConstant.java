package org.grade.constant;

import java.nio.file.Paths;

public class PythonConstant {
    // 可通过 -Dpython.script.dir=... 或环境变量 PYTHON_SCRIPT_DIR 指定脚本目录，默认在后端项目根目录下运行
    public static final String SCRIPT_DIR = System.getProperty("python.script.dir",
            System.getenv().getOrDefault("PYTHON_SCRIPT_DIR", "src/main/resources/script"));
    public static final String QR_SCRIPT_PATH = Paths.get(SCRIPT_DIR, "ReadQr.py").toAbsolutePath().toString();
    public static final String SEG_SCRIPT_PATH = Paths.get(SCRIPT_DIR, "ImageSeg.py").toAbsolutePath().toString();

}
