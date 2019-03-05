package com.example.assignment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.hardware.Sensor;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.se.omapi.Session;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button start_service_button, stop_service_button,bind_service_button, acc_button, light_button;
    private TextView sensor_txt_view, textStatus;

    Messenger mService = null;
    final Messenger mMessenger = new Messenger(new IncomingHandler());

    private SensorService nService;
    private boolean bound = false;

    static final int MSG_ACCELEROMETER = 1;
    static final int MSG_LIGHT = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        start_service_button = findViewById(R.id.start_service_button);
        stop_service_button = findViewById(R.id.stop_service_button);
        bind_service_button = findViewById(R.id.bind_service_button);
        acc_button = findViewById(R.id.acc_sensor_button);
        light_button = findViewById(R.id.light_sensor_button);



        sensor_txt_view = findViewById(R.id.sensor_txt_view);
        textStatus = findViewById(R.id.textStatus);

        start_service_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startService(new Intent(getBaseContext(), SensorService.class));
            }
        });

        stop_service_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doUnbindService();
                stopService(new Intent(getBaseContext(), SensorService.class));
            }
        });

        bind_service_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doBindService();
            }
        });

        acc_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessageToService(MSG_ACCELEROMETER);
            }
        });
        light_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessageToService(MSG_LIGHT);
            }
        });

    }

    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SensorService.MSG_ACCELEROMETER:
                    sensor_txt_view.setText("Accelerometer locked in: " + msg.arg1);
                    break;
                case SensorService.MSG_LIGHT:
                    sensor_txt_view.setText("Light Sensor locked in: " + msg.arg1);
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mService = new Messenger(service);
            textStatus.setText("Attached.");
            try {
                Message msg = Message.obtain(null, SensorService.MSG_REGISTER_CLIENT);
                msg.replyTo = mMessenger;
                mService.send(msg);
            }
            catch (RemoteException e) {
                // In this case the service has crashed before we could even do anything with it
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mService = null;
            textStatus.setText("Disconnected.");
            bound = false;
        }

    };

    private void sendMessageToService(int intvaluetosend) {
        if (bound) {
            if (mService != null) {
                try {
                    //need to check this if successful because may need to alter for use
                    Message msg = Message.obtain(null, SensorService.MSG_SENSOR, intvaluetosend, 0);
                    msg.replyTo = mMessenger;
                    mService.send(msg);
                }
                catch (RemoteException e) {
                }
            }
        }
    }

    void doBindService() {
        bindService(new Intent(this, SensorService.class), connection, Context.BIND_AUTO_CREATE);
        bound = true;
        textStatus.setText("Binding.");
    }

    void doUnbindService() {
        if (bound) {

            // Detach our existing connection.
            unbindService(connection);
            bound = false;
            textStatus.setText("Unbinding.");
        }
    }
    //sensor_txt_view.setText(Float.toString(sensorEvent.values[0]));
}
