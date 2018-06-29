package me.nkkumawat.lostify;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import me.nkkumawat.lostify.Adapter.MessageAdapter;
import me.nkkumawat.lostify.Database.DbHelper;

public class History extends AppCompatActivity {
    private static DbHelper dbHelper ;
    private static Cursor cursor;
    private static ListView messages_lv;
    private static MessageAdapter messageAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        dbHelper = new DbHelper(this);
        messages_lv = (ListView)findViewById(R.id.history_list_view);
        if(dbHelper.getDatabaseSize("SMSRESPONSE") < 1) {
            dbHelper.insert("#num name" + "\n" + "#Loc" , "Mobile Number" , "Sent" , "SMSRESPONSE");
        }
        setTextToList(this);
    }
    public static void setTextToList(Context context) {
        cursor = dbHelper.getWholeData("SMSRESPONSE");
        messageAdapter = new MessageAdapter(context, cursor);
        messages_lv.setAdapter(messageAdapter);
        messages_lv.post(new Runnable() {
            @Override
            public void run() {
                messages_lv.setSelection(messageAdapter.getCount() - 1);
            }
        });
    }
}
