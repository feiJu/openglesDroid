package com.fenghun.openglesdroid.vr.beans;

import com.fenghun.openglesdroid.jni.bean20.Constant;

/**
 * 球体 
 * 
 * @author fenghun5987@gmail.com
 * @date Oct 23, 2016
 */
public class Sphere {

	private float radius = 0.8f;	// 球体半径
	
	private float[] verticesCoordinates;

	
	/**
	 * 获取顶点坐标
	 * 原理见 http://www.tuicool.com/articles/Qjm6bmy
	 * @return
	 */
	public float[] getVerticesCoordinates() {
		
		int angleSpan = 2;
		for (int vAngle = -90; vAngle < 90; vAngle = vAngle + angleSpan)// 垂直方向angleSpan度一份
		{
			for (int hAngle = 0; hAngle <= 360; hAngle = hAngle + angleSpan)// 水平方向angleSpan度一份
			{// 纵向横向各到一个角度后计算对应的此点在球面上的坐标
				
				float x0 = (float) (radius * Constant.UNIT_SIZE
						* Math.cos(Math.toRadians(vAngle)) * Math.cos(Math
						.toRadians(hAngle)));
				float y0 = (float) (radius * Constant.UNIT_SIZE
						* Math.cos(Math.toRadians(vAngle)) * Math.sin(Math
						.toRadians(hAngle)));
				float z0 = (float) (radius * Constant.UNIT_SIZE * Math.sin(Math
						.toRadians(vAngle)));
				
			}
		}
		return verticesCoordinates;
	}
}
