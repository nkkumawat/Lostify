package me.nkkumawat.lostify;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import me.nkkumawat.lostify.Adapter.MessageAdapter;
import me.nkkumawat.lostify.Database.DbHelper;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.RECEIVE_SMS;
import static android.Manifest.permission.READ_SMS;
import static android.Manifest.permission.SEND_SMS;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.ACCESS_COARSE_LOCATION;


public class MainActivity extends AppCompatActivity {

    private Button historyBtn , queryBtn;
    private static final int PERMISSION_REQUEST_CODE = 10;
    private static DbHelper dbHelper;
    private static Cursor cursor;
    private static ListView querymessages_lv;
    public static MessageAdapter messageAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermission();
        dbHelper = new DbHelper(this);
        querymessages_lv = (ListView) findViewById(R.id.query_list_view);


        historyBtn = (Button)findViewById(R.id.history_bt);
        queryBtn = (Button) findViewById(R.id.query_btn);
        historyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this , History.class));
            }
        });
        queryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogForQuery();
            }
        });
        if(dbHelper.getDatabaseSize("SMSQUERY") < 1) {
            dbHelper.insert("#num name   for the get the contact from phone" + "\n" + "#Loc  for find the location of device \n Device camera capture image and send me yet to be impliment" , "Mobile Number" , "Sent" , "SMSQUERY");
        }
        setTextToList(this);
    }

    public static void setTextToList(Context context) {
        cursor = dbHelper.getWholeData("SMSQUERY");
        if(cursor != null) {
            messageAdapter = new MessageAdapter(context, cursor);
            querymessages_lv.setAdapter(messageAdapter);
            querymessages_lv.post(new Runnable() {
                @Override
                public void run() {
                    querymessages_lv.setSelection(messageAdapter.getCount() - 1);
                }
            });
        }
    }

    public void showDialogForQuery(){
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.query_alert_dialog_layout, null);
        final EditText mobileEt = alertLayout.findViewById(R.id.mobile_et);
        final EditText messageEt = alertLayout.findViewById(R.id.query_et);

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Query");
        alert.setView(alertLayout);
        alert.setCancelable(false);
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getBaseContext(), "Cancel clicked", Toast.LENGTH_SHORT).show();
            }
        });
        alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String mobile = mobileEt.getText().toString();
                String message = messageEt.getText().toString();
                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(mobile, null, message, null, null);
                    dbHelper.insert(message , mobile , "Sent" , "SMSQUERY");
                    messageAdapter = null;
                    setTextToList(MainActivity.this);
                }catch (Exception e) {
                    Log.d("SMS SEND ERROR" , e.toString());
                }
            }
        });
        AlertDialog dialog = alert.create();
        dialog.show();
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{ WRITE_EXTERNAL_STORAGE , READ_EXTERNAL_STORAGE , READ_CONTACTS , READ_SMS , RECEIVE_SMS , SEND_SMS , ACCESS_FINE_LOCATION , ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_CODE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean fir = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean sec = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean thi = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean fort = grantResults[4] == PackageManager.PERMISSION_GRANTED;
                    boolean fif = grantResults[5] == PackageManager.PERMISSION_GRANTED;
                    boolean six = grantResults[6] == PackageManager.PERMISSION_GRANTED;
                    boolean sev = grantResults[7] == PackageManager.PERMISSION_GRANTED;
                    if (fir && sec && thi && fort && fif && six && sev) {
                    }else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
                                showMessageOKCancel("You need to allow access to all the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{ WRITE_EXTERNAL_STORAGE , READ_EXTERNAL_STORAGE  , READ_CONTACTS , READ_SMS , RECEIVE_SMS , SEND_SMS ,ACCESS_FINE_LOCATION , ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            }
                        }

                    }
                }
                break;
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
