package com.fenghun.openglesdroid.jni.bean;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;

/**
 * 
 * 立方体，为简单起见，这个四面体只可以设置宽度，高度，和深度，没有和Plane一样提供Segments支持。
 * 
 * @author fenghun5987@gmail.com
 * @date 2016-9-23
 * @function
 */
public class Cube extends Mesh {

	// The colors mapped to the vertices.
	float[] colors = { 1f, 0f, 0f, 1f, // vertex 0 red
			0f, 1f, 0f, 1f, // vertex 1 green
			0f, 0f, 1f, 1f, // vertex 2 blue
			1f, 0f, 1f, 1f, // vertex 3 magenta
			1f, 0f, 0f, 1f, // vertex 4 red
			0f, 1f, 0f, 1f, // vertex 5 green
			0f, 0f, 1f, 1f, // vertex 6 blue
			1f, 0f, 1f, 1f, // vertex 7 magenta
	};

	/**
	 * 
	 * 立方体
	 * 
	 * @param width
	 * @param height
	 * @param depth
	 */
	public Cube(float width, float height, float depth) {

		width /= 2;
		height /= 2;
		depth /= 2;
		float vertices[] = { -width, -height, -depth, // 0
				width, -height, -depth, // 1
				width, height, -depth, // 2
				-width, height, -depth, // 3
				-width, -height, depth, // 4
				// -width, height, depth, // 4
				width, -height, depth, // 5
				// width, height, depth, // 5
				width, height, depth, // 6
				// width, -height, depth, // 6
				-width, height, depth // 7
		// -width, -height, depth // 7
		};

		short indices[] = { 
				0, 4, 5,
				0, 5, 1, // bottom
				 
				1, 5, 6,
				1, 6, 2, // right
				
				2,1,0, 
				2,0,3, // back
				
				
				3,2,6,
				3, 6, 7,	// top
				
				
				7,3, 0,
				7, 0, 4,  // left
				
				4,7,6,
				4,6,5	// front
				
				
		};
		// "这个坐标的顺序也是对的，在绘制的时候
		// gl.glEnable(GL10.GL_CULL_FACE);
		// gl.glCullFace(GL10.GL_BACK);会忽略掉后面
		// 所以有花不全的情况，"以上内容理解有误，
		// 绘制的顺序与这个GLES20.glFrontFace(GLES20.GL_CCW);	// 逆时针
		// 属性有关，顶点顺序（逆时针或者顺时针）不同，效果也会不同。
		// 
		
//		 short indices[] = { 0, 4, 5,
//		
//		 0, 5, 1,
//		
//		 1, 5, 6,
//		
//		 1, 6, 2,
//		
//		 2, 6, 7,
//		
//		 2, 7, 3,
//		
//		 3, 7, 4,
//		
//		 3, 4, 0,
//		
//		 4, 7, 6,
//		
//		 4, 6, 5,
//		
//		 3, 0, 1,
//		
//		 3, 1, 2,};
		setIndices(indices);
		setVertices(vertices);
		setColors(colors);
	}

	// @Override
	// public void draw(GL10 gl) {
	// // TODO Auto-generated method stub
	// super.draw(gl);
	// gl.glDrawElements(GL10.GL_LINE_STRIP, numOfIndices,
	// GL10.GL_UNSIGNED_SHORT, indicesBuffer);
	//
	// }
}
