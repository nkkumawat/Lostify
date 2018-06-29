package me.nkkumawat.lostify.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import me.nkkumawat.lostify.Messages;
import me.nkkumawat.lostify.R;

/**
 * Created by sonu on 28/6/18.
 */

public class MessageAdapter extends CursorAdapter {

    Context mContext;
    public static Cursor cursor;


    public MessageAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
        this.cursor = cursor;
        this.mContext=context;
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.history_list, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView message_tv = (TextView) view.findViewById(R.id.message_tv);
        TextView number_tv = (TextView) view.findViewById(R.id.number_tv);
        CardView cardView = (CardView) view.findViewById(R.id.history_card_view);
        if(cursor.getString(3).equals("Sent")) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_END);

            cardView.setLayoutParams(params);

        }
        message_tv.setText(cursor.getString(1));
        number_tv.setText(cursor.getString(2));

    }
    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public void changeCursor(Cursor cursor) {
        super.changeCursor(cursor);
    }
}