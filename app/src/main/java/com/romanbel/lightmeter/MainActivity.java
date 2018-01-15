package com.romanbel.lightmeter;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private TextView mTxtCurrentLux, mTxtMin, mTxtMax, mTxtAverage;
    private SensorManager mySensorManager;
    private Sensor LightSensor;
    private Button mBtnClear, btnSave;
    private ImageButton btnNext;
    private float mMaxValue = 0 ,
                  mMinValue = 1.08E8f,
                  mAverageValue = 0;
    private int mCount;
    private ArrayList<Line> list;
    private String eventString;
    private Integer currentVelue;
    private Timer mTimer;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTxtCurrentLux = findViewById(R.id.txt_current_lux);
        mTxtMin = findViewById(R.id.txt_min);
        mTxtMax = findViewById(R.id.txt_max);
        mTxtAverage = findViewById(R.id.txt_average);
        mBtnClear = findViewById(R.id.btn_clear);
        btnSave = findViewById(R.id.btn_save);
        btnNext = findViewById(R.id.btn_next);


        mySensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mCount = 0;
        mTxtMax.setText("0");
        mTxtMin.setText("0");
        mTxtAverage.setText("0");
        LightSensor = mySensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if(LightSensor != null){
            mySensorManager.registerListener(
                    LightSensorListener,
                    LightSensor,
                    SensorManager.SENSOR_DELAY_NORMAL);

        }else{
            mTxtCurrentLux.setText(R.string.error);
            Toast.makeText(this, getString(R.string.sensor_not_detected), Toast.LENGTH_LONG).show();
        }
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startIntent();
            }
        });
        mBtnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTxtMax.setText(currentVelue.toString());
                mTxtMin.setText(currentVelue.toString());
                mTxtAverage.setText(currentVelue.toString());
                mMaxValue = currentVelue;
                mMinValue = currentVelue;
                mAverageValue = currentVelue;
                mCount = 0;
            }
        });
        list = new ArrayList<>();
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });

        final Handler myHandler = new Handler();
        Timer timer = new java.util.Timer();
        TimerTask task = new TimerTask() {
            public void run()
            {
                myHandler.post(new Runnable() {  // используя Handler, привязанный к UI-Thread
                    @Override
                    public void run() {
                        calculateAverage();        // выполним установку значения
                    }
                });
            }
        };
        timer.schedule( task, 500, 500);


    }
    private void startIntent(){
        Intent intent = new Intent(this, RecyclerActivity.class);
        intent.putParcelableArrayListExtra("List", list);
        startActivity(intent);
    }

    private void save() {
        Calendar calander = Calendar.getInstance();
        int day = calander.get(Calendar.DAY_OF_MONTH);
        int month = calander.get(Calendar.MONTH) + 1;
        int hour = calander.get(Calendar.HOUR_OF_DAY);
        int minute = calander.get(Calendar.MINUTE);
        int second = calander.get(Calendar.SECOND);
        String datTime = day + "/" + month + " " + hour + ":" + minute + ":" + second;
        Line line = new Line(eventString + " " + getString(R.string.lx), datTime);
        list.add(line);
        Toast.makeText(this, getString(R.string.save) + " " + line.getLux(), Toast.LENGTH_SHORT).show();
    }

    private final SensorEventListener LightSensorListener
            = new SensorEventListener(){

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            if(event.sensor.getType() == Sensor.TYPE_LIGHT){

                eventString = String.valueOf(Math.round(event.values[0]));
                currentVelue = (int) event.values[0];

                mTxtCurrentLux.setText(eventString);
                if (currentVelue > mMaxValue){
                    mMaxValue = currentVelue;
                    mTxtMax.setText(eventString);
                }
                if (currentVelue < mMinValue) {
                    mMinValue = currentVelue;
                    mTxtMin.setText(eventString);
                }

            }
        }

    };

    private void calculateAverage() {
        if(currentVelue != null){
            mCount++;
            if(mCount == 1){
                mAverageValue = currentVelue;
            } else {
                mAverageValue = (mAverageValue*(mCount-1)+currentVelue)/mCount;
            }

            mTxtAverage.setText(String.valueOf(Math.round(mAverageValue)));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mySensorManager.unregisterListener(LightSensorListener);
    }


    @Override
    protected void onStop() {
        super.onStop();
        list.clear();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mySensorManager.registerListener(LightSensorListener,
                LightSensor,
                SensorManager.SENSOR_DELAY_NORMAL);
    }


}