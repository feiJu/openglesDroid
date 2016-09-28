package com.fenghun.openglesdroid.jni.view;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.fenghun.openglesdroid.R;
import com.fenghun.openglesdroid.jni.MyOpenglES;
import com.fenghun.openglesdroid.jni.bean.Base2DGraphics;
import com.fenghun.openglesdroid.jni.bean.Base3DGraphics;
import com.fenghun.openglesdroid.jni.bean.Cube;
import com.fenghun.openglesdroid.jni.bean.FlatColoredSquare;
import com.fenghun.openglesdroid.jni.bean.SimplePlane;
import com.fenghun.openglesdroid.jni.bean.SmoothColoredSquare;
import com.fenghun.openglesdroid.jni.bean.Square;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;

/**
 * 不支持OpenGLES2.0 使用OpenGLES1.0版本
 * 
 * @author fenghun
 * @date 2015-10-20
 */
public class GLES10SurfaceView extends GLSurfaceView implements Renderer {

	private static String TAG = "VideoGLES20SurfaceView";

	static Handler handler;

	private Context context;

	// Initialize our square.
	// Square square = new Square();
	// FlatColoredSquare square = new FlatColoredSquare(); // 顶点着色
	SmoothColoredSquare square = new SmoothColoredSquare(); // 渐变色

	Cube cube = new Cube(1, 1, 1);

	float angle = 0;

	SimplePlane sp = null;

	Base2DGraphics base2DGraphics = null; // 基本2D图形类

	Base3DGraphics base3DGraphics = null; // 基本3D图形类

	public GLES10SurfaceView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		init();
	}

	public GLES10SurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}

	/**
	 * 初始化底层Opengl es 与上层的连接
	 */
	private void init() {
		// TODO Auto-generated method stub
		Log.d(TAG, "------------ init() is called!");
		setEGLContextClientVersion(1); // 学习 按教程的opengl 1.0 操作。
		setRenderer(this);
		// setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY); // 设置为脏模式
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				// case R.id.videoGLES20SurfaceView_refresh_UI:
				// requestRender(); // 重绘UI
				// break;
				default:
					break;
				}
			}
		};
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// TODO Auto-generated method stub
		// Log.d(TAG,
		// "------- onSurfaceCreated(GL10 gl, EGLConfig config) is called!");
		// MyOpenglES.onSurfaceCreated(640, 480);

		base2DGraphics = new Base2DGraphics();
		base3DGraphics = new Base3DGraphics();

		// 创建一个简单的平面，用于绘制材质
		sp = new SimplePlane();
		// 有些设备对使用的Bitmap的大小有要求，要求Bitmap的宽度和长度为2的几次幂（1，2，4，8，16，32，64.。。。)，
		// 如果使用不和要求的Bitmap来渲染，可能只会显示白色。
		sp.loadBitmap(BitmapFactory.decodeResource(context.getResources(),
				R.drawable.ic_launcher)); // 加载bitmap作为材质

		// 颜色的定义通常使用Hex格式0xFF00FF 或十进制格式(255,0,255)，
		// 在OpenGL 中却是使用0…1之间的浮点数表示。 0为0，1相当于255（0xFF)。
		// // Set the background color to black ( rgba ).
		gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f); // OpenGL docs.

		// Enable Smooth Shading, default not really needed.
		gl.glShadeModel(GL10.GL_SMOOTH);// OpenGL docs.

		// // Depth buffer setup.
		gl.glClearDepthf(1.0f);// OpenGL docs.

		// Enables depth testing.
		gl.glEnable(GL10.GL_DEPTH_TEST);// OpenGL docs.

		// The type of depth testing to do.
		gl.glDepthFunc(GL10.GL_LEQUAL);// OpenGL docs.

		// Really nice perspective calculations.
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, // OpenGL docs.
				GL10.GL_NICEST);

	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		// TODO Auto-generated method stub
		Log.d(TAG,
				"----- onSurfaceChanged(GL10 gl, int width, int height) is called!");
		MyOpenglES.onSurfaceChanged(width, height);
		// Sets the current view port to the new size.
		gl.glViewport(0, 0, width, height);// OpenGL docs.

		// Select the projection matrix
		gl.glMatrixMode(GL10.GL_PROJECTION);// OpenGL docs.

		// Reset the projection matrix
		gl.glLoadIdentity();// OpenGL docs.

		// Calculate the aspect ratio of the window
		GLU.gluPerspective(gl, 45.0f, (float) width / (float) height, 0.1f,
				100.0f);

		// Select the modelview matrix
		gl.glMatrixMode(GL10.GL_MODELVIEW);// OpenGL docs.

		// Reset the modelview matrix
		gl.glLoadIdentity();// OpenGL docs.
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		// TODO Auto-generated method stub
		// Log.d(TAG, "------------- onDrawFrame(GL10 gl) is called!");
		// MyOpenglES.onDrawFrame();
		/**
		 * OpenGL ES 内部存放图形数据的Buffer有COLOR ,DEPTH (深度信息）等， 在绘制图形之前一般需要清空COLOR 和
		 * DEPTH Buffer。
		 */
		// Clears the screen and depth buffer.// 清除屏幕和深度缓存
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | // OpenGL docs.
				GL10.GL_DEPTH_BUFFER_BIT);

		// Replace the current matrix with the identity matrix
		// 因为每次调用onDrawFrame 时，glTranslatef(0, 0, -4)每次都再向后移动4个单位，
		// 正方形迅速后移直至看不见,需要加上重置Matrix的代码。
		//gl.glLoadIdentity();// 重置当前的模型观察矩阵，即将当前矩阵设为单位矩阵

		// Translates 4 units into the screen.
		// OpenGL ES从当前位置开始渲染，缺省坐标为(0,0,0)，和View port 的坐标一样，
		// 相当于把画面放在眼前，对应这种情况OpenGL不会渲染离view Port很近的画面，
		// 因此我们需要将画面向后退一点距离
		//gl.glTranslatef(0, 0, -4); // 平移变换，向z轴负方向移动4个单位

		// 绘制2D基本图形
		// base2DGraphics.drawPoints(gl); // 绘制点
		// base2DGraphics.drawLineSegments(gl); // 绘制线段
		// base2DGraphics.drawTrangles(gl); // 绘制三角形
		// base2DGraphics.drawMiniSolarSystem(gl); // 迷你太阳系

		// 绘制3D基本图形
		// base3DGraphics.drawPositive20surface(gl);
		//initSphereScene(gl);
		//base3DGraphics.drawSphere(gl);	// 绘制球体

		base3DGraphics.initTestLightScene(gl);
		base3DGraphics.drawMaterialSphere(gl,true);
		
		
		
		// 绘制正方形
		// drawTestRects(gl);

		// 绘制旋转的立方体
		// if(cube != null) drawCube(gl);

		// 绘制包含材质的简单平面
		// if (sp != null)
		// sp.draw(gl);

	}

	/**
	 * 绘制立方体
	 * 
	 * @param gl
	 */
	private void drawCube(GL10 gl) {
		// TODO Auto-generated method stub
		cube.rx = angle;
		cube.ry = angle;
		cube.draw(gl); // 绘制立方体
		angle++;
		angle = angle % 360;
	}

	/**
	 * 绘制测试正方形
	 */
	private void drawTestRects(GL10 gl) {

		// SQUARE A 以屏幕中心逆时针旋转A
		gl.glTranslatef(0, 0, -10); // 平移变换，向z轴负方向移动4个单位

		// 保存当前矩阵信息
		// Save the current matrix.
		gl.glPushMatrix();
		// 绕z轴逆时针方向旋转矩阵angle度
		// Rotate square A counter-clockwise.
		gl.glRotatef(angle, 0, 0, 1);
		// Draw our square A.
		square.draw(gl);
		// 旋转完成恢复原始矩阵
		// Restore the last matrix.
		gl.glPopMatrix();

		// SQUARE B ，使的B比A小50%。B以A为中心顺时针旋转
		// 保存当前矩阵信息
		// Save the current matrix.
		gl.glPushMatrix();
		// Rotate square B before moving it,
		// making it rotate around A. 此时具有相同的中心点即原点
		gl.glRotatef(-angle, 0, 0, 1);
		// Move square B.向x轴正向移动两个单位
		gl.glTranslatef(2, 0, 0);
		// Scale it to 50% of square A，缩小为A的一半
		gl.glScalef(0.5f, 0.5f, 0.5f);
		// Draw square B.
		square.draw(gl);

		// SQUARE C，C比B小50%，C以B为中心顺时针旋转同时以自己中心高速逆时针旋转。
		// Save the current matrix,当前的矩阵是B变换后的状态
		gl.glPushMatrix();
		// Make the rotation around B，绕z轴
		gl.glRotatef(-angle, 0, 0, 1); // 旋转变换glRotatef(angle, -x, -y, -z)
										// 和glRotatef(-angle, x, y, z)是等价的
		gl.glTranslatef(2, 0, 0); // 向x轴正向移动两个单位
		// Scale it to 50% of square B，缩小为B的一半
		gl.glScalef(0.5f, 0.5f, 0.5f);
		// Rotate around it's own center.
		gl.glRotatef(angle * 10, 0, 0, 1);
		// Draw square C.
		square.draw(gl);

		// Restore to the matrix as it was before C.
		gl.glPopMatrix();
		// Restore to the matrix as it was before B.
		gl.glPopMatrix();

		// Increse the angle.
		angle++;
		angle = angle % 360;
		// Log.d(TAG, "----------- angle="+angle);
	}

	/**
	 * 为了看出球体的效果，添加光照效果
	 * 
	 * @param gl
	 */
	private void initSphereScene(GL10 gl) {
		float[] mat_amb = { 0.2f * 1.0f, 0.2f * 0.4f, 0.2f * 0.4f, 1.0f, };
		float[] mat_diff = { 1.0f, 0.4f, 0.4f, 1.0f, };
		float[] mat_spec = { 1.0f, 1.0f, 1.0f, 1.0f, };

		ByteBuffer mabb = ByteBuffer.allocateDirect(mat_amb.length * 4);
		mabb.order(ByteOrder.nativeOrder());
		FloatBuffer mat_ambBuf = mabb.asFloatBuffer();
		mat_ambBuf.put(mat_amb);
		mat_ambBuf.position(0);

		ByteBuffer mdbb = ByteBuffer.allocateDirect(mat_diff.length * 4);
		mdbb.order(ByteOrder.nativeOrder());
		FloatBuffer mat_diffBuf = mdbb.asFloatBuffer();
		mat_diffBuf.put(mat_diff);
		mat_diffBuf.position(0);

		ByteBuffer msbb = ByteBuffer.allocateDirect(mat_spec.length * 4);
		msbb.order(ByteOrder.nativeOrder());
		FloatBuffer mat_specBuf = msbb.asFloatBuffer();
		mat_specBuf.put(mat_spec);
		mat_specBuf.position(0);

		gl.glClearColor(0.8f, 0.8f, 0.8f, 0.0f);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glEnable(GL10.GL_CULL_FACE);
		gl.glShadeModel(GL10.GL_SMOOTH);

		gl.glEnable(GL10.GL_LIGHTING);
		gl.glEnable(GL10.GL_LIGHT0);

		
		/**
		 * 设置物体表面材料(Material)的反光属性（颜色和材质）
		 * 
		 * face : 在OpenGL ES中只能使用GL_FRONT_AND_BACK，表示修改物体的前面和后面的材质光线属性。
		 * pname: 参数类型，可以有GL_AMBIENT, GL_DIFFUSE, GL_SPECULAR, GL_EMISSION, GL_SHININESS。这些参数用在光照方程。
		 * param：  参数的值。
		 * 其中GL_AMBIENT,GL_DIFFUSE,GL_SPECULAR ，GL_EMISSION为颜色RGBA值，GL_SHININESS 值可以从0到128，值越大，光的散射越小：
		 */
		gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT, mat_ambBuf);
		gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, mat_diffBuf);
		gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SPECULAR, mat_specBuf);
		gl.glMaterialf(GL10.GL_FRONT_AND_BACK, GL10.GL_SHININESS, 64.0f);

		gl.glLoadIdentity();
		GLU.gluLookAt(gl, 
				
				0.0f, 0.0f, 10.0f, 
				
				0.0f, 0.0f, 0.0f, 
				
				0.0f, 1.0f, 0.0f);
	}

}
