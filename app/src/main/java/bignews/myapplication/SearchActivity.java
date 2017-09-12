package bignews.myapplication;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Vector;

public class SearchActivity extends Activity implements HeadlinesFragment.OnHeadlineSelectedListener
{
    private static final String TAG = "SearchActivity"; // ???有什么用
    private Vector<Fragment> fragments;
    private SearchView searchView;
    private String queryContent;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

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
        searchView = (SearchView) findViewById(R.id.searchview1);
        getMessage();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                showContent(s);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        /*searchView1 = (SearchView) findViewById(R.id.searchview1);
        searchView1.setFocusable(true);
        searchView1.setFocusableInTouchMode(true);
        searchView1.requestFocus();
        searchView1.requestFocusFromTouch();*/
    }

    private void getMessage()
    {
        Intent intent = getIntent();
        String query = intent.getStringExtra("querycontent");
        searchView.setQuery(query, true);
        searchView.setIconifiedByDefault(true);
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.requestFocusFromTouch();
        showContent(query);
    }

    private View line()
    {
        ImageView image = new ImageView(this);
        image.setImageResource(R.drawable.home);
        return image;
    }

    @Override
    public void onArticleSelected(int position)
    {
        Intent intent = new Intent(this, ArticleFragment.class);
        intent.putExtra(ArticleFragment.ARG_POSITION, position - 1);
        startActivity(intent);
    }
    private void showContent(String query)
    {

    }
}