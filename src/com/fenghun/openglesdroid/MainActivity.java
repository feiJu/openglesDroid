package com.fenghun.openglesdroid;

import com.fenghun.openglesdroid.jni.MyOpenglES;
import com.fenghun.openglesdroid.jni.view.GLES10SurfaceView;
import com.fenghun.openglesdroid.jni.view.GLES20SurfaceView;
import com.fenghun.openglesdroid.jni.view.LessonFiveRenderer;
import com.fenghun.openglesdroid.jni.view.TestBlendingGL20SurfaceView;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		FrameLayout surfaceViewFL = (FrameLayout) findViewById(R.id.surfaceViewFL);

//		TestBlendingGL20SurfaceView tbglSurfaceview = new TestBlendingGL20SurfaceView(
//				this);
//
//		// Request an OpenGL ES 2.0 compatible context.
//		if(isSurportGLES20()) tbglSurfaceview.setEGLContextClientVersion(2);
//
//		// Set the renderer to our demo renderer, defined below.
//		tbglSurfaceview.setRenderer(new LessonFiveRenderer(this));
//		surfaceViewFL.addView(tbglSurfaceview);
		
		
		final DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		GLES20SurfaceView surfaceView = new GLES20SurfaceView(this,displayMetrics.density);
		// GLES10SurfaceView surfaceView = new GLES10SurfaceView(this);

		surfaceViewFL.addView(surfaceView);
		MyOpenglES.test();
	}

	public boolean isSurportGLES20() {
		// Check if the system supports OpenGL ES 2.0.
		final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		final ConfigurationInfo configurationInfo = activityManager
				.getDeviceConfigurationInfo();
		final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;

		if (supportsEs2) {
			return true;
		} else {
			// This is where you could create an OpenGL ES 1.x compatible
			// renderer if you wanted to support both ES 1 and ES 2.
			return false;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
