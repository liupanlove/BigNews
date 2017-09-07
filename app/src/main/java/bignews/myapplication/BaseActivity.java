package bignews.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
/**
 * Created by guoye on 2017/9/7.
 */

public class BaseActivity extends Activity {
    private LinearLayout contentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContentView(R.layout.base_layout);
    }
    private void initContentView(int layoutResID) {
        super.setContentView(layoutResID);
        contentLayout = (LinearLayout) findViewById(R.id.content_layout);
    }

    @Override
    public void setContentView(View view) {
        contentLayout.addView(view);
    }
    @Override
    public void setContentView(int layoutResID) {
        View sonLayout = LayoutInflater.from(this).inflate(layoutResID, null);
        sonLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f));
        contentLayout.addView(sonLayout);
    }
}
