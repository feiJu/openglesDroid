package com.fenghun.openglesdroid.jni.bean20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.GLES20;
import android.opengl.Matrix;

/**
 * 
 * 立方体
 * 
 * @author fenghun5987@gmail.com
 * @date 2016-10-15
 * @function
 */
public class Cube {

	 
	
	
	/** Store our model data in a float buffer. */
	private final FloatBuffer mCubePositions;	// 位置信息
	private final FloatBuffer mCubeColors;	// 颜色信息
	private final FloatBuffer mCubeNormals;	// 法线信息
	
	/** Allocate storage for the final combined matrix. This will be passed into the shader program. */
	private float[] mMVPMatrix = new float[16];
	
	
	/** How many bytes per float. */
	private final int mBytesPerFloat = 4; 
	
	/** Size of the position data in elements. */
	private final int mPositionDataSize = 3;	
	
	/** Size of the color data in elements. */
	private final int mColorDataSize = 4;	
	
	/** Size of the normal data in elements. */
	private final int mNormalDataSize = 3;
	
	public Cube() {
		// TODO Auto-generated constructor stub
		
		// Define points for a cube.		
		
				// X, Y, Z
				final float[] cubePositionData =
				{
						// In OpenGL counter-clockwise winding is default. This means that when we look at a triangle, 
						// if the points are counter-clockwise we are looking at the "front". If not we are looking at
						// the back. OpenGL has an optimization where all back-facing triangles are culled, since they
						// usually represent the backside of an object and aren't visible anyways.
						
						// Front face
						-1.0f, 1.0f, 1.0f,				
						-1.0f, -1.0f, 1.0f,
						1.0f, 1.0f, 1.0f, 
						-1.0f, -1.0f, 1.0f, 				
						1.0f, -1.0f, 1.0f,
						1.0f, 1.0f, 1.0f,
						
						// Right face
						1.0f, 1.0f, 1.0f,				
						1.0f, -1.0f, 1.0f,
						1.0f, 1.0f, -1.0f,
						1.0f, -1.0f, 1.0f,				
						1.0f, -1.0f, -1.0f,
						1.0f, 1.0f, -1.0f,
						
						// Back face
						1.0f, 1.0f, -1.0f,				
						1.0f, -1.0f, -1.0f,
						-1.0f, 1.0f, -1.0f,
						1.0f, -1.0f, -1.0f,				
						-1.0f, -1.0f, -1.0f,
						-1.0f, 1.0f, -1.0f,
						
						// Left face
						-1.0f, 1.0f, -1.0f,				
						-1.0f, -1.0f, -1.0f,
						-1.0f, 1.0f, 1.0f, 
						-1.0f, -1.0f, -1.0f,				
						-1.0f, -1.0f, 1.0f, 
						-1.0f, 1.0f, 1.0f, 
						
						// Top face
						-1.0f, 1.0f, -1.0f,				
						-1.0f, 1.0f, 1.0f, 
						1.0f, 1.0f, -1.0f, 
						-1.0f, 1.0f, 1.0f, 				
						1.0f, 1.0f, 1.0f, 
						1.0f, 1.0f, -1.0f,
						
						// Bottom face
						1.0f, -1.0f, -1.0f,				
						1.0f, -1.0f, 1.0f, 
						-1.0f, -1.0f, -1.0f,
						1.0f, -1.0f, 1.0f, 				
						-1.0f, -1.0f, 1.0f,
						-1.0f, -1.0f, -1.0f,
				};	
				
				// R, G, B, A
				final float[] cubeColorData =
				{				
						// Front face (red)
						1.0f, 0.0f, 0.0f, 1.0f,				
						1.0f, 0.0f, 0.0f, 1.0f,
						1.0f, 0.0f, 0.0f, 1.0f,
						1.0f, 0.0f, 0.0f, 1.0f,				
						1.0f, 0.0f, 0.0f, 1.0f,
						1.0f, 0.0f, 0.0f, 1.0f,
						
						// Right face (green)
						0.0f, 1.0f, 0.0f, 1.0f,				
						0.0f, 1.0f, 0.0f, 1.0f,
						0.0f, 1.0f, 0.0f, 1.0f,
						0.0f, 1.0f, 0.0f, 1.0f,				
						0.0f, 1.0f, 0.0f, 1.0f,
						0.0f, 1.0f, 0.0f, 1.0f,
						
						// Back face (blue)
						0.0f, 0.0f, 1.0f, 1.0f,				
						0.0f, 0.0f, 1.0f, 1.0f,
						0.0f, 0.0f, 1.0f, 1.0f,
						0.0f, 0.0f, 1.0f, 1.0f,				
						0.0f, 0.0f, 1.0f, 1.0f,
						0.0f, 0.0f, 1.0f, 1.0f,
						
						// Left face (yellow)
						1.0f, 1.0f, 0.0f, 1.0f,				
						1.0f, 1.0f, 0.0f, 1.0f,
						1.0f, 1.0f, 0.0f, 1.0f,
						1.0f, 1.0f, 0.0f, 1.0f,				
						1.0f, 1.0f, 0.0f, 1.0f,
						1.0f, 1.0f, 0.0f, 1.0f,
						
						// Top face (cyan)
						0.0f, 1.0f, 1.0f, 1.0f,				
						0.0f, 1.0f, 1.0f, 1.0f,
						0.0f, 1.0f, 1.0f, 1.0f,
						0.0f, 1.0f, 1.0f, 1.0f,				
						0.0f, 1.0f, 1.0f, 1.0f,
						0.0f, 1.0f, 1.0f, 1.0f,
						
						// Bottom face (magenta)
						1.0f, 0.0f, 1.0f, 1.0f,				
						1.0f, 0.0f, 1.0f, 1.0f,
						1.0f, 0.0f, 1.0f, 1.0f,
						1.0f, 0.0f, 1.0f, 1.0f,				
						1.0f, 0.0f, 1.0f, 1.0f,
						1.0f, 0.0f, 1.0f, 1.0f
				};
				
				// X, Y, Z
				// The normal is used in light calculations and is a vector which points
				// orthogonal to the plane of the surface. For a cube model, the normals
				// should be orthogonal to the points of each face.
				final float[] cubeNormalData =
				{												
						// Front face
						0.0f, 0.0f, 1.0f,				
						0.0f, 0.0f, 1.0f,
						0.0f, 0.0f, 1.0f,
						0.0f, 0.0f, 1.0f,				
						0.0f, 0.0f, 1.0f,
						0.0f, 0.0f, 1.0f,
						
						// Right face 
						1.0f, 0.0f, 0.0f,				
						1.0f, 0.0f, 0.0f,
						1.0f, 0.0f, 0.0f,
						1.0f, 0.0f, 0.0f,				
						1.0f, 0.0f, 0.0f,
						1.0f, 0.0f, 0.0f,
						
						// Back face 
						0.0f, 0.0f, -1.0f,				
						0.0f, 0.0f, -1.0f,
						0.0f, 0.0f, -1.0f,
						0.0f, 0.0f, -1.0f,				
						0.0f, 0.0f, -1.0f,
						0.0f, 0.0f, -1.0f,
						
						// Left face 
						-1.0f, 0.0f, 0.0f,				
						-1.0f, 0.0f, 0.0f,
						-1.0f, 0.0f, 0.0f,
						-1.0f, 0.0f, 0.0f,				
						-1.0f, 0.0f, 0.0f,
						-1.0f, 0.0f, 0.0f,
						
						// Top face 
						0.0f, 1.0f, 0.0f,			
						0.0f, 1.0f, 0.0f,
						0.0f, 1.0f, 0.0f,
						0.0f, 1.0f, 0.0f,				
						0.0f, 1.0f, 0.0f,
						0.0f, 1.0f, 0.0f,
						
						// Bottom face 
						0.0f, -1.0f, 0.0f,			
						0.0f, -1.0f, 0.0f,
						0.0f, -1.0f, 0.0f,
						0.0f, -1.0f, 0.0f,				
						0.0f, -1.0f, 0.0f,
						0.0f, -1.0f, 0.0f
				};
				
				// Initialize the buffers.
				mCubePositions = ByteBuffer.allocateDirect(cubePositionData.length * mBytesPerFloat)
		        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
				mCubePositions.put(cubePositionData).position(0);		
				
				mCubeColors = ByteBuffer.allocateDirect(cubeColorData.length * mBytesPerFloat)
		        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
				mCubeColors.put(cubeColorData).position(0);
				
				mCubeNormals = ByteBuffer.allocateDirect(cubeNormalData.length * mBytesPerFloat)
		        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
				mCubeNormals.put(cubeNormalData).position(0);
	}
		
	public String getVertexShader(){
		// TODO: Explain why we normalize the vectors, explain some of the vector math behind it all. Explain what is eye space.
		final String vertexShader =
			"uniform mat4 u_MVPMatrix;      \n"		// A constant representing the combined model/view/projection matrix.
		  + "uniform mat4 u_MVMatrix;       \n"		// A constant representing the combined model/view matrix.	
		  + "uniform vec3 u_LightPos;       \n"	    // The position of the light in eye space.
			
		  + "attribute vec4 a_Position;     \n"		// Per-vertex position information we will pass in.
		  + "attribute vec4 a_Color;        \n"		// Per-vertex color information we will pass in.
		  + "attribute vec3 a_Normal;       \n"		// Per-vertex normal information we will pass in.
		  
		  + "varying vec4 v_Color;          \n"		// This will be passed into the fragment shader.
		  
		  + "void main()                    \n" 	// The entry point for our vertex shader.
		  + "{                              \n"		
		// Transform the vertex into eye space.
		  + "   vec3 modelViewVertex = vec3(u_MVMatrix * a_Position);              \n"
		// Transform the normal's orientation into eye space.
		  + "   vec3 modelViewNormal = vec3(u_MVMatrix * vec4(a_Normal, 0.0));     \n"
		// Will be used for attenuation.
		  + "   float distance = length(u_LightPos - modelViewVertex);             \n"
		// Get a lighting direction vector from the light to the vertex.
		  + "   vec3 lightVector = normalize(u_LightPos - modelViewVertex);        \n"
		// Calculate the dot product of the light vector and vertex normal. If the normal and light vector are
		// pointing in the same direction then it will get max illumination.
		  + "   float diffuse = max(dot(modelViewNormal, lightVector), 0.1);       \n" 	  		  													  
		// Attenuate the light based on distance.
		  + "   diffuse = diffuse * (1.0 / (1.0 + (0.25 * distance * distance)));  \n"
		// Multiply the color by the illumination level. It will be interpolated across the triangle.
		  + "   v_Color = a_Color * diffuse;                                       \n" 	 
		// gl_Position is a special variable used to store the final position.
		// Multiply the vertex by the matrix to get the final point in normalized screen coordinates.		
		  + "   gl_Position = u_MVPMatrix * a_Position;                            \n"     
		  + "}                                                                     \n"; 
		
		return vertexShader;
	}
	
	public String getFragmentShader(){
		final String fragmentShader =
			"precision mediump float;       \n"		// Set the default precision to medium. We don't need as high of a 
													// precision in the fragment shader.				
		  + "varying vec4 v_Color;          \n"		// This is the color from the vertex shader interpolated across the 
		  											// triangle per fragment.			  
		  + "void main()                    \n"		// The entry point for our fragment shader.
		  + "{                              \n"
		  + "   gl_FragColor = v_Color;     \n"		// Pass the color directly through the pipeline.		  
		  + "}                              \n";
		
		return fragmentShader;
	}

	public void drawCube(int mPositionHandle, int mColorHandle,
			int mNormalHandle, float[] mViewMatrix, float[] mModelMatrix,
			int mMVMatrixHandle, float[] mProjectionMatrix,
			int mMVPMatrixHandle, int mLightPosHandle,
			float[] mLightPosInEyeSpace) {
		// TODO Auto-generated method stub
		
		// Pass in the position information
		mCubePositions.position(0);
		GLES20.glVertexAttribPointer(mPositionHandle, mPositionDataSize,
				GLES20.GL_FLOAT, false, 0, mCubePositions);	// 传入位置坐标信息

		GLES20.glEnableVertexAttribArray(mPositionHandle);

		// Pass in the color information
		mCubeColors.position(0);
		GLES20.glVertexAttribPointer(mColorHandle, mColorDataSize,
				GLES20.GL_FLOAT, false, 0, mCubeColors);	// 传入颜色信息

		GLES20.glEnableVertexAttribArray(mColorHandle);

		// Pass in the normal information
		mCubeNormals.position(0);
		GLES20.glVertexAttribPointer(mNormalHandle, mNormalDataSize,
				GLES20.GL_FLOAT, false, 0, mCubeNormals);	// 传入法线信息

		GLES20.glEnableVertexAttribArray(mNormalHandle);

		// This multiplies the view matrix by the model matrix, and stores the
		// result in the MVP matrix
		// (which currently contains model * view).获取模型和视图矩阵乘积，目前只有M和V信息
		Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);

		// Pass in the modelview matrix.
		GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mMVPMatrix, 0);// 传入M和V信息，保存在顶点着色器的u_MVMatrix常量中

		// This multiplies the modelview matrix by the projection matrix, and
		// stores the result in the MVP matrix
		// (which now contains model * view * projection).获取透视矩阵信息，目前含有M、V和P信息
		Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);	

		// Pass in the combined matrix.
		GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);// 传入M、V和P信息，保存在顶点着色器的u_MVPMatrix常量中

		// Pass in the light position in eye space.传入光源在视图空间的位置信息
		GLES20.glUniform3f(mLightPosHandle, mLightPosInEyeSpace[0],
				mLightPosInEyeSpace[1], mLightPosInEyeSpace[2]);

		// Draw the cube.
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 36);
	}

	public float[] getmMVPMatrix() {
		return mMVPMatrix;
	}
}
