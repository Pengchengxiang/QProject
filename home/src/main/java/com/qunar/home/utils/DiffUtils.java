package com.qunar.home.utils;
/**
 * 二进制对比工具类，提供二进制对比生成查分包方法
 * Created by chengxiang.peng on 2016/11/10.
 */

public class DiffUtils {
    /**
     * native方法声明，将旧文件和新文件进行二进制对比，生成查分文件。这里我们用来生成新就版本apk的查分包
     *
     * @param oldFilePatch 对比旧版本文件路径，这里为旧版本apk
     * @param newFilePath  对比新版本文件路径，这里为新版本的apk
     * @param patchPath    对比生成差分文件路径，这里为新旧版本apk的二进制差分文件
     * @return 操作返回码，0-代表成功
     */
    public static native int diff(String oldFilePatch, String newFilePath, String patchPath);
}

