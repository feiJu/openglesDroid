package com.fenghun.openglesdroid.jni.bean;

import javax.microedition.khronos.opengles.GL10;

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
		
//		short indices[] = { 
//				0, 4, 5, 
//				0, 5, 1, // 下面
//
//				1, 2, 6,
//				1, 6, 5, // 右面
//
//				5, 6, 7,
//				5, 7, 4, // 前面
//
//				4, 7, 0,
//				4, 0, 3, // 左面
//				
//				3, 7, 6, 
//				3, 6, 2, // top
//				
//				2, 1, 0, 
//				2, 0, 3 // back
//		};

		// short indices[] = { 0, 4, 5,
		//
		// 0, 5, 1,
		//
		// 1, 5, 6,
		//
		// 1, 6, 2,
		//
		// 2, 6, 7,
		//
		// 2, 7, 3,
		//
		// 3, 7, 4,
		//
		// 3, 4, 0,
		//
		// 4, 7, 6,
		//
		// 4, 6, 5,
		//
		// 3, 0, 1,
		//
		// 3, 1, 2,};
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
