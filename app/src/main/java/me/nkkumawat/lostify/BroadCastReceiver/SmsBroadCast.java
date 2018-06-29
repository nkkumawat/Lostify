package me.nkkumawat.lostify.BroadCastReceiver;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import me.nkkumawat.lostify.Database.DbHelper;
import me.nkkumawat.lostify.LocationFinder;
import me.nkkumawat.lostify.MainActivity;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by sonu on 28/6/18.
 */

public class SmsBroadCast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences.Editor editor = context.getSharedPreferences("Hello Nk", MODE_PRIVATE).edit();
        DbHelper dbHelper = new DbHelper(context);
        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null) {
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (int i = 0; i < pdusObj.length; i++) {
                    SmsMessage receivedMessageBody = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String receivedPhoneNumber = receivedMessageBody.getDisplayOriginatingAddress();
                    String receivedMessage = receivedMessageBody.getDisplayMessageBody();

                    String tokens[] = receivedMessage.split(" ") ;
                    if(tokens[0].equals("#num")) {
                        dbHelper.insert(receivedMessage , receivedPhoneNumber , "Received","SMSRESPONSE");
                        String sendingMessage = "#Response ";
                        Cursor cursorPhone = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER , ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME},
                                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " like ?" ,
                                new String[]{tokens[1]+"%"},
                                null);
                        if (cursorPhone.moveToFirst()) {
                            while(cursorPhone.moveToNext()) {
                                sendingMessage += cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)) + " : " +
                                                cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))+ "\n" ;
                            }
                            Log.d("NUMBERS", tokens[0] + "\n" + sendingMessage);
                        }else {
                            sendingMessage = "#Response NOT Found!!!";
                        }
                        try {
                            SmsManager smsManager = SmsManager.getDefault();
                            smsManager.sendTextMessage(receivedPhoneNumber, null, sendingMessage, null, null);
                            dbHelper.insert(sendingMessage , receivedPhoneNumber , "Sent", "SMSRESPONSE");
                        }catch (Exception e) {
                            Log.d("SMS SEND ERROR" , e.toString());
                        }
                        cursorPhone.close();
                    }else if(tokens[0].equals("#loc")) {
                        dbHelper.insert(receivedMessage , receivedPhoneNumber , "Sent" , "SMSRESPONSE");
                        LocationFinder locationFinder = new LocationFinder();
                        String sendingMessage = "#Response ";
                        sendingMessage += locationFinder.latitude + "   " + locationFinder.longitude;
                        try {
                            SmsManager smsManager = SmsManager.getDefault();
                            smsManager.sendTextMessage(receivedPhoneNumber, null, sendingMessage, null, null);
                            dbHelper.insert(sendingMessage , receivedPhoneNumber , "Sent", "SMSRESPONSE");
                        }catch (Exception e) {
                            Log.d("SMS SEND ERROR" , e.toString());
                        }

                    }else if(tokens[0].equals("#Response")) {

                        dbHelper.insert(receivedMessage , receivedPhoneNumber , "Received" , "SMSQUERY");
                        Cursor c = dbHelper.getWholeData("SMSQUERY");
                        MainActivity.messageAdapter = null;
                        MainActivity.setTextToList(context);

                    }else{
                        Log.d("NotForMeException" , "This Message is not for me");
                    }
                }
            }
        } catch (Exception e) {
            Log.e("SMSNOTRECEIVEDEXCEPTION", "Exception smsReceiver" + e.toString());
        }
    }

}
