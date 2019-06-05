package com.example.incoming_outgoing_call_application;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.outgoingcallapplication.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = this.getClass().getSimpleName();
    private Button mCallButton;
    private final static int REQUEST_CODE_ASK_PERMISSIONS = 1;
    IncomingAndOutgoingCallReceiver mIncomingAndOutgoingCallReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "OnCreate");
        setContentView(R.layout.activity_main);
        mCallButton = findViewById(R.id.btn_call);
        mCallButton.setOnClickListener(this);
        mIncomingAndOutgoingCallReceiver = new IncomingAndOutgoingCallReceiver();
        registerReceiver();
    }

    private void registerReceiver() {
        Log.i(TAG, "registerReceiver()");
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.PHONE_STATE");
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        try {
            registerReceiver(mIncomingAndOutgoingCallReceiver, filter);
        } catch (IllegalArgumentException e) {

            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_call:
                makeCall();
                break;
        }
    }

    private void makeCall() {
        Intent phoneIntent = new Intent(Intent.ACTION_CALL);
        phoneIntent.setData(Uri.parse("tel:" + "+91-YOUR_PHONE_NUMBER"));
        try {
            if (checkCallPermission()) {
                startActivity(phoneIntent);
            }
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getApplicationContext(), R.string.call_failed, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkCallPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            String call = Manifest.permission.CALL_PHONE;
            String outgoing = Manifest.permission.PROCESS_OUTGOING_CALLS;
            String incoming = Manifest.permission.READ_PHONE_STATE;
            int hasCallPermission = checkSelfPermission(call);
            List<String> permissions = new ArrayList<String>();
            if (hasCallPermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(call);
                permissions.add(outgoing);
                permissions.add(incoming);
            }
            if (!permissions.isEmpty()) {
                String[] params = permissions.toArray(new String[permissions.size()]);
                requestPermissions(params, REQUEST_CODE_ASK_PERMISSIONS);
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy()");
        try {
            unregisterReceiver(mIncomingAndOutgoingCallReceiver);
        } catch (IllegalArgumentException e) {

            e.printStackTrace();
        }
    }
}
