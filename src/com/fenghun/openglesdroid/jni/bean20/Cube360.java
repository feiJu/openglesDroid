package com.fenghun.openglesdroid.jni.bean20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import com.fenghun.openglesdroid.R;
import com.fenghun.openglesdroid.jni.utils.GLES20Utils;

import android.content.Context;
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
public class Cube360 {

	private Context context;
	
	
	/** Store our model data in a float buffer. */
	private final FloatBuffer mCubePositions;	// 位置信息
	private final FloatBuffer mCubeColors;	// 颜色信息
	private final FloatBuffer mCubeNormals;	// 法线信息
	private final FloatBuffer mCubeTextureCoordinates;	// 贴图信息
	private final FloatBuffer mCubeTextureCoordinatesForPlane; // 地面，
	
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
	
	/** Size of the texture coordinate data in elements. */
	private final int mTextureCoordinateDataSize = 2;
	
	public Cube360(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		// Define points for a cube.		

		// X, Y, Z
		final float[] cubePositionData = {
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
						1.0f, 1.0f, -1.0f,		// 2			
						1.0f, -1.0f, -1.0f,		// 1
						-1.0f, 1.0f, -1.0f,		// 3
						1.0f, -1.0f, -1.0f,		// 1			
						-1.0f, -1.0f, -1.0f,	// 0
						-1.0f, 1.0f, -1.0f,		// 3
						
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

				// S, T (or X, Y)
				// Texture coordinate data.
				// Because images have a Y axis pointing downward (values increase as you move down the image) while
				// OpenGL has a Y axis pointing upward, we adjust for that here by flipping the Y axis.
				// What's more is that the texture coordinates are the same for every face.
				final float[] cubeTextureCoordinateData =
				{												
						// Front face
						0.0f, 0.0f, 				
						0.0f, 1.0f,
						//1.0f, 0.0f,
						0.25f,0.0f,
						0.0f, 1.0f,
						//1.0f, 1.0f,
						0.25f,1.0f,
						//1.0f, 0.0f,				
						0.25f,0.0f,
						
						// Right face 
						//0.0f, 0.0f, 				
						0.25f, 0.0f,
						//0.0f, 1.0f,
						0.25f, 1.0f,
						//1.0f, 0.0f,
						0.5f, 0.0f,
						//0.0f, 1.0f,
						0.25f, 1.0f,
						//1.0f, 1.0f,
						0.5f, 1.0f,
						//1.0f, 0.0f,	
						0.5f, 0.0f,
						
						// Back face 
						//0.0f, 0.0f, 				
						0.5f, 0.0f,
						//0.0f, 1.0f,
						0.5f, 1.0f,
						//1.0f, 0.0f,
						0.75f, 0.0f,
						//0.0f, 1.0f,
						0.5f, 1.0f,
						//1.0f, 1.0f,
						0.75f, 1.0f,
						//1.0f, 0.0f,	
						0.75f, 0.0f,
						
						// Left face 
						//0.0f, 0.0f, 				
						0.75f, 0.0f,
						//0.0f, 1.0f,
						0.75f, 1.0f,
						1.0f, 0.0f,
						//0.0f, 1.0f,
						0.75f, 1.0f,
						1.0f, 1.0f,
						1.0f, 0.0f,	
						
						// Top face 
						0.0f, 0.0f, 				
						0.0f, 1.0f,
						1.0f, 0.0f,
						0.0f, 1.0f,
						1.0f, 1.0f,
						1.0f, 0.0f,	
						
						// Bottom face 
						0.0f, 0.0f, 				
						0.0f, 1.0f,
						1.0f, 0.0f,
						0.0f, 1.0f,
						1.0f, 1.0f,
						1.0f, 0.0f
		};
				
		// S, T (or X, Y)
		// Texture coordinate data.
		// Because images have a Y axis pointing downward (values increase as you move down the image) while
		// OpenGL has a Y axis pointing upward, we adjust for that here by flipping the Y axis.
		// What's more is that the texture coordinates are the same for every face.
		final float[] cubeTextureCoordinateDataForPlane = {												
						// Front face
						0.0f, 0.0f, 				
						0.0f, 25.0f,
						25.0f, 0.0f,
						0.0f, 25.0f,
						25.0f, 25.0f,
						25.0f, 0.0f,				
						
						// Right face 
						0.0f, 0.0f, 				
						0.0f, 25.0f,
						25.0f, 0.0f,
						0.0f, 25.0f,
						25.0f, 25.0f,
						25.0f, 0.0f,	
						
						// Back face 
						0.0f, 0.0f, 				
						0.0f, 25.0f,
						25.0f, 0.0f,
						0.0f, 25.0f,
						25.0f, 25.0f,
						25.0f, 0.0f,	
						
						// Left face 
						0.0f, 0.0f, 				
						0.0f, 25.0f,
						25.0f, 0.0f,
						0.0f, 25.0f,
						25.0f, 25.0f,
						25.0f, 0.0f,	
						
						// Top face 
						0.0f, 0.0f, 				
						0.0f, 25.0f,
						25.0f, 0.0f,
						0.0f, 25.0f,
						25.0f, 25.0f,
						25.0f, 0.0f,	
						
						// Bottom face 
						0.0f, 0.0f, 				
						0.0f, 25.0f,
						25.0f, 0.0f,
						0.0f, 25.0f,
						25.0f, 25.0f,
						25.0f, 0.0f
				};
				
				
				
		// Initialize the buffers.
		mCubePositions = 
		
		// Allocate a direct block of memory on the native heap,
		// size in bytes is equal to cubePositions.length * BYTES_PER_FLOAT.
		// BYTES_PER_FLOAT is equal to 4, since a float is 32-bits, or 4 bytes.
		ByteBuffer.allocateDirect(cubePositionData.length * mBytesPerFloat)
		// Floats can be in big-endian or little-endian order.
		// We want the same as the native platform.
		.order(ByteOrder.nativeOrder())
		// Give us a floating-point view on this byte buffer.
		.asFloatBuffer();
		// Copy data from the Java heap to the native heap.
		mCubePositions.put(cubePositionData)
		// Reset the buffer position to the beginning of the buffer.
		.position(0);

		
		
		mCubeColors = ByteBuffer
				.allocateDirect(cubeColorData.length * mBytesPerFloat)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();
		mCubeColors.put(cubeColorData).position(0);

		mCubeNormals = ByteBuffer
				.allocateDirect(cubeNormalData.length * mBytesPerFloat)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();
		mCubeNormals.put(cubeNormalData).position(0);
		
		mCubeTextureCoordinates = ByteBuffer.allocateDirect(cubeTextureCoordinateData.length * mBytesPerFloat)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();
		mCubeTextureCoordinates.put(cubeTextureCoordinateData).position(0);

		mCubeTextureCoordinatesForPlane = ByteBuffer
				.allocateDirect(
						cubeTextureCoordinateDataForPlane.length
								* mBytesPerFloat)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();
		mCubeTextureCoordinatesForPlane.put(cubeTextureCoordinateDataForPlane)
				.position(0);
	}
	
	/**
	 * 光照效果在顶点着色器中完成，作用于每个顶点
	 * @return
	 */
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
		// Transform the vertex into eye space.modelView顶点
		  + "   vec3 modelViewVertex = vec3(u_MVMatrix * a_Position);              \n"
		// Transform the normal's orientation into eye space.法线
		  + "   vec3 modelViewNormal = vec3(u_MVMatrix * vec4(a_Normal, 0.0));     \n"
		// Will be used for attenuation.光源到顶点距离，用于计算能量衰减
		  + "   float distance = length(u_LightPos - modelViewVertex);             \n"
		// Get a lighting direction vector from the light to the vertex.光线矢量
		  + "   vec3 lightVector = normalize(u_LightPos - modelViewVertex);        \n"
		// Calculate the dot product of the light vector and vertex normal. 
		// (计算光矢量和顶点法线的点积)
		//If the normal and light vector are
		// pointing in the same direction then it will get max illumination.
		// 获取最大的光能量值，如果法线和光矢量方向相同，则能获取最大的光照强度
		  + "   float diffuse = max(dot(modelViewNormal, lightVector), 0.1);       \n" 	  		  													  
		// Attenuate the light based on distance.
		// 计算漫发射的值，光强随着距离的增大而衰减
		  + "   diffuse = diffuse * (1.0 / (1.0 + (0.25 * distance * distance)));  \n"
		// Multiply the color by the illumination level. It will be interpolated across the triangle.
		// 计算漫反射产生的光照对颜色的影响，不同的光照强度对物体的颜色产生不同的影响，取决于光强和物体本身的反光属性
		// 计算结果输出给片段着色器
		  + "   v_Color = a_Color * diffuse;                                       \n" 	 
		// gl_Position is a special variable used to store the final position.
		// Multiply the vertex by the matrix to get the final point in normalized screen coordinates.		
		  + "   gl_Position = u_MVPMatrix * a_Position;                            \n"     
		  + "}                                                                     \n"; 
		
		return vertexShader;
	}
	
	/**
	 * 
	 * @return
	 */
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
	
	/**
	 * 光照效果在片段着色器中完成，作用于每个片段
	 * @return
	 */
	public String getVertexShader_lightPerFragment(){
		
		final String vertexShader =
		"uniform mat4 u_MVPMatrix;		\n"      // A constant representing the combined model/view/projection matrix.
		+ "uniform mat4 u_MVMatrix;		\n"       // A constant representing the combined model/view matrix.
		 
		+"attribute vec4 a_Position;	\n"     // Per-vertex position information we will pass in.
		+"attribute vec4 a_Color;  		\n"      // Per-vertex color information we will pass in.
		+"attribute vec3 a_Normal;  	\n"     // Per-vertex normal information we will pass in.
		 
		+"varying vec3 v_Position; 		\n"      // This will be passed into the fragment shader.
		+"varying vec4 v_Color;     	\n"     // This will be passed into the fragment shader.
		+"varying vec3 v_Normal;    	\n"     // This will be passed into the fragment shader.
		
		+"attribute vec2 a_TexCoordinate;	\n"	 // Per-vertex texture coordinate information we will pass in. 
		+"varying vec2 v_TexCoordinate;   	\n"	// This will be passed into the fragment shader.
		
		+"void main(){ \n"	// The entry point for our vertex shader.
		+"    v_Position = vec3(u_MVMatrix * a_Position); 			\n" // Transform the vertex into eye space.
		+"    v_Color = a_Color; 	\n" // Pass through the color.
		+"    v_Normal = vec3(u_MVMatrix * vec4(a_Normal, 0.0)); 	\n"// Transform the normal's orientation into eye space.
		    // gl_Position is a special variable used to store the final position.
		    // Multiply the vertex by the matrix to get the final point in normalized screen coordinates.
		+"    gl_Position = u_MVPMatrix * a_Position; 				\n"
		+"	  v_TexCoordinate = a_TexCoordinate;					\n" // Pass through the texture coordinate.
		+"}";
		return vertexShader;
	}
	
	/**
	 * 参考链接：http://www.learnopengles.com/android-lesson-three-moving-to-per-fragment-lighting/
	 * 
	 * 光照效果在片段着色器中完成，作用于每个片段
	 * @return
	 */
	public String getFragmentShader_lightPerFragment(){
		final String fragmentShader =
		"precision mediump float;     	\n"   // Set the default precision to medium. We don't need as high of a
		        // precision in the fragment shader.
		+"uniform vec3 u_LightPos;    	\n"       // The position of the light in eye space.
		+"uniform sampler2D u_Texture;  \n"  // The input texture.
		
		
		+"varying vec3 v_Position;    	\n"     // Interpolated position for this fragment.
		+"varying vec4 v_Color;       	\n"       // This is the color from the vertex shader interpolated across the
		        // triangle per fragment.
		+"varying vec3 v_Normal;      	\n"   // Interpolated normal for this fragment.
		+"varying vec2 v_TexCoordinate; \n"  // Interpolated texture coordinate per fragment.
		
		//The entry point for our fragment shader.
		+"void main() {   				\n"
			// Will be used for attenuation.
		+"	float distance = length(u_LightPos - v_Position);    				\n"
			
			// Get a lighting direction vector from the light to the vertex.
		+"	vec3 lightVector = normalize(u_LightPos - v_Position);    			\n"
			
		
		
//		+"float diffuse;														\n"
//
//		+"if (gl_FrontFacing) {													\n"
//		+"	diffuse = max(dot(v_Normal, lightVector), 0.0);						\n"
//		+"} else {																\n"
//		+"	diffuse = max(dot(-v_Normal, lightVector), 0.0);\n"
//		+"}     																\n"
		
			// Calculate the dot product of the light vector and vertex normal. If the normal and light vector are
			// pointing in the same direction then it will get max illumination.
		+"	float diffuse = max(dot(v_Normal, lightVector), 0.1);    			\n"
			
//			// Add attenuation.
//		+"	diffuse = diffuse * (1.0 / (1.0 + (0.25 * distance * distance)));    \n"
		
		// Add attenuation.
		+"diffuse = diffuse * (1.0 / (1.0 + (0.10 * distance)));				 \n"
			
		// Add ambient lighting
		+"diffuse = diffuse + 0.3;												 \n"
			
//		// Multiply the color by the diffuse illumination level to get final output color.
//		+"	gl_FragColor = v_Color * diffuse;    								\n"
		
		// Multiply the color by the diffuse illumination level and texture value to get final output color.
		+"gl_FragColor = (v_Color * diffuse * texture2D(u_Texture, v_TexCoordinate));	\n"
		
		+"}    \n";
		return fragmentShader;
	}
	
	/**
	 * 通过文件的方式加载顶点着色器
	 * @return
	 */
	public String getVertexShaderBlending() {
		// TODO Auto-generated method stub
		return GLES20Utils.readTextFileFromRawResource(context, R.raw.blending_vertex);
	}

	/**
	 * 通过文件的方式加载片段着色器
	 * @return
	 */
	public String getFragmentShaderBlending() {
		// TODO Auto-generated method stub
		return GLES20Utils.readTextFileFromRawResource(context, R.raw.blending_fragment);
	}
	

	public void drawCube(int mPositionHandle, int mColorHandle,
			int mNormalHandle, float[] mViewMatrix, float[] mModelMatrix,
			int mMVMatrixHandle, float[] mProjectionMatrix,
			int mMVPMatrixHandle, int mLightPosHandle,
			float[] mLightPosInEyeSpace,int mTextureCoordinateHandle) {
		// TODO Auto-generated method stub
		
		// Pass in the position information
		mCubePositions.position(0);
		GLES20.glVertexAttribPointer(mPositionHandle, mPositionDataSize,
				GLES20.GL_FLOAT, false, 0, mCubePositions);	// 传入位置坐标信息

		GLES20.glEnableVertexAttribArray(mPositionHandle);

		
		
		// Pass in the color information
		if(mColorHandle >=0){
			mCubeColors.position(0);
			GLES20.glVertexAttribPointer(mColorHandle, mColorDataSize,
					GLES20.GL_FLOAT, false, 0, mCubeColors);	// 传入颜色信息

			GLES20.glEnableVertexAttribArray(mColorHandle);
		}
		

		// Pass in the normal information
		mCubeNormals.position(0);
		GLES20.glVertexAttribPointer(mNormalHandle, mNormalDataSize,
				GLES20.GL_FLOAT, false, 0, mCubeNormals);	// 传入法线信息

		GLES20.glEnableVertexAttribArray(mNormalHandle);
		
		
		// Pass in the texture coordinate information
        mCubeTextureCoordinates.position(0);
        GLES20.glVertexAttribPointer(mTextureCoordinateHandle, mTextureCoordinateDataSize, GLES20.GL_FLOAT, false, 
        		0, mCubeTextureCoordinates);
        GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);	// 传入贴图信息
		

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

	public void drawPlane(int mPositionHandle, int mColorHandle,
			int mNormalHandle, float[] mViewMatrix, float[] mModelMatrix,
			int mMVMatrixHandle, float[] mProjectionMatrix,
			int mMVPMatrixHandle, int mLightPosHandle,
			float[] mLightPosInEyeSpace, int mGrassDataHandle) {
		// TODO Auto-generated method stub
		
		// 传入材质坐标信息
		// Pass in the texture coordinate information
		mCubeTextureCoordinatesForPlane.position(0);
		GLES20.glVertexAttribPointer(mGrassDataHandle,
				mTextureCoordinateDataSize, GLES20.GL_FLOAT, false, 0,
				mCubeTextureCoordinatesForPlane);
		GLES20.glEnableVertexAttribArray(mGrassDataHandle); // 传入贴图信息

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
}
