package com.fenghun.openglesdroid;

import com.fenghun.openglesdroid.jni.MyOpenglES;
import com.fenghun.openglesdroid.jni.view.GLES10SurfaceView;
import com.fenghun.openglesdroid.jni.view.GLES20SurfaceView;
import com.fenghun.openglesdroid.jni.view.LessonFiveRenderer;
import com.fenghun.openglesdroid.jni.view.TestBlendingGL20SurfaceView;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLES20;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

public class MainActivity extends Activity {

	private static final int MIN_DIALOG = 1;
	private static final int MAG_DIALOG = 2;
	
	private int mMinSetting = -1;
	private int mMagSetting = -1;
	
	private GLES20SurfaceView surfaceView;
	
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
		surfaceView = new GLES20SurfaceView(this,displayMetrics.density);
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
	
	/**
	 * 
	 * @param view
	 */
	public void setMinFilter(View view){
		showDialog(MIN_DIALOG);		
	}
	
	/**
	 * 
	 * @param view
	 */
	public void setMagFilter(View view){
		showDialog(MAG_DIALOG);		
	}
	
	
	private void setMinSetting(final int item)
	{
		System.out.println("------------- item ="+item);
		mMinSetting = item;
		
		surfaceView.queueEvent(new Runnable()
		{
			@Override
			public void run()
			{
				final int filter;
				
				if (item == 0)
				{
					filter = GLES20.GL_NEAREST;
				}
				else if (item == 1)
				{
					filter = GLES20.GL_LINEAR;
				}
				else if (item == 2)
				{
					filter = GLES20.GL_NEAREST_MIPMAP_NEAREST;
				}
				else if (item == 3)
				{
					filter = GLES20.GL_NEAREST_MIPMAP_LINEAR;
				}
				else if (item == 4)
				{
					filter = GLES20.GL_LINEAR_MIPMAP_NEAREST;
				}
				else // if (item == 5)
				{
					filter = GLES20.GL_LINEAR_MIPMAP_LINEAR;
				}
				
				surfaceView.setMinFilter(filter);
			}
		});
	}
	
	private void setMagSetting(final int item)
	{
		mMagSetting = item;
		
		surfaceView.queueEvent(new Runnable()
		{
			@Override
			public void run()
			{
				final int filter;
				
				if (item == 0)
				{
					filter = GLES20.GL_NEAREST;
				}
				else // if (item == 1)
				{
					filter = GLES20.GL_LINEAR;
				}	    						
				
				surfaceView.setMagFilter(filter);
			}
		});
	}
	
	@Override
	protected Dialog onCreateDialog(int id) 
	{
	    Dialog dialog = null;
	    
	    switch(id) 
	    {
	    	case MIN_DIALOG:
	    	{
	    		AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    		builder.setTitle(getText(R.string.lesson_six_set_min_filter_message));
	    		builder.setItems(getResources().getStringArray(R.array.lesson_six_min_filter_types), new DialogInterface.OnClickListener() 	    		
	    		{
	    			@Override
	    		    public void onClick(final DialogInterface dialog, final int item) 
	    			{
	    				setMinSetting(item);
	    		    }
	    		});
	    		
	    		dialog = builder.create();
	    	}
	        break;
	    	case MAG_DIALOG:
	    	{
	    		AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    		builder.setTitle(getText(R.string.lesson_six_set_mag_filter_message));
	    		builder.setItems(getResources().getStringArray(R.array.lesson_six_mag_filter_types), new DialogInterface.OnClickListener() 	    		
	    		{
	    			@Override
	    		    public void onClick(final DialogInterface dialog, final int item) 
	    			{
	    				setMagSetting(item);
	    		    }
	    		});
	    		
	    		dialog = builder.create();
	    	}
	        break;
	    default:
	        dialog = null;
	    }
	    
	    return dialog;
	}
}
