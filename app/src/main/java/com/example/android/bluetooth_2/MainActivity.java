package com.example.android.bluetooth_2;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_ENABLE_BT =1 ;
    ListView lv1,lv2;
    Set<BluetoothDevice> pairedDevices;
ArrayList<String>ar1,ar2;
BluetoothAdapter mBluetoothAdapter;
ArrayAdapter<String>ad1,ad2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv1=(ListView)findViewById(R.id.lst1);
        lv2=(ListView)findViewById(R.id.lst2);
        ar1=new ArrayList<String>();
        ar2=new ArrayList<String >();
        ad1=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,ar1);
        ad2=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,ar2);
        lv1.setAdapter(ad1);
        lv2.setAdapter(ad2);
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBluetoothAdapter==null){
            Toast.makeText(this,"Device doesn't supprot Bluetooth",Toast.LENGTH_SHORT);
        }
        else if(!mBluetoothAdapter.isEnabled()){
            Intent enableBtIntent=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            // There are paired devices. Get the name and address of each paired device.
            for (BluetoothDevice device : pairedDevices) {
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress();
                ar1.add(deviceName+"\n"+deviceHardwareAddress);
                ad1.notifyDataSetChanged();// MAC address
            }

        }
        if (mBluetoothAdapter.isDiscovering()){
            mBluetoothAdapter.cancelDiscovery();
        }
        int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
        mBluetoothAdapter.startDiscovery();
    }
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress();// MAC address
                ar2.add(deviceName+"\n"+deviceHardwareAddress);
                ad2.notifyDataSetChanged();
            }
            else if(BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)){
                if(mBluetoothAdapter.getState()==mBluetoothAdapter.STATE_OFF){
                    Toast.makeText(getApplicationContext(),"disabled",Toast.LENGTH_SHORT);
                }
            }
        }
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Don't forget to unregister the ACTION_FOUND receiver.
        unregisterReceiver(mReceiver);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_CANCELED){
            Toast.makeText(this,"Turn on bluetooth to continue further",Toast.LENGTH_SHORT);
        }
    }
}

