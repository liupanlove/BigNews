package bignews.myapplication;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

/**
 * Created by guoye on 2017/9/7.
 */

class ConfigStruct {
    public boolean picture_mode;
    public boolean day_mode;
    public ConfigStruct() {
        picture_mode = true;
        day_mode = true;
    }
}

public class BaseActivity extends AppCompatActivity {
    private LinearLayout contentLayout;

    private SearchView searchView;
    private Intent config_intent;
    final static public ConfigStruct config_struct = new ConfigStruct();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContentView(R.layout.base_layout);
        
	ImageButton config_button = (ImageButton)findViewById(R.id.config_botton);
        config_intent = new Intent(this, ConfigActivity.class);
        config_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(config_intent);
            }
        });


        searchView = (SearchView) findViewById(R.id.searchview);
        searchView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void  onClick(View view)
            {
                openSearchActivity();
            }
        });
    }

    private void openSearchActivity()
    {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
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
