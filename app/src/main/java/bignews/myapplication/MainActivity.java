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

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

public class MainActivity extends BaseActivity
        implements HeadlinesFragment.OnHeadlineSelectedListener{

    private static final String TAG = "MainActivity";
    private String titles[];
    {
        titles = new String[]{"1", "2", "3", "4", "5", "6", "7"};
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        final List<Fragment> fragments = new ArrayList<Fragment>();
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
        for (int i = 0; i < titles.length; ++i) {
            Fragment fragment = new HeadlinesFragment();
            Bundle bundle = new Bundle();
            bundle.putString("text",titles[i]);
            fragment.setArguments(bundle);
            fragments.add(fragment);
        }
        Log.i(TAG, "onCreate: "+fragments.size());
        viewPager.setAdapter(new TabFragmentAdapter(fragments, titles, getSupportFragmentManager(), this));
        // 初始化
        TabLayout tablayout = (TabLayout) findViewById(R.id.tablayout);
// 将ViewPager和TabLayout绑定
        tablayout.setupWithViewPager(viewPager);
// 设置tab文本的没有选中（第一个参数）和选中（第二个参数）的颜色
        tablayout.setTabTextColors(Color.BLACK, Color.WHITE);
    }

    @Override
    public void onArticleSelected(int position) {

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

        Intent intent = new Intent(this, ArticleFragment.class);
        intent.putExtra(ArticleFragment.ARG_POSITION, position - 1);
        startActivity(intent);

    }
}
