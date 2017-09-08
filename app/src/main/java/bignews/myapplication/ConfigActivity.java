package bignews.myapplication;

import android.os.Bundle;
import android.util.Log;

import bignews.myapplication.BaseActivity;
import bignews.myapplication.R;

/**
 * Created by guoye on 2017/9/8.
 */

public class ConfigActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("Err", "233");
        setContentView(R.layout.config);
    }
}
