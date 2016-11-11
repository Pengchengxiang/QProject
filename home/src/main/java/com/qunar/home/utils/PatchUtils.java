package com.qunar.home.utils;

/**
 * 二进制合并工具类，提供二进制合并方法
 * Created by chengxiang.peng on 2016/11/10.
 */
public class PatchUtils {
    /**
     * native方法声明，将旧文件和差分文件进行合并，生成新文件。这里我们用来生成合并生成新版本的apk
     *
     * @param oldFilePatch 合并旧版本文件路径，这里为旧版本apk
     * @param newFilePath  合并生成的新版本文件路径，这里为合成生成的新版本的apk
     * @param patchPath    合并差分文件路径，这里为新老版本apk的二进制差分文件
     * @return 操作返回码，0-代表成功
     */
    public static native int patch(String oldFilePatch, String newFilePath, String patchPath);
}

