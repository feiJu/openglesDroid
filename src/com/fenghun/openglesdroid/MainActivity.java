package com.fenghun.openglesdroid;

import com.fenghun.openglesdroid.jni.MyOpenglES;
import com.fenghun.openglesdroid.jni.view.GLES10SurfaceView;
import com.fenghun.openglesdroid.jni.view.GLES20SurfaceView;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FrameLayout surfaceViewFL = (FrameLayout) findViewById(R.id.surfaceViewFL);
        
        GLES20SurfaceView surfaceView = new GLES20SurfaceView(this);
        //GLES10SurfaceView surfaceView = new GLES10SurfaceView(this);
        
        surfaceViewFL.addView(surfaceView);
        MyOpenglES.test();
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
