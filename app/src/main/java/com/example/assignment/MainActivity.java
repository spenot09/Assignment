package com.example.assignment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button start_service_button;
    private Button stop_service_button;
    private Button bind_service_button;

    private TextView sensor_txt_view;

    private SensorService nService;
    private boolean bound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        start_service_button = findViewById(R.id.start_service_button);
        stop_service_button = findViewById(R.id.stop_service_button);
        bind_service_button = findViewById(R.id.bind_service_button);
        sensor_txt_view = findViewById(R.id.sensor_txt_view);

        start_service_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startService(new Intent(getBaseContext(), SensorService.class));
            }
        });

        stop_service_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopService(new Intent(getBaseContext(), SensorService.class));
            }
        });

        bind_service_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MainActivity.this, SensorService.class);
                bindService(intent, connection, Context.BIND_AUTO_CREATE);
            }
        });
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            SensorService.SensorBinder binder = (SensorService.SensorBinder) iBinder;
            nService = binder.getService();
            bound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            bound = false;
        }

    };
    //sensor_txt_view.setText(Float.toString(sensorEvent.values[0]));
}
