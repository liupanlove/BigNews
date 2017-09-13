package bignews.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import bignews.myapplication.db.DAO;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

public class MainActivity extends BaseActivity
        implements HeadlinesFragment.OnHeadlineSelectedListener{

    private static final String TAG = "MainActivity";
    private Vector<Fragment> fragments;
    private TabFragmentAdapter tab_adapter;
    Intent config_intent;
    public void refreshTag() {
//        for (int i = fragments.size() - 1; i >= 0; --i) {
//            String tag = fragments.get(i).getArguments().getString("text");
//            boolean got = false;
//            for (int j = 0; j < BaseActivity.config_struct.tag_list.size(); ++j)
//                if (BaseActivity.config_struct.tag_list.get(j).equals(tag)) {
//                    got = true;
//                    break;
//                }
//            if (!got) {
//                Log.v("Err", tag);
//                fragments.remove(i);
//            }
//        }
        fragments.clear();
        for (int i = 0; i < BaseActivity.config_struct.tag_list.size(); ++i) {
            String tag = BaseActivity.config_struct.tag_list.get(i);
            boolean got = false;
            for (int j = 0; j < fragments.size(); ++j)
                if (tag.equals(fragments.get(j).getArguments().getString("text"))) {
                    got = true;
                    break;
                }
            if (!got) {
                Log.v("Err", tag);
                Fragment fragment = new HeadlinesFragment();
                Bundle bundle = new Bundle();
                bundle.putString("text",tag);
                bundle.putInt("id",BaseActivity.config_struct.tag_id_list.get(i));
                fragment.setArguments(bundle);
                fragments.add(fragment);
            }
        }
        tab_adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (BaseActivity.config_struct.class_changed || BaseActivity.config_struct.favorite_changed) {
            BaseActivity.config_struct.class_changed = false;
            BaseActivity.config_struct.favorite_changed = false;
            refreshTag();
        }
        tab_adapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DAO.init(getApplicationContext());
        ImageButton config_button = (ImageButton)findViewById(R.id.config_button);
        config_intent = new Intent(this, ConfigActivity.class);
        config_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(config_intent);
            }
        });

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        fragments = new Vector<>();
        /*Observable.fromArray(titles)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Fragment fragment = new HeadlinesFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("text",s);
                        fragment.setArguments(bundle);
                        fragments.add(fragment);
                    }
                });*/
        for (int i = 0; i < BaseActivity.config_struct.tag_list.size(); ++i) {
            Fragment fragment = new HeadlinesFragment();
            Bundle bundle = new Bundle();
            bundle.putString("text",BaseActivity.config_struct.tag_list.get(i));
            bundle.putInt("id",BaseActivity.config_struct.tag_id_list.get(i));
            fragment.setArguments(bundle);
            fragments.add(fragment);
        }
        Log.i(TAG, "onCreate: "+fragments.size());
        tab_adapter = new TabFragmentAdapter(fragments, BaseActivity.config_struct.tag_list, getSupportFragmentManager(), this);
        viewPager.setAdapter(tab_adapter);
        viewPager.setCurrentItem(1);
        // 初始化
        TabLayout tablayout = (TabLayout) findViewById(R.id.tablayout);
// 将ViewPager和TabLayout绑定
        tablayout.setupWithViewPager(viewPager);
// 设置tab文本的没有选中（第一个参数）和选中（第二个参数）的颜色
        tablayout.setTabTextColors(Color.BLACK, Color.WHITE);
    }

    @Override
    public void onArticleSelected(String id) {

/*
        // Create fragment and give it an argument for the selected article
        ArticleFragment newFragment = new ArticleFragment();
        Bundle args = new Bundle();
        args.putInt(ArticleFragment.ARG_POSITION, position);
        newFragment.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
        */
        Log.i(TAG, "onArticleSelected");
        Intent intent = new Intent(this, ArticleFragment.class);
        intent.putExtra(ArticleFragment.ARG_POSITION, id);
        startActivity(intent);

    }
}
