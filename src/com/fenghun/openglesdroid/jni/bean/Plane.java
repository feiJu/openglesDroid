package com.fenghun.openglesdroid.jni.bean;

/**
 * 
 * plane可以有宽度，高度和深度，宽度定义为沿X轴方向的长度，深度定义为沿Z轴方向长度，高度为Y轴方向。
 * 
 * Segments为形体宽度，高度，深度可以分成的份数。
 * Segments在构造一个非均匀分布的Surface特别有用，比如在一个游戏场景中，构造地貌，使的Z轴的值随机分布在
 * -0.1到0.1之间，然后给它渲染好看的材质就可以造成地图凹凸不平的效果。
 * 
 * @author fenghun5987@gmail.com
 * @date 2016-9-23
 * @function
 */
public class Plane extends Mesh {

	public Plane() {
		this(1, 1, 1, 1);
	}

	/**
	 * Let you decide the size of the plane but still only one segment.
	 * 
	 * 设置平面的宽度和高度，只有一个碎片（分割），即平面本身
	 * 
	 * @param width
	 *            平面的宽度
	 * @param height
	 *            平面的高度
	 */
	public Plane(float width, float height) {
		this(width, height, 1, 1);
	}

	/**
	 * For alla your settings. 可以调整所有的设置
	 * 
	 * @param width
	 *            平面的宽度
	 * @param height
	 *            平面的高度
	 * @param widthSegments
	 *            细分碎片（分割单位）的宽度
	 * @param heightSegments
	 *            细分碎片（分割单位）的高度
	 */
	public Plane(float width, float height, int widthSegments,
			int heightSegments) {

		float[] vertices = new float[(widthSegments + 1) * (heightSegments + 1)
				* 3];

		short[] indices = new short[(widthSegments + 1) * (heightSegments + 1)
				* 6];

		float xOffset = width / -2;

		float yOffset = height / -2;

		float xWidth = width / (widthSegments);

		float yHeight = height / (heightSegments);

		int currentVertex = 0;

		int currentIndex = 0;

		short w = (short) (widthSegments + 1);

		for (int y = 0; y < heightSegments + 1; y++) {

			for (int x = 0; x < widthSegments + 1; x++) {

				vertices[currentVertex] = xOffset + x * xWidth;

				vertices[currentVertex + 1] = yOffset + y * yHeight;

				vertices[currentVertex + 2] = 0;

				currentVertex += 3;

				int n = y * (widthSegments + 1) + x;

				if (y < heightSegments && x < widthSegments) {

					// Face one

					indices[currentIndex] = (short) n;

					indices[currentIndex + 1] = (short) (n + 1);

					indices[currentIndex + 2] = (short) (n + w);

					// Face two

					indices[currentIndex + 3] = (short) (n + 1);

					indices[currentIndex + 4] = (short) (n + 1 + w);

					indices[currentIndex + 5] = (short) (n + 1 + w - 1);

					currentIndex += 6;
				}
			}
		}
		setIndices(indices);	// 设置绘制顺序数组
		setVertices(vertices);	// 设置顶点数组
	}
}
