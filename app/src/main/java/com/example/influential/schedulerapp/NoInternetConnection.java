package com.example.influential.schedulerapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.influential.schedulerapp.ExtraJavaClasses.NetworkUtil;

public class NoInternetConnection extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_util);
    }

    public void callForFinish(View view)
    {
        int status = NetworkUtil.getConnectivityStatusString(this);

        if (status != NetworkUtil.NETWORK_STATUS_NOT_CONNECTED) {

            Intent intent=new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();

        }

    }
}
