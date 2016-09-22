#include "com_fenghun_openglesdroid_jni_MyOpenglES.h"

#include <android/log.h>
#include "android_log.h"
#ifndef LOG_TAG
#define LOG_TAG    "MyOpenglES.cpp"
#endif
/**
 * 测试函数
 *
 * Class:     com_fenghun_openglesdroid_jni_MyOpenglES
 * Method:    test
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_fenghun_openglesdroid_jni_MyOpenglES_test
  (JNIEnv *env, jclass thiz){
	LOGD("----------- test() is called! --------------");
}
