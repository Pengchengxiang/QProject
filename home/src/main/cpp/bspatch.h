//
// Created by chengxiang.peng on 2016/11/11.
//
#include <jni.h>

#ifndef BS_PATCH_H_
#define BS_PATCH_H_

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT jint JNICALL Java_com_qunar_home_utils_PatchUtils_patch(JNIEnv *, jclass, jstring, jstring, jstring);

#ifdef __cplusplus
}
#endif

#endif