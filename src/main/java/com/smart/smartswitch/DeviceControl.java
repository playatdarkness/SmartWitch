package com.smart.smartswitch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class DeviceControl extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    AsyncHttpClient deviceClient;
    Switch deviceSwitch;
    TextView deviceName;
    ImageView statusLight;
    TextView switchStatus;
    String deviceIP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_control);

        deviceIP = getIntent().getExtras().getString("ip");
        Log.d("ip_add",deviceIP);
        deviceName = findViewById(R.id.device_name);
        switchStatus = findViewById(R.id.switch_status);
        statusLight = findViewById(R.id.status_light);

        deviceName.setText(getIntent().getExtras().getString("name"));

        deviceSwitch = findViewById(R.id.device_switch);
        deviceSwitch.setOnCheckedChangeListener(this);

        deviceClient = new AsyncHttpClient();

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        if(isChecked)   {
            deviceClient.get("http://"+deviceIP+"/4/on", null, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                    switchStatus.setText("ON");
                    statusLight.setImageResource(R.drawable.ic_switch_on);
                }

                @Override
                public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                    switchStatus.setText("Offline");
                    statusLight.setImageResource(R.drawable.ic_switch_off);
                    Toast.makeText(DeviceControl.this, "Device Offline!", Toast.LENGTH_SHORT).show();
                }

            });
        }

        else    {
            deviceClient.get("http://"+deviceIP+"/4/off", null, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    switchStatus.setText("OFF");
                    statusLight.setImageResource(R.drawable.ic_switch_off);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    switchStatus.setText("Offline");
                    statusLight.setImageResource(R.drawable.ic_switch_off);
                    Toast.makeText(DeviceControl.this, "Device Offline!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
