package com.example.geometryfly;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    GameView view;
    SensorManager sensorManager;
    Sensor acelerometerSensor;
    Point pantalla;
    public static String nombreUsuario;

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Bundle b = getIntent().getExtras();
        nombreUsuario = b.getString("nombre");

        pantalla = new Point();
        getDisplay().getSize(pantalla);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        List<Sensor> listaSensores = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
        if(!listaSensores.isEmpty()){
            acelerometerSensor = listaSensores.get(0);
            sensorManager.registerListener(this,acelerometerSensor,SensorManager.SENSOR_DELAY_UI);
        }

        view = new GameView(this,pantalla,this);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(view);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        synchronized (this){
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
                float y = event.values[0];
                view.setCoordenadaY(y);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this,acelerometerSensor,SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
}