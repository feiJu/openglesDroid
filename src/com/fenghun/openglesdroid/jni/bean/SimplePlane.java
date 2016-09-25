package com.fenghun.openglesdroid.jni.bean;

/**
 * 
 * 简单的平面，用于展示材质的绘制
 * 
 * @author fenghun5987@gmail.com
 * @date 2016-9-26
 * @function
 */
public class SimplePlane extends Mesh {

	/**
	 * 
	 * Create a plane with a default with and height of 1 unit.
	 */
	public SimplePlane() {
		this(1, 1);
	}

	/**
	 * Create a plane.
	 * 
	 * 
	 * 
	 * @param width
	 * 
	 *            the width of the plane.
	 * 
	 * @param height
	 * 
	 *            the height of the plane.
	 */

	public SimplePlane(float width, float height) {

		float[] vertices = new float[] { 
				-0.5f, -0.5f, 0.0f, // LB 
				0.5f, -0.5f, 0.0f, // RB
				-0.5f, 0.5f, 0.0f,  // LT
				0.5f, 0.5f, 0.0f }; // RT

		short[] indices = new short[] { 0, 1, 2, 1, 3, 2 };

		// 材质为图片的4倍
		// 为了能正确的匹配，需要把UV坐标中的点映射vertices
		// 顺序与indices一致
		// Mapping coordinates for the vertices
		float textureCoordinates[] = { 0.0f, 2.0f, //
				2.0f, 2.0f, //
				0.0f, 0.0f, //
				2.0f, 0.0f, //
		};

		// 材质和图片1:1
		// 坐标系为图片的坐标系，左上点为原点，向右为x正向，向下为y正向
//		float textureCoordinates[] = {0.0f, 1.0f,	// LB
//				
//					 1.0f, 1.0f,	// RB
//				
//					 0.0f, 0.0f,	// LT
//				
//					 1.0f, 0.0f };	// RT
		
		setIndices(indices);
		setVertices(vertices);
		setTextureCoordinates(textureCoordinates);
	}
}
