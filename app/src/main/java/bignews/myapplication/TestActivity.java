package bignews.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestActivity extends Activity
{
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        ListView listView = (ListView) findViewById(R.id.listView);
        //listView.addHeaderView(line());
        List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();

        for(int i = 0; i < 10; ++i)
        {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", R.drawable.home);
            map.put("title", "hello");
            listItems.add(map);
        }

        SimpleAdapter adapter = new SimpleAdapter(this, listItems, R.layout.search_item,
                new String[]{"title", "image"}, new int[]{R.id.title, R.id.image});
        listView.setAdapter(adapter);
        /*searchView1 = (SearchView) findViewById(R.id.searchview1);
        searchView1.setFocusable(true);
        searchView1.setFocusableInTouchMode(true);
        searchView1.requestFocus();
        searchView1.requestFocusFromTouch();*/
    }
}