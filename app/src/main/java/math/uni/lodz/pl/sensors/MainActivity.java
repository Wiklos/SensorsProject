package math.uni.lodz.pl.sensors;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    TextView textView;
    TextView textViewP;
    SensorManager manager;
    Sensor sensor;
    Sensor pSensor;
    Sensor mSensor;
    Sensor lSensor;
    Sensor gSensor;
    Sensor aSensor;
    Sensor trSensor;


    //flashlight
    private CameraManager mCameraManager;
    private String mCameraId;
    private Boolean isTorchOn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        textView = (TextView)findViewById(R.id.textView);
        textViewP = (TextView)findViewById(R.id.textView2);

        manager = (SensorManager)getSystemService(Service.SENSOR_SERVICE);
        sensor = manager.getDefaultSensor(Sensor.TYPE_LIGHT);
        pSensor = manager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        mSensor = manager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        lSensor = manager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        gSensor = manager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        aSensor = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        trSensor = manager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);


        textViewP.setText("Proximity max range" + pSensor.getMaximumRange());

        //flashlight
        isTorchOn = false;

        Boolean isFlashAvailable = getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        if (!isFlashAvailable) {

            AlertDialog alert = new AlertDialog.Builder(MainActivity.this)
                    .create();
            alert.setTitle("Error !!");
            alert.setMessage("Your device doesn't support flash light!");
            alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // closing the application
                    finish();
                    System.exit(0);
                }
            });
            alert.show();
            return;
        }
        mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            mCameraId = mCameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        manager.unregisterListener(this);
        if(isTorchOn){
            turnOffFlashLight();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        manager.registerListener(this, sensor,manager.SENSOR_DELAY_NORMAL);
        manager.registerListener(this, pSensor,manager.SENSOR_DELAY_NORMAL);
        //manager.registerListener(this, mSensor,manager.SENSOR_DELAY_NORMAL);
        //manager.registerListener(this, lSensor,manager.SENSOR_DELAY_NORMAL);
        //manager.registerListener(this, gSensor,manager.SENSOR_DELAY_NORMAL);
       // manager.registerListener(this, aSensor,manager.SENSOR_DELAY_NORMAL);
        //manager.registerListener(this, trSensor,manager.SENSOR_DELAY_NORMAL);
        if(isTorchOn){
            turnOnFlashLight();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(isTorchOn){
            turnOffFlashLight();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor.getType()== Sensor.TYPE_LIGHT) {
            textView.setText("" + sensorEvent.values[0]);

        //flashlight
        if(sensorEvent.values[0]<10)
        {
            //Toast.makeText(getApplicationContext(),"Toast",Toast.LENGTH_SHORT).show();
            turnOnFlashLight();
            isTorchOn = true;
        }
        else
        {
            turnOffFlashLight();
            isTorchOn = false;
        }
        }
        //proximity
        if(sensorEvent.sensor.getType()==Sensor.TYPE_PROXIMITY)
            textViewP.setText("Proximity sensor:" + sensorEvent.values[0]);

        //magnetic
        if(sensorEvent.sensor.getType()==Sensor.TYPE_MAGNETIC_FIELD)
        textViewP.setText("Magnetic field strength:" + sensorEvent.values[0]);

        //linear acc
        if(sensorEvent.sensor.getType()==Sensor.TYPE_LINEAR_ACCELERATION)
            textViewP.setText("Linear acceletarion value:" + sensorEvent.values[0]);
        //gravity
        if(sensorEvent.sensor.getType()==Sensor.TYPE_GRAVITY)
            textViewP.setText("Gravity:" + sensorEvent.values[0]);
        //acc
        if(sensorEvent.sensor.getType()==Sensor.TYPE_ACCELEROMETER)
            textViewP.setText("Acceleration:" + sensorEvent.values[0]);
        //TYPE_ROTATION_VECTOR
        if(sensorEvent.sensor.getType()==Sensor.TYPE_ROTATION_VECTOR)
            textViewP.setText("Rotation:" + sensorEvent.values[0]);




    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    //flashlight

    public void turnOnFlashLight() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mCameraManager.setTorchMode(mCameraId, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void turnOffFlashLight() {

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mCameraManager.setTorchMode(mCameraId, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void gotoMaps(View view)
    {
        startActivity(new Intent(getApplicationContext(), MapsActivity.class));
    }

}
