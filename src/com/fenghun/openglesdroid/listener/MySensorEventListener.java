package com.fenghun.openglesdroid.listener;

import com.fenghun.openglesdroid.MainActivity;
import com.fenghun.openglesdroid.vr.beans.Sphere;
import com.fenghun.openglesdroid.vr.view.VRSurfaceView;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class MySensorEventListener implements SensorEventListener{

	private MainActivity mainActivity;
	
	public MySensorEventListener(MainActivity mainActivity) {
		// TODO Auto-generated constructor stub
		this.mainActivity = mainActivity;
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            // convert the rotation-vector to a 4x4 matrix. the matrix
            // is interpreted by Open GL as the inverse of the
            // rotation-vector, which is what we want.
			// 可以將之轉換為四元數：
			// 		x = s * Xa	绕x轴旋转的值
			//		y = s * Ya  绕y轴旋转的值
			//		z = s * Za	绕z轴旋转的值
			//		w = cos(θ/2)	旋转角的余弦值
			//		s = sin(θ/2)	旋转角的正弦值
        	float[] rotation_vec = event.values;
        	//System.out.println("--------------rotation_vec.length=="+rotation_vec.length);
    
        	VRSurfaceView surfVR = mainActivity.getSurfaceViewVR();
        	if(surfVR != null){
        		Sphere sv = surfVR.getVrRender().getSphereView();
        		if(sv != null){
        			SensorManager.getRotationMatrixFromVector(
            				sv.getmRotationMatrix() , rotation_vec);
        			
        		}
        	}   
        }
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

}
