package bignews.myapplication;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.app.UiModeManager;
import android.arch.persistence.room.Dao;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
///import android.widget.SearchView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Vector;

import bignews.myapplication.db.DAO;
import bignews.myapplication.db.Preferences;

/**
 * Created by guoye on 2017/9/7.
 */

class ConfigStruct {
    public boolean picture_mode;
    public boolean day_mode;
    public boolean class_changed;
    public boolean favorite_changed;
    public Vector<Integer> tag_id_list;
    public Vector<String> tag_list;
    public Vector<String> class_data;
    public Vector<Boolean> class_use;
    public ConfigStruct() {
        picture_mode = true;
        day_mode = true;
        class_changed = false;
        favorite_changed = false;
        class_data = new Vector<>();
        class_data.add("科技");
        class_data.add("教育");
        class_data.add("军事");
        class_data.add("国内");
        class_data.add("社会");
        class_data.add("文化");
        class_data.add("汽车");
        class_data.add("国际");
        class_data.add("体育");
        class_data.add("财经");
        class_data.add("健康");
        class_data.add("娱乐");
        class_use = new Vector<>();
        for (int i = 0; i < 12; ++i)
            class_use.add(true);
        tag_id_list = new Vector<>();
        tag_list = new Vector<>();
        refresh_tag_list();
    }

    public void refresh_tag_list() {
        tag_list.clear();
        tag_id_list.clear();
        tag_id_list.add(-3);
        tag_id_list.add(0);
        tag_id_list.add(-1);
        tag_list.add("历史");
        tag_list.add("推荐");
        tag_list.add("收藏");
        for (int i = 0; i < 12; ++i)
            if (class_use.get(i)) {
                tag_id_list.add(i + 1);
                tag_list.add(class_data.get(i));
            }
    }

    public void refresh_data() {
        DAO dao = DAO.getInstance();
        Preferences p = dao.getSettings();
        for (int i = 0; i < 12; ++i)
            if ((p.classTags & (1 << i)) > 0)
                class_use.set(i, true);
            else
                class_use.set(i, false);
        picture_mode = !p.onlyText;
        day_mode = !p.isNight;
        refresh_tag_list();
    }

    public void push_data() {
        DAO dao = DAO.getInstance();
        Preferences p = dao.getSettings();
        p.classTags = 0;
        for (int i = 0; i < 12; ++i)
            if (class_use.get(i))
                p.classTags += 1 << i;
        p.onlyText = !picture_mode;
        p.isNight = !day_mode;
        dao.setSettings(p);
    }
}

public class BaseActivity extends AppCompatActivity {
    private LinearLayout contentLayout;

    private SearchView searchView;
    private Toolbar toolBar;
    private Intent config_intent;
    final static public ConfigStruct config_struct = new ConfigStruct();
    private LinearLayout linearLayout;
    private UiModeManager mUiModeManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContentView(R.layout.base_layout);
        DAO.init(getApplicationContext());
        config_struct.refresh_data();
        linearLayout = (LinearLayout) findViewById(R.id.baselinearlayout);
	    ImageButton config_button = (ImageButton)findViewById(R.id.config_botton);
        config_intent = new Intent(this, ConfigActivity.class);
        config_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(config_intent);
            }
        });

        toolBar = (Toolbar) findViewById(R.id.toolbar);
        toolBar.setTitle("大新闻");
        toolBar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolBar);
        /*searchView = (SearchView) findViewById(R.id.searchview);
        searchView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void  onClick(View view)
            {
                openSearchActivity();
            }
        });*/
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        setSearchView(menu);
        /**
         * 关联检索配置和SearchView
         *
         * 实例化searchable，调用SearchView的setSearchAbleInfo（SearchAbleInfo）方法传递给SearchView
         * setSearchAbleInfo会根据XML文件产生一个 SearchableInfo对象，
         * 当在SearchView中进行搜索时，则会通过一个包含ACTION_SEARCH的intent来启动一个Activity
         */

        //MenuItem searchViewButton = (MenuItem) menu.findItem(R.id.search);
        //searchViewButton.setOnMenuItemClickListener()
        /*SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        SearchableInfo searchableInfo =
                searchManager.getSearchableInfo(
                        new ComponentName(getApplicationContext(), SearchActivity.class));
        searchView.setSearchableInfo(searchableInfo);*/
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            //输入完成后，点击回车或是完成键
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Toast.makeText(getApplicationContext(),query,Toast.LENGTH_SHORT).show();
                openSearchActivity(query);
                return true;
            }

            //查询文本框有变化时事件
            @Override
            public boolean onQueryTextChange(String newText) {

                Log.e("onQueryTextChange","搜索内容为：" + newText);
                return false;
            }
        });
        return true;
        //return super.onCreateOptionsMenu(menu);
    }
    private void setSearchView(Menu menu)
    {
        MenuItem item = menu.getItem(0);
        searchView = new SearchView(this);
        searchView.setIconifiedByDefault(false); // control the position of the search icon
        searchView.setQueryHint("请输入内容");
        searchView.setSubmitButtonEnabled(true);

        //设置搜索框文字颜色
        TextView textView = (TextView) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        textView.setHintTextColor(Color.GRAY);
        textView.setTextColor(Color.BLACK); /////////!!!!!!!!!!!!!!!!

        //设置关闭按钮
        /*ImageView closeButton = (ImageView)searchView
                .findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        closeButton.setImageDrawable(getDrawable(R.drawable.home));

        //设置跳转按钮
        ImageView goButton = (ImageView)searchView
                .findViewById(android.support.v7.appcompat.R.id.search_go_btn);
        goButton.setImageDrawable(getDrawable(R.drawable.home));

        //设置隐藏的ICON,在setIconifiedByDefault(false)时才能设置成功
        ImageView collapsedIcon = (ImageView)searchView
                .findViewById(android.support.v7.appcompat.R.id.search_mag_icon);
        collapsedIcon.setImageDrawable(getDrawable(R.drawable.home));*/
        item.setActionView(searchView);
    }
    private void openSearchActivity(String query)
    {
        Intent intent = new Intent(this, SearchActivity.class);
        intent.putExtra("querycontent", query);
        linearLayout.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

        startActivity(intent);
    }

}
