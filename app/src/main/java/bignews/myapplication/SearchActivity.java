package bignews.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.SearchView;

public class SearchActivity extends Activity
{
    private SearchView searchView;
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        Intent intent = getIntent();
        String query = intent.getStringExtra("querycontent");
        searchView = (SearchView) findViewById(R.id.searchview1);
        searchView.setQuery(query, true);
        searchView.setIconifiedByDefault(true);
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.requestFocusFromTouch();
        /*searchView1 = (SearchView) findViewById(R.id.searchview1);
        searchView1.setFocusable(true);
        searchView1.setFocusableInTouchMode(true);
        searchView1.requestFocus();
        searchView1.requestFocusFromTouch();*/
    }
}