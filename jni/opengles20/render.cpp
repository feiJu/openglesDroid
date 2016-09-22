#include <jni.h>
#include <android/log.h>
#include <android_log.h>

#include <GLES2/gl2.h>
#include <GLES2/gl2ext.h>

#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include "render.h"
#ifndef LOG_TAG
#define LOG_TAG    "render.cpp"
#endif

static GLuint simpleProgram;

static void checkGlError(const char* op) {
	GLint error;
	for (error = glGetError(); error; error = glGetError()) {
		LOGE("error::after %s() glError (0x%x)\n", op, error);
	}
}

void gl_initialize(int width,int height){
	LOGD("------ gl_initialize(int width,int height) IS CALLED!");
	// Set the background color to black ( rgba ).
	glClearColor(0.5f, 0.0f, 0.0f, 0.5f); // OpenGL docs.
	//glClearColor(0.5f, 0.5f, 0.5f, 1);
	checkGlError("glClearColor");

	// Enable Smooth Shading, default not really needed.
//	glShadeModel(GL_SMOOTH);// OpenGL docs.
//	checkGlError("glShadeModel");

	// Depth buffer setup.
	glClearDepthf(1.0f);// OpenGL docs.
	checkGlError("glClearDepthf");

	// Enables depth testing.
	glEnable(GL_DEPTH_TEST);// OpenGL docs.
	checkGlError("glEnable");

	// The type of depth testing to do.
	glDepthFunc(GL_LEQUAL);// OpenGL docs.
	checkGlError("glDepthFunc");

	// Really nice perspective calculations.
//	glHint(GL_PERSPECTIVE_CORRECTION_HINT, // OpenGL docs.
//			GL_NICEST);
	checkGlError("glHint");
}

void gl_view_changed(int width,int height){
	LOGD("------ gl_view_changed(int width,int height) IS CALLED!");
//	// Sets the current view port to the new size.
//	glViewport(0, 0, width, height);// OpenGL docs.
//
//	// Select the projection matrix
//	glMatrixMode(GL_PROJECTION);// OpenGL docs.
//
//	// Reset the projection matrix
//	glLoadIdentity();// OpenGL docs.
//
//	// Calculate the aspect ratio of the window
//	/*GLU.*/gluPerspective(gl, 45.0f,
//	(float) width / (float) height,
//	0.1f, 100.0f);
//
//	// Select the modelview matrix
//	glMatrixMode(GL_MODELVIEW);// OpenGL docs.
//
//	// Reset the modelview matrix
//	glLoadIdentity();// OpenGL docs.
}

void gl_drawFrame(){
	glClear(GL_COLOR_BUFFER_BIT | // OpenGL docs.
					GL_DEPTH_BUFFER_BIT);
	checkGlError("glClear");
}

void gl_uninitialize(){

}
