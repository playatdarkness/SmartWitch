package com.smart.smartswitch;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddDevice extends AppCompatActivity implements View.OnClickListener {

    EditText name;
    EditText ip;
    SQLiteOpenHelper openHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);

        final Button saveDevice = findViewById(R.id.savedevice);

        name = findViewById(R.id.devicename);
        ip = findViewById(R.id.deviceip);

        saveDevice.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        openHelper = new DeviceList(this);

        if (name.getText().toString().length() == 0)    {
            name.setError("Name required");
        }
        else if (ip.getText().toString().length() == 0) {
            ip.setError("IP Address required");
        }
        else if (isValidIp())  {
            ip.setError("Enter valid IP Address");
        }

        else {
            db = openHelper.getWritableDatabase();
            String namedata = name.getText().toString();
            String ipdata = ip.getText().toString();
            Intent returnvalue = new Intent(this, MainActivity.class);
            db.insert(DeviceList.TABLE_NAME,null,insertData(namedata,ipdata));
            returnvalue.putExtra("device_name",namedata);
            setResult(RESULT_OK, returnvalue);
            Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public boolean isValidIp()  {
        String[] num = ip.getText().toString().split("//.");
        if(num.length != 4) {
            return false;
        }

        for(String n : num) {
            if(isNumeric(n)) {
                int val = Integer.parseInt(n);
                if(val > 255 || val < 0) {
                    return false;
                }
            }
            else {
                return false;
            }
        }
        return true;
    }

    public boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public ContentValues insertData(String namedata, String ipdata)    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DeviceList.NAME, namedata);
        contentValues.put(DeviceList.IP, ipdata);
        return(contentValues);
    }


}
