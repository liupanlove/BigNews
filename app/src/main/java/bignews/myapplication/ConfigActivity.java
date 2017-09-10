package bignews.myapplication;

import android.media.Image;
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

import java.util.Vector;

import bignews.myapplication.BaseActivity;
import bignews.myapplication.R;

/**
 * Created by guoye on 2017/9/8.
 */

public class ConfigActivity extends BaseActivity {
    private ArrayAdapter<String> class_adapter;
    private ArrayAdapter<String> shield_adapter;
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
//vvvvvvvvvvv class vvvvvvvvvvvvvvvv
        ImageButton class_trash = (ImageButton)findViewById(R.id.class_trash);
        class_trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BaseActivity.config_struct.class_list_deleting)
                    ((ImageButton)view).setImageResource(R.drawable.trash);
                else
                    ((ImageButton)view).setImageResource(R.drawable.trash_red);
                BaseActivity.config_struct.class_list_deleting = !BaseActivity.config_struct.class_list_deleting;
            }
        });
        final ListView class_view = (ListView)findViewById(R.id.class_list);
        class_adapter = new ArrayAdapter<String>(this, R.layout.list_item, BaseActivity.config_struct.class_data);
        class_view.setAdapter(class_adapter);
        class_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String result = adapterView.getItemAtPosition(i).toString();
                if (BaseActivity.config_struct.class_list_deleting) {
                    BaseActivity.config_struct.class_data.remove(i);
                    class_adapter.notifyDataSetChanged();
                    Toast.makeText(ConfigActivity.this, result + " deleted", Toast.LENGTH_SHORT).show();
                }
                BaseActivity.config_struct.class_changed = true;
            }
        });
        Button class_add = (Button)findViewById(R.id.class_add);
        class_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = ((EditText)findViewById(R.id.class_content)).getText().toString();
                BaseActivity.config_struct.class_data.add(text);
                class_adapter.notifyDataSetChanged();
                ((EditText)findViewById(R.id.class_content)).setText("");
                BaseActivity.config_struct.class_changed = true;
            }
        });
//vvvvvvvvvvv shield vvvvvvvvvvvvvv
        ImageButton shield_trash = (ImageButton)findViewById(R.id.shield_trash);
        shield_trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BaseActivity.config_struct.shield_list_deleting)
                    ((ImageButton)view).setImageResource(R.drawable.trash);
                else
                    ((ImageButton)view).setImageResource(R.drawable.trash_red);
                BaseActivity.config_struct.shield_list_deleting = !BaseActivity.config_struct.shield_list_deleting;
            }
        });
        final ListView shield_view = (ListView)findViewById(R.id.shield_list);
        shield_adapter = new ArrayAdapter<String>(this, R.layout.list_item, BaseActivity.config_struct.shield_data);
        shield_view.setAdapter(shield_adapter);
        shield_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String result = adapterView.getItemAtPosition(i).toString();
                if (BaseActivity.config_struct.shield_list_deleting) {
                    BaseActivity.config_struct.shield_data.remove(i);
                    shield_adapter.notifyDataSetChanged();
                    Toast.makeText(ConfigActivity.this, result + " deleted", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Button shield_add = (Button)findViewById(R.id.shield_add);
        shield_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = ((EditText)findViewById(R.id.shield_content)).getText().toString();
                BaseActivity.config_struct.shield_data.add(text);
                shield_adapter.notifyDataSetChanged();
                ((EditText)findViewById(R.id.shield_content)).setText("");
            }
        });

    }
    static public Vector<String> getClasses() {
        Vector<String> ans = new Vector<String>();
        ans.add("1");
        ans.add("2");
        ans.add("3");
        ans.add("4");
        ans.add("5");
        ans.add("6");
        ans.add("7");
        ans.add("8");
        return ans;
    }
    static public Vector<String> getShields() {
        Vector<String> ans = new Vector<String>();
        ans.add("123");
        ans.add("124");
        ans.add("125");
        ans.add("123");
        ans.add("124");
        ans.add("125");
        ans.add("123");
        ans.add("124");
        ans.add("125");
        ans.add("123");
        ans.add("124");
        ans.add("125");
        ans.add("123");
        ans.add("124");
        ans.add("125");
        return ans;
    }
}
