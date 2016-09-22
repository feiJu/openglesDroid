# Copyright (C) 2009 The Android Open Source Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

LOCAL_PATH := $(call my-dir)  

### ffmpeg 视频播放工具类 ###
#
#
# opengl 在 c代码中实现 里面用到了，两个库，
# lGLESv1_CM opengl 1.0 和 -lGLESv2 opengl 2.0
#
#
include $(CLEAR_VARS)  
LOCAL_MODULE := myOpenglES  
LOCAL_SRC_FILES := com_fenghun_openglesdroid_jni_MyOpenglES.cpp #\ opengles20/render.cpp \ ffmpeg/decodeUtils.c
LOCAL_SRC_FILES += opengles20/render.cpp
LOCAL_C_INCLUDES += $(LOCAL_PATH)/include  
LOCAL_LDLIBS := -llog -lz -lm -L$(SYSROOT)/usr/lib -lGLESv1_CM -ldl -lGLESv2  
LOCAL_LDLIBS += -lOpenSLES
include $(BUILD_SHARED_LIBRARY) 
