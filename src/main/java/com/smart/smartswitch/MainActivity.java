package com.smart.smartswitch;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Intent addDeviceIntent;
    private DrawerLayout drawer;
    private RecyclerView recyclerView;
    private AdapterForView adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<DeviceItem> deviceItem;

    SQLiteDatabase db;
    SQLiteOpenHelper openHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        openHelper = new DeviceList(this);

        createDeviceList();
        buildRecyclerView();

        drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.addDevice:
                addDeviceIntent = new Intent(this, AddDevice.class);
                startActivityForResult(addDeviceIntent, 999);
                break;
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_wifi:
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                break;

            case R.id.nav_rate:
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.smart.smartswitch")));
                } catch (android.content.ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.smart.smartswitch")));
                }
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 999 && resultCode == RESULT_OK && data != null) {
            deviceItem.add(new DeviceItem(R.drawable.ic_devices_other, data.getStringExtra("device_name")));
        }
    }

    public void createDeviceList() {
        deviceItem = new ArrayList<>();
        deviceDBRetrieve();
    }

    public void removeItem(int position) {
        String device_name = deviceItem.get(position).getDeviceName();
        db = openHelper.getWritableDatabase();
        db.delete(DeviceList.TABLE_NAME,"DeviceName=?", new String[] {device_name});
        deviceItem.remove(position);
        adapter.notifyItemRemoved(position);
    }

    public void buildRecyclerView() {
        recyclerView = findViewById(R.id.devicelistView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        adapter = new AdapterForView(deviceItem);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new AdapterForView.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String device_name = deviceItem.get(position).getDeviceName();
                db = openHelper.getReadableDatabase();
                Cursor result = db.rawQuery("SELECT DeviceName,IPAddress FROM deviceregister WHERE DeviceName=?",new String[] {device_name});
                result.moveToFirst();
                String ip = result.getString(1);

                Intent deviceControlActivity = new Intent(MainActivity.this, DeviceControl.class);

                deviceControlActivity.putExtra("name",device_name);
                deviceControlActivity.putExtra("ip",ip);

                startActivity(deviceControlActivity);
            }

            @Override
            public void onDeleteClick(int position) {
                deviceItem.get(position);
                removeItem(position);
            }
        });
    }

    public void deviceDBRetrieve() {
        db = openHelper.getReadableDatabase();
        Cursor result = db.rawQuery("SELECT DeviceName FROM deviceregister",null);
        if (result != null) {
            result.moveToFirst();
            while (result.moveToNext()) {
                String device = result.getString(0);
                deviceItem.add(new DeviceItem(R.drawable.ic_devices_other, device));
            }
        }
    }
}

