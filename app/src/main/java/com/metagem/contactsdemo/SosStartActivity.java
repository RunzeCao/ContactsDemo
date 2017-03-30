package com.metagem.contactsdemo;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

public class SosStartActivity extends CheckPermissionsActivity {


    private static final int REQUEST_CODE_SEND_SMS_PERMISSIONS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos_start);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_sos_start);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        findViewById(R.id.sos_start_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hasReadContactsPermission();
            }


        });
    }
    private void hasReadContactsPermission() {
        if (ContextCompat.checkSelfPermission(SosStartActivity.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(SosStartActivity.this, Manifest.permission.SEND_SMS)) {
                //当一个权限第一次被请求和用户标记过不再提醒的时候,我们写的对话框被展示。
                //最后一种情况，onRequestPermissionsResult 会收到PERMISSION_DENIED ，系统询问对话框不展示。
                showMessageOKCancel("You need to allow access to Contacts", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(SosStartActivity.this, new String[]{Manifest.permission.SEND_SMS}, REQUEST_CODE_SEND_SMS_PERMISSIONS);
                    }
                });
            } else {
                ActivityCompat.requestPermissions(SosStartActivity.this, new String[]{Manifest.permission.SEND_SMS}, REQUEST_CODE_SEND_SMS_PERMISSIONS);
            }
        } else {
            //SmsManager.getDefault().sendTextMessage("18676411162",null,"123456",null,null);
            startActivity(new Intent(this, MainActivity.class));
        }
    }
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(SosStartActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_SEND_SMS_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivity(new Intent(this, MainActivity.class));
                } else {
                    Toast.makeText(SosStartActivity.this, "SEND_SMS Denied", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
