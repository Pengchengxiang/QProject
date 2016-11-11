//
// Created by chengxiang.peng on 2016/11/11.
//
#include <jni.h>
#ifndef BS_DIFF_H_
#define BS_DIFF_H_

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT jint JNICALL Java_com_qunar_home_utils_DiffUtils_diff(JNIEnv *, jclass, jstring, jstring, jstring);

#ifdef __cplusplus
}
#endif

#endif