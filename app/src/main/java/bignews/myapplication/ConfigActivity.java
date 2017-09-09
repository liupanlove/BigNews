package bignews.myapplication;

import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import bignews.myapplication.BaseActivity;
import bignews.myapplication.R;

/**
 * Created by guoye on 2017/9/8.
 */

public class ConfigActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.config);
        ImageButton check_picture_mode = (ImageButton)findViewById(R.id.check_picture_mode);
        check_picture_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BaseActivity.config_struct.picture_mode)
                    ((ImageButton)view).setImageResource(R.drawable.no_picture);
                else
                    ((ImageButton)view).setImageResource(R.drawable.yes_picture);
                BaseActivity.config_struct.picture_mode = !BaseActivity.config_struct.picture_mode;
            }
        });
        ImageButton check_day_mode = (ImageButton)findViewById(R.id.check_day_mode);
        check_day_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (BaseActivity.config_struct.day_mode)
                    ((ImageButton)view).setImageResource(R.drawable.night);
                else
                    ((ImageButton)view).setImageResource(R.drawable.day);
                BaseActivity.config_struct.day_mode = !BaseActivity.config_struct.day_mode;
            }
        });

    }
}
