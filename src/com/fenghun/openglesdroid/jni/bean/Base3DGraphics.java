package com.fenghun.openglesdroid.jni.bean;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLU;

/**
 * 基本的3d图形类
 * 
 * @author fenghun5987@gmail.com
 * @date 2016-9-27
 * @function
 */

public class Base3DGraphics {
	static final float X = .525731112119133606f;

	static final float Z = .850650808352039932f;

	// 正20面体的12个顶点
	static float vertices_20[] = new float[] {

	-X, 0.0f, Z, // 1

			X, 0.0f, Z, // 2

			-X, 0.0f, -Z, // 3

			X, 0.0f, -Z, // 4

			0.0f, Z, X, // 5

			0.0f, Z, -X, // 6

			0.0f, -Z, X, // 7

			0.0f, -Z, -X,// 8

			Z, X, 0.0f,// 9

			-Z, X, 0.0f, // 10

			Z, -X, 0.0f, // 11

			-Z, -X, 0.0f // 12

	};

	// 顶点的绘制顺序
	static short indices_20[] = new short[] {

	0, 4, 1,

	0, 9, 4,

	9, 5, 4,

	4, 5, 8,

	4, 8, 1,

	8, 10, 1,

	8, 3, 10,

	5, 3, 8,

	5, 2, 3,

	2, 7, 3,

	7, 10, 3,

	7, 6, 10,

	7, 11, 6,

	11, 0, 6,

	0, 1, 6,

	6, 1, 10,

	9, 0, 11,

	9, 11, 2,

	9, 2, 5,

	7, 2, 11 };

	// 为了能够更好的显示3D效果，我们为每个顶点随机定义一些颜色如下：
	float[] colors20 = {

	0f, 0f, 0f, 1f, // 1

			0f, 0f, 1f, 1f,// 2

			0f, 1f, 0f, 1f,// 3

			0f, 1f, 1f, 1f,// 4

			1f, 0f, 0f, 1f,// 5

			1f, 0f, 1f, 1f,// 6

			1f, 1f, 0f, 1f,// 7

			1f, 1f, 1f, 1f,// 8

			1f, 0f, 0f, 1f,// 9

			0f, 1f, 0f, 1f,// 10

			0f, 0f, 1f, 1f,// 11

			1f, 0f, 1f, 1f // 12
	};

	private FloatBuffer vertex20Buffer;

	private FloatBuffer color20Buffer;

	private ShortBuffer index20Buffer;

	private float angle = 0f;

	public Base3DGraphics() {
		// TODO Auto-generated constructor stub

		// 顶点缓存
		ByteBuffer vbb20 = ByteBuffer.allocateDirect(vertices_20.length * 4);
		vbb20.order(ByteOrder.nativeOrder());
		vertex20Buffer = vbb20.asFloatBuffer();
		vertex20Buffer.put(vertices_20);
		vertex20Buffer.position(0);

		// 颜色缓存
		ByteBuffer cbb20 = ByteBuffer.allocateDirect(colors20.length * 4);
		cbb20.order(ByteOrder.nativeOrder());
		color20Buffer = cbb20.asFloatBuffer();
		color20Buffer.put(colors20);
		color20Buffer.position(0);

		// 顶点绘制顺序缓存
		ByteBuffer ibb20 = ByteBuffer.allocateDirect(indices_20.length * 2);
		ibb20.order(ByteOrder.nativeOrder());
		index20Buffer = ibb20.asShortBuffer();
		index20Buffer.put(indices_20);
		index20Buffer.position(0);
	}

	/**
	 * 绘制正20面体
	 * 
	 * @param gl
	 */
	public void drawPositive20surface(GL10 gl) {

		gl.glColor4f(1.0f, 0.0f, 0.0f, 1.0f);

		gl.glLoadIdentity(); // 重置当前模型的观察矩阵

		gl.glTranslatef(0, 0, -4); // 向z轴负方向平移4个单位

		gl.glRotatef(0f, 1, 0, 0); // 绕x轴旋转0
		gl.glRotatef(-angle, 0, 1, 0); // 绕y轴旋转
		gl.glRotatef(-angle, 0, 0, 1); // 绕z轴旋转

		gl.glFrontFace(GL10.GL_CCW); // 设置逆时针方法为面的“前面”

		gl.glEnable(GL10.GL_CULL_FACE); // 开启 面忽略设置

		gl.glCullFace(GL10.GL_BACK); // 指明忽略后面

		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY); // 开启顶点数组传入功能

		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertex20Buffer);// 传入顶点数组

		gl.glEnableClientState(GL10.GL_COLOR_ARRAY); // 开启颜色数组传入功能

		gl.glColorPointer(4, GL10.GL_FLOAT, 0, color20Buffer); // 传入颜色顶点数组

		gl.glDrawElements(GL10.GL_TRIANGLES, indices_20.length,

		GL10.GL_UNSIGNED_SHORT, index20Buffer); // 以indices_20的顺序绘制顶点，

		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY); // 关闭顶点数组传入功能

		gl.glDisableClientState(GL10.GL_COLOR_ARRAY); // 关闭颜色数组传入功能

		gl.glDisable(GL10.GL_CULL_FACE); // 关闭面忽略功能

		// 改变旋转角度
		angle++;
		angle = angle % 360;
	}

	/**
	 * 绘制球体
	 */
	public void drawSphere(GL10 gl) {
		
		float theta, pai;
		float co, si;
		float r1, r2;
		float h1, h2;
		float step = 2.0f;
		float[][] v = new float[32][3];

		ByteBuffer vbb;
		FloatBuffer vBuf;

		vbb = ByteBuffer.allocateDirect(v.length * v[0].length * 4);
		vbb.order(ByteOrder.nativeOrder());
		
		vBuf = vbb.asFloatBuffer();

		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);

		for (pai = -90.0f; pai < 90.0f; pai += step) {

			int n = 0;

			r1 = (float) Math.cos(pai * Math.PI / 180.0);

			r2 = (float) Math.cos((pai + step) * Math.PI / 180.0);

			h1 = (float) Math.sin(pai * Math.PI / 180.0);

			h2 = (float) Math.sin((pai + step) * Math.PI / 180.0);

			for (theta = 0.0f; theta <= 360.0f; theta += step) {

				co = (float) Math.cos(theta * Math.PI / 180.0);

				si = -(float) Math.sin(theta * Math.PI / 180.0);

				v[n][0] = (r2 * co);

				v[n][1] = (h2);

				v[n][2] = (r2 * si);

				v[n + 1][0] = (r1 * co);

				v[n + 1][1] = (h1);

				v[n + 1][2] = (r1 * si);

				vBuf.put(v[n]);

				vBuf.put(v[n + 1]);

				n += 2;

				if (n > 31) {

					vBuf.position(0);

					gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vBuf);

					gl.glNormalPointer(GL10.GL_FLOAT, 0, vBuf);

					gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, n);

					n = 0;

					theta -= step;

				}

			}

			vBuf.position(0);

			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vBuf);

			gl.glNormalPointer(GL10.GL_FLOAT, 0, vBuf);

			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, n);

		}

		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);

		gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
	}

	/**
	 * 初始化测试光照场景
	 * 
	 * 1. 平行光
	 * 
	 *  
	 *  
	 *  
	 * @param gl
	 */
	public void initTestLightScene(GL10 gl) {

		float[] ambient = { 1.0f, 1.0f, 1.0f, 1.0f, };	// 环境光，分别指定R,G,B,A 的值。
		float[] diffuse = { 1.0f, 1.0f, 1.0f, 1.0f, };	// 漫反射，分别指定R,G,B,A 的值。
		float[] specular = { 1.0f, 1.0f, 1.0f, 1.0f, };	// 镜面反射，分别指定R,G,B,A 的值。
		float[] position = { 0.0f, 15.0f, 15.0f, 1.0f, }; 	// 光源（position[3]!=0.0,通常1.0）的位置或者平行光（position[3]=0.0）的方向
		float[] spot_direction = { 0.0f, -1.0f, 0.0f, };	//  将点光源设置成聚光灯，
		gl.glEnable(GL10.GL_DEPTH_TEST);		// 开启深度测试
    	gl.glEnable(GL10.GL_CULL_FACE);	// 开启面忽略

    	gl.glEnable(GL10.GL_LIGHTING);	// 使用光源首先要开光源的总开关：
    	gl.glEnable(GL10.GL_LIGHT0);	// 然后可以再打开某个光源如0号光源
    	
    	
	    ByteBuffer abb = ByteBuffer.allocateDirect(ambient.length*4);
		abb.order(ByteOrder.nativeOrder());
		FloatBuffer ambBuf = abb.asFloatBuffer();
		ambBuf.put(ambient);
		ambBuf.position(0);    	
		
		ByteBuffer dbb = ByteBuffer.allocateDirect(diffuse.length*4);
		dbb.order(ByteOrder.nativeOrder());
		FloatBuffer diffBuf = dbb.asFloatBuffer();
		diffBuf.put(diffuse);
		diffBuf.position(0);    	
		
		ByteBuffer sbb = ByteBuffer.allocateDirect(specular.length*4);
		sbb.order(ByteOrder.nativeOrder());
		FloatBuffer specBuf = sbb.asFloatBuffer();
		specBuf.put(specular);
		specBuf.position(0);       
		
		ByteBuffer pbb = ByteBuffer.allocateDirect(position.length*4);
		pbb.order(ByteOrder.nativeOrder());
		FloatBuffer posBuf = pbb.asFloatBuffer();
		posBuf.put(position);
		posBuf.position(0);
	
		ByteBuffer spbb = ByteBuffer.allocateDirect(spot_direction.length*4);
		spbb.order(ByteOrder.nativeOrder());
		FloatBuffer spot_dirBuf = spbb.asFloatBuffer();
		spot_dirBuf.put(spot_direction);
		spot_dirBuf.position(0);


		/**
		 * light 指光源的序号，OpenGL ES可以设置从0到7共八个光源。
		 * pname: 光源参数名称，可以有如下：
		 * 	GL_SPOT_EXPONENT, 给出了聚光灯光源汇聚光的程度，值越大，则聚光区域越小（聚光能力更强）。
		 * 	GL_SPOT_CUTOFF,  设置聚光等发散角度（0到90度）
		 * 	GL_CONSTANT_ATTENUATION,  
		 * 	GL_LINEAR_ATTENUATION, 
		 * 	GL_QUADRATIC_ATTENUATION, 
		 * 	以上三个为点光源设置光线衰减参数
		 * 
		 * 	GL_AMBIENT, 	// 环境光
		 * 	GL_DIFFUSE,		// 漫反射光
		 * 	GL_SPECULAR, 	// 镜面反射
		 * 	GL_SPOT_DIRECTION, 	// 聚光灯
		 * 	GL_POSITION  指定光源的位置的参数为GL_POSITION,值为(x,y,z,w)：
		 * params 参数的值（数组或是Buffer类型）。
		 * 
		 * 
		 */
    	gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, ambBuf);	// 光源1环境光
    	gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, diffBuf);
    	gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_SPECULAR, specBuf);
    	gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, posBuf);				
    	gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_SPOT_DIRECTION, spot_dirBuf);	
    	gl.glLightf(GL10.GL_LIGHT0, GL10.GL_SPOT_EXPONENT, 0.0f);		
    	gl.glLightf(GL10.GL_LIGHT0, GL10.GL_SPOT_CUTOFF, 60.0f);		
    	
    	gl.glLoadIdentity();
    	GLU.gluLookAt(gl, 
    			0.0f, 4.0f, 14.0f, 	// camera
    			
    			0.0f, 0.0f, 0.0f, // 物体
    			
    			0.0f, 1.0f, 0.0f);	

	}

	/**
	 * 绘制设置材料的球体
	 * 
	 * 最终顶点的颜色由这些参数（光源，材质光学属性，光照模型）综合决定（光照方程计算出）。
	 * 
	 * @param gl
	 * @param isMaterial
	 */
	public void drawMaterialSphere(GL10 gl,boolean isMaterial) {
		float[] mat_amb = { 0.2f * 0.4f, 0.2f * 0.4f, 0.2f * 1.0f, 1.0f, };

		float[] mat_diff = { 0.4f, 0.4f, 1.0f, 1.0f, };

		float[] mat_spec = { 1.0f, 1.0f, 1.0f, 1.0f, };

		ByteBuffer mabb = ByteBuffer.allocateDirect(mat_amb.length * 4);
		mabb.order(ByteOrder.nativeOrder());

		FloatBuffer mat_ambBuf = mabb.asFloatBuffer();
		mat_ambBuf.put(mat_amb);
		mat_ambBuf.position(0);

		ByteBuffer mdbb= ByteBuffer.allocateDirect(mat_diff.length * 4);
		mdbb.order(ByteOrder.nativeOrder());
		FloatBuffer mat_diffBuf = mdbb.asFloatBuffer();
		mat_diffBuf.put(mat_diff);
		mat_diffBuf.position(0);

		ByteBuffer msbb= ByteBuffer.allocateDirect(mat_spec.length * 4);
		msbb.order(ByteOrder.nativeOrder());
		FloatBuffer mat_specBuf = msbb.asFloatBuffer();
		mat_specBuf.put(mat_spec);
		mat_specBuf.position(0);

		// 设置材料的反光属性
		gl.glMaterialfv(GL10.GL_FRONT_AND_BACK,GL10.GL_AMBIENT, mat_ambBuf);	// 环境光参数，RGBA的值
		gl.glMaterialfv(GL10.GL_FRONT_AND_BACK,GL10.GL_DIFFUSE, mat_diffBuf);	// 漫反射参数，RGBA的值
		gl.glMaterialfv(GL10.GL_FRONT_AND_BACK,GL10.GL_SPECULAR, mat_specBuf);// 镜面参数，RGBA的值
		gl.glMaterialf(GL10.GL_FRONT_AND_BACK,GL10.GL_SHININESS, 64.0f);	// 从0到128，值越大，光的散射越小：
		
		drawSphere(gl);
	}

}
