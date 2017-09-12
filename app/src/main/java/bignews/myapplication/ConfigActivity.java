package bignews.myapplication;

import android.app.UiModeManager;
import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;


import com.facebook.drawee.view.SimpleDraweeView;

import java.util.Vector;

import bignews.myapplication.BaseActivity;
import bignews.myapplication.R;

/**
 * Created by guoye on 2017/9/8.
 */

public class ConfigActivity extends BaseActivity {
    private ArrayAdapter<String> class_adapter;
    private Vector<String> class_show_data;
    private UiModeManager mUiModeManager = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.config);

        mUiModeManager = (UiModeManager) getSystemService(Context.UI_MODE_SERVICE);

        ImageButton check_picture_mode = (ImageButton)findViewById(R.id.check_picture_mode);
        if (BaseActivity.config_struct.picture_mode)
            check_picture_mode.setImageResource(R.drawable.yes_picture);
        else
            check_picture_mode.setImageResource(R.drawable.no_picture);
        check_picture_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BaseActivity.config_struct.picture_mode)
                    ((ImageButton)view).setImageResource(R.drawable.no_picture);
                else
                    ((ImageButton)view).setImageResource(R.drawable.yes_picture);
                BaseActivity.config_struct.picture_mode = !BaseActivity.config_struct.picture_mode;
                BaseActivity.config_struct.push_data();
            }
        });
        ImageButton check_day_mode = (ImageButton)findViewById(R.id.check_day_mode);
        check_day_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BaseActivity.config_struct.day_mode = !BaseActivity.config_struct.day_mode;
                if (!BaseActivity.config_struct.day_mode) {
                    //mUiModeManager.enableCarMode(0);
                    mUiModeManager.setNightMode(UiModeManager.MODE_NIGHT_YES);
                }
                else{
                    //mUiModeManager.disableCarMode(0);
                    mUiModeManager.setNightMode(UiModeManager.MODE_NIGHT_NO);
                }
                BaseActivity.config_struct.push_data();
            }
        });
//vvvvvvvvvvv class vvvvvvvvvvvvvvvv
        class_show_data = (Vector<String>) BaseActivity.config_struct.class_data.clone();
        for (int i = 0; i < 12; ++i)
            if (BaseActivity.config_struct.class_use.get(i))
                class_show_data.set(i, BaseActivity.config_struct.class_data.get(i) + "   o");
        final ListView class_view = (ListView)findViewById(R.id.class_list);
        class_adapter = new ArrayAdapter<String>(this, R.layout.list_item, class_show_data);
        class_view.setAdapter(class_adapter);
        class_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (BaseActivity.config_struct.class_use.get(i)) {
                    BaseActivity.config_struct.class_use.set(i, false);
                    class_show_data.set(i, BaseActivity.config_struct.class_data.get(i));
                } else {
                    BaseActivity.config_struct.class_use.set(i, true);
                    class_show_data.set(i, BaseActivity.config_struct.class_data.get(i) + "   o");
                }
                BaseActivity.config_struct.refresh_tag_list();
                BaseActivity.config_struct.class_changed = true;
                class_adapter.notifyDataSetChanged();
                BaseActivity.config_struct.push_data();
            }
        });
    }
}
