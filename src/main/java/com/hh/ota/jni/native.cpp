#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include "jni.h"
#include "native.h"
#include "hipatch.h"

static const char *classPathName = "com/github/snowdream/hdiffpatch/HDiffPatch";

static JNINativeMethod methods[] = {
        {(char*)"hdiff", (char*)"(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)I", (void *) hdiff},
        {(char*)"hpatch", (char*)"(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)I", (void *) hpatch}
};

JNIEXPORT jint JNICALL hdiff(JNIEnv *env, jobject thiz, jstring oldFilePath, jstring newFilePath,
                             jstring diffFilePath){
    int argc = 4;
    char * argv[argc];
    argv[0] = (char*) "hdiff";
    argv[1] = (char*) (env->GetStringUTFChars( oldFilePath, 0));
    argv[2] = (char*) (env->GetStringUTFChars( newFilePath, 0));
    argv[3] = (char*) (env->GetStringUTFChars( diffFilePath, 0));

    printf("old apk = %s \n", argv[1]);
    printf("new apk = %s \n", argv[2]);
    printf("patch = %s \n", argv[3]);

    int ret = hh_ota_diff(argv[1], argv[2], argv[3]);
    printf("genDiff result = %d ", ret);

    env->ReleaseStringUTFChars( oldFilePath, argv[1]);
    env->ReleaseStringUTFChars( newFilePath, argv[2]);
    env->ReleaseStringUTFChars( diffFilePath, argv[3]);
//      env->DeleteLocalRef(argv[1]);
//      env->DeleteLocalRef(argv[2]);
//      env->DeleteLocalRef(argv[3]);
    return ret;
}


JNIEXPORT jint JNICALL hpatch(JNIEnv *env, jobject thiz, jstring oldFilePath,
                              jstring diffFilePath, jstring newFilePath){
    int argc = 4;
    char * argv[argc];
    argv[0] = (char*) "hpatch";
    argv[1] = (char*) (env->GetStringUTFChars(oldFilePath, 0));
    argv[2] = (char*) (env->GetStringUTFChars(diffFilePath, 0));
    argv[3] = (char*) (env->GetStringUTFChars(newFilePath, 0));

    printf("old apk = %s \n", argv[1]);
    printf("patch = %s \n", argv[2]);
    printf("new apk = %s \n", argv[3]);

    int ret = hh_ota_patch(argv[2], argv[1], argv[3]);

    printf("patch result = %d ", ret);

    env->ReleaseStringUTFChars(oldFilePath, argv[1]);
    env->ReleaseStringUTFChars(newFilePath, argv[2]);
    env->ReleaseStringUTFChars(diffFilePath, argv[3]);
//      (*env)->DeleteLocalRef(env,argv[1]);
//      (*env)->DeleteLocalRef(env,argv[2]);
//      (*env)->DeleteLocalRef(env,argv[3]);
    return ret;
}


/*
 * Register several native methods for one class.
 */
static int registerNativeMethods(JNIEnv* env, const char* className,
                                 JNINativeMethod* gMethods, int numMethods)
{
    jclass clazz;
    clazz = env->FindClass(className);
    if (clazz == NULL) {
        return JNI_FALSE;
    }
    if (env->RegisterNatives(clazz, gMethods, numMethods) < 0) {
        return JNI_FALSE;
    }
    return JNI_TRUE;
}
/*
 * Register native methods for all classes we know about.
 *
 * returns JNI_TRUE on success.
 */
static int registerNatives(JNIEnv* env)
{
    if (!registerNativeMethods(env, classPathName,
                               methods, sizeof(methods) / sizeof(methods[0]))) {
        return JNI_FALSE;
    }
    return JNI_TRUE;
}
// ----------------------------------------------------------------------------
/*
 * This is called by the VM when the shared library is first loaded.
 */

typedef union {
    JNIEnv* env;
    void* venv;
} UnionJNIEnvToVoid;

jint JNI_OnLoad(JavaVM* vm, void* reserved)
{
    UnionJNIEnvToVoid uenv;
    uenv.venv = NULL;
    jint result = -1;
    JNIEnv* env = NULL;

    if (vm->GetEnv(&uenv.venv, JNI_VERSION_1_4) != JNI_OK) {
        goto bail;
    }
    env = uenv.env;
    if (registerNatives(env) != JNI_TRUE) {
        goto bail;
    }

    result = JNI_VERSION_1_4;

    bail:
    return result;
}
