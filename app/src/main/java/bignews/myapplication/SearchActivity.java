package bignews.myapplication;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Vector;

import bignews.myapplication.db.DAO;

public class SearchActivity extends AppCompatActivity implements HeadlinesFragment.OnHeadlineSelectedListener
{
    private static final String TAG = "SearchActivity"; // ???有什么用
    private SearchView searchView;
    private  SimpleAdapter adapter;
    private String queryContent;
    private final int id = -2;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        //DAO.init(getApplicationContext());
        /*ListView listView = (ListView) findViewById(R.id.listView);
        //listView.addHeaderView(line());
        List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();

        for(int i = 0; i < 10; ++i)
        {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", R.drawable.home);
            map.put("title", "hello");
            listItems.add(map);
        }

        adapter = new SimpleAdapter(this, listItems, R.layout.search_item,
                new String[]{"title", "image"}, new int[]{R.id.title, R.id.image});
        listView.setAdapter(adapter);*/

        searchView = (SearchView) findViewById(R.id.searchview1);
        getMessage();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                showContent(s);
                Log.i(TAG, "onQueryTextSubmit" + s );
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
    public void onArticleSelected(String id)
    {
        Intent intent = new Intent(this, ArticleFragment.class);
        intent.putExtra(ArticleFragment.ARG_POSITION, id);
        startActivity(intent);
    }
    private void showContent(String query)
    {
        //getSupportFragmentManager().findFragmentById("f1").
        Log.i(TAG, "queryContent" + query);

        Fragment fragment = new HeadlinesFragment();
        Bundle bundle = new Bundle();
        bundle.putString("text", query);
        bundle.putInt("id", id);
        fragment.setArguments(bundle);

        //Fragment.getSupportFragmentManager().b
        //FragmentManager fragmentManager = getFragmentManager();
        //getSupportFragmentManager().beginTransaction().add(R.id.searchlinearlayout, fragment, "f1").commit();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.searchlinearlayout, fragment);
        fragmentTransaction.commit();
        //LayoutInflater inflater = LayoutInflater.from(this);
        //View view  = inflater.inflate(R.layout.fragment_layout, (LinearLayout)findViewById(R.id.searchlinearlayout));
    }
}