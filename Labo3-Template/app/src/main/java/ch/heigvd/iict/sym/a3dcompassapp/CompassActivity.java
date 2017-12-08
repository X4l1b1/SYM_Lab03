package ch.heigvd.iict.sym.a3dcompassapp;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class CompassActivity extends AppCompatActivity implements SensorEventListener {

    //opengl
    private OpenGLRenderer  opglr           = null;
    private GLSurfaceView   m3DView         = null;
    //sensor manager to access the sensors
    private SensorManager mSensorManager = null;

    private Sensor accSensor = null; //accelerometer sensor
    private Sensor magSensor = null; //magnetometer sensor

    private float [] mGravity = null;
    private float [] mGeomagnetic = null;
    private float [] matrix;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // we need fullscreen
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // we initiate the view
        setContentView(R.layout.activity_compass);

        // link to GUI
        this.m3DView = findViewById(R.id.compass_opengl);

        //we create the 3D renderer
        this.opglr = new OpenGLRenderer(getApplicationContext());

        //init opengl surface view
        this.m3DView.setRenderer(this.opglr);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        // Use the accelerometer.
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null){
            accSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }
        // Use the magnetometer
        if( mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) != null){
            magSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        }
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {}//Not used function

    @Override
    public final void onSensorChanged(SensorEvent event) {
        //Check sensor type
        switch (event.sensor.getType()){
            case Sensor.TYPE_ACCELEROMETER :
                mGravity = event.values;
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                mGeomagnetic = event.values;
                break;
        }
        //Compute the rotation matrix
        mSensorManager.getRotationMatrix(matrix, null, mGravity, mGeomagnetic);
        matrix = this.opglr.swapRotMatrix(matrix);
    }

    @Override
    protected void onResume() { //enable sensors
        super.onResume();
        mSensorManager.registerListener(this, accSensor, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, magSensor, SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    protected void onPause() {//desable sensors
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

}
