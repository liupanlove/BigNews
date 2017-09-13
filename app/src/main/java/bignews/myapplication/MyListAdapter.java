package bignews.myapplication;

/**
 * Created by guoye on 2017/9/13.
 */
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;
import java.util.Vector;

import bignews.myapplication.BaseActivity;
import bignews.myapplication.R;
import bignews.myapplication.db.Headline;

/**
 * Created by guoye on 2017/9/13.
 */

public class MyListAdapter extends ArrayAdapter<String> {
    LayoutInflater inflater;

    public MyListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List objects, LayoutInflater inflater) {
        super(context, resource, objects);
        this.inflater = inflater;
    }

    @Override
    public View getView(int i, View view_, ViewGroup viewGroup) {
        View view = null;
        view = inflater.inflate(R.layout.list_item, null);
        TextView tv = (TextView)view.findViewById(R.id.text1);
        tv.setText(getItem(i));
        if (BaseActivity.config_struct.class_use.get(i)) {
            tv.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimary));
            tv.setTextColor(Color.WHITE);
        } else {
            tv.setBackgroundColor(getContext().getResources().getColor(R.color.colorBackground));
            tv.setTextColor(getContext().getResources().getColor(R.color.colorText));
        }
        return view;
    }
}
