
#ifndef DIFFPATCH_NATIVE_H
#define DIFFPATCH_NATIVE_H
#include <jni.h>

#define SUCCESS 0
#define ERROR 1

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT jint JNICALL hdiff(JNIEnv *env, jobject thiz, jstring oldFilePath, jstring newFilePath,
                                jstring diffFilePath);

JNIEXPORT jint JNICALL hpatch(JNIEnv *env, jobject thiz, jstring oldFilePath, jstring diffFilePath,
		                jstring newFilePath);


#ifdef __cplusplus
}
#endif
#endif //ANDROID_DIFFPATCH_NATIVE_DIFF_H
