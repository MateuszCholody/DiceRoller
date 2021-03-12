package com.mcholody.diceroller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private boolean color = false;
    private long lastUpdate;
    private Random random = new Random();
    private ImageView diceImage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        diceImage = findViewById(R.id.image_view_dice);
        diceImage.setOnClickListener((v) -> rollDice());

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lastUpdate = System.currentTimeMillis();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            getAccelerometer(event);
        }

    }

    private void getAccelerometer(SensorEvent event) {
        float[] values = event.values;
        // Movement
        float x = values[0];
        float y = values[1];
        float z = values[2];

        Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        float accelationSquareRoot = (x * x + y * y + z * z) / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
        long actualTime = event.timestamp;
        if (accelationSquareRoot >= 1.5) //
        {
            if (actualTime - lastUpdate < 500) {
                return;
            }
            lastUpdate = actualTime;
            Toast.makeText(this, "Dice rolled", Toast.LENGTH_SHORT).show();
            rollDice();
            vibe.vibrate(VibrationEffect.DEFAULT_AMPLITUDE);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        // register this class as a listener for the orientation and
        // accelerometer sensors
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        // unregister listener
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    private void rollDice() {
        int randomNumber = random.nextInt(6) + 1;
        switch (randomNumber) {
            case 1:
                diceImage.setImageResource(R.drawable.ic_dice_1);
                break;
            case 2:
                diceImage.setImageResource(R.drawable.ic_dice_2);
                break;
            case 3:
                diceImage.setImageResource(R.drawable.ic_dice_3);
                break;
            case 4:
                diceImage.setImageResource(R.drawable.ic_dice_4);
                break;
            case 5:
                diceImage.setImageResource(R.drawable.ic_dice_5);
                break;
            default:
                diceImage.setImageResource(R.drawable.ic_dice_6a);
                break;
        }
    }
}
