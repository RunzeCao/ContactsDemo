package com.metagem.contactsdemo;

import android.Manifest;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_CODE_READ_CONTACTS_PERMISSIONS = 1;
    private Uri contactData;
    private List<MGemContact> mGemContacts;
    private MGemContactAdapter contactAdapter;
    private FloatingActionButton fab;
    private RadioButton radioButton1, radioButton2;
    private boolean isClose = true;
    private String messageContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }


    private void initView() {
        findViewById(R.id.sos_add_iv).setOnClickListener(this);
        findViewById(R.id.sos_help_iv).setOnClickListener(this);
        findViewById(R.id.sos_back_iv).setOnClickListener(this);
        fab = (FloatingActionButton) findViewById(R.id.sos_open_fab);
        fab.setOnClickListener(this);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.sos_recycle);
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.sos_rg);
        radioGroup.setOnCheckedChangeListener(this);
        radioButton1 = (RadioButton) findViewById(R.id.sos_rb_1);
        radioButton1.setChecked(true);
        radioButton2 = (RadioButton) findViewById(R.id.sos_rb_2);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        if (Utils.isEmpty(PrefUtils.getItemBean(this))) {
            mGemContacts = new ArrayList<>();
        } else {
            mGemContacts = (List<MGemContact>) PrefUtils.str2Object(PrefUtils.getItemBean(this));
        }

        contactAdapter = new MGemContactAdapter(this, mGemContacts);
        contactAdapter.setOnDeleteClickListener(new MGemContactAdapter.OnDeleteClickListener(){

            @Override
            public void onDeleteClick(View view, final int position) {
                new AlertDialog.Builder(MainActivity.this).setMessage("确定要删除当前联系人吗？").setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        contactAdapter.removeData(position);
                    }
                }).setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create().show();
            }
        });
        recyclerView.setAdapter(contactAdapter);
    }

    private void initData() {
        AMapLocationClient mLocationClient = new AMapLocationClient(this.getApplicationContext());
        AMapLocation location = mLocationClient.getLastKnownLocation();
        if (location != null && location.getErrorCode() == 0) {
            String longitude = String.valueOf(location.getLongitude());
            String latitude = String.valueOf(location.getLatitude());
            String address = location.getAddress();
            String url = getResources().getString(R.string.sos) + longitude + "&lat=" + latitude;
            radioButton1.setText(String.format("%s%s", String.format(getResources().getString(R.string.sosMessage1), address), url));
            radioButton2.setText(String.format("%s%s", String.format(getResources().getString(R.string.sosMessage2), address), url));
            if (radioButton1.isChecked()){
                messageContent = radioButton1.getText().toString();
            }else if (radioButton2.isChecked()){
                messageContent = radioButton2.getText().toString();
            }
        } else {
            if (location != null) {
                Log.e(TAG, "定位失败" + "\n" + "错误码:" + location.getErrorCode()
                        + "\n" + "错误信息:" + location.getErrorInfo()
                        + "\n" + "错误描述:" + location.getLocationDetail());
            } else {
                Log.e(TAG, "location = null");
            }

        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_READ_CONTACTS_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                    MainActivity.this.startActivityForResult(intent, 1);
                } else {
                    Toast.makeText(MainActivity.this, "READ_CONTACTS Denied", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_OK:
                switch (requestCode) {
                    case 1:
                        if (data != null) {
                            contactData = data.getData();
                            getLoaderManager().restartLoader(0, null, this);
                        }
                        break;
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sos_back_iv:
                finish();
                break;
            case R.id.sos_help_iv:
                break;
            case R.id.sos_add_iv:
                hasReadContactsPermission();
                break;
            case R.id.sos_open_fab:
                if (isClose) {
                    isClose = false;
                    fab.setSelected(true);
                    SmsManager smsManager = SmsManager.getDefault();
                    for (MGemContact contact : mGemContacts) {
                        if(messageContent.length() > 70){
                            ArrayList<String> phoneList = smsManager.divideMessage(messageContent);
                            smsManager.sendMultipartTextMessage(contact.getNumber(), null, phoneList, null, null);
                        }else {
                            smsManager.sendTextMessage(contact.getNumber(), null, messageContent, null, null);
                        }
                    }

                } else {
                    isClose = true;
                    fab.setSelected(false);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId) {
            case R.id.sos_rb_1:
                messageContent = radioButton1.getText().toString();
                break;
            case R.id.sos_rb_2:
                messageContent = radioButton2.getText().toString();
                break;
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, contactData, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        String phoneNumber = "";

        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            String hasPhone = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
            String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            if (hasPhone.equalsIgnoreCase("1")) {
                hasPhone = "true";
            } else {
                hasPhone = "false";
            }
            if (Boolean.parseBoolean(hasPhone)) {
                Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null);
                if (phones != null) {
                    while (phones.moveToNext()) {
                        phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    }
                    phones.close();
                }
            }

            Uri imageUri = Uri.withAppendedPath(contactData, ContactsContract.Contacts.Photo.DISPLAY_PHOTO);
            contactAdapter.addItem(new MGemContact(name, phoneNumber, imageUri.toString()));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


    public void hasReadContactsPermission() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_CONTACTS)) {
                //当一个权限第一次被请求或用户标记过不再提醒的时候,我们写的对话框被展示。
                //用户标记过不再提醒时，onRequestPermissionsResult 会收到PERMISSION_DENIED ，系统询问对话框不展示。
                showMessageOKCancel("You need to allow access to Contacts", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CODE_READ_CONTACTS_PERMISSIONS);
                    }
                });
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CODE_READ_CONTACTS_PERMISSIONS);
            }
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            MainActivity.this.startActivityForResult(intent, 1);
        }

    }


    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

}
