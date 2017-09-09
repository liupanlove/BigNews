package bignews.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.widget.SearchView;

public class SearchActivity extends Activity
{
    private SearchView searchView1;
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        searchView1 = (SearchView) findViewById(R.id.searchview1);
        searchView1.setFocusable(true);
        searchView1.setFocusableInTouchMode(true);
        searchView1.requestFocus();
        searchView1.requestFocusFromTouch();
    }
}
