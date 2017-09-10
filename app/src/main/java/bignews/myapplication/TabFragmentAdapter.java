package bignews.myapplication;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import java.util.List;
import java.util.Vector;

/**
 * Created by lazycal on 2017/9/7.
 */

public class TabFragmentAdapter extends FragmentStatePagerAdapter {

    private static final String TAG = "TabFragmentAdapter";
    private final Vector<String> titles;
    private Context context;
    private List<Fragment> fragments;
    private FragmentManager fm;

    public TabFragmentAdapter(List<Fragment> fragments, Vector<String> titles, FragmentManager fm, Context context) {
        super(fm);
        this.fm = fm;
        this.context = context;
        this.fragments = fragments;
        this.titles = titles;
    }

    @Override
    public void notifyDataSetChanged() {
        Log.i(TAG, "notifyDataSetChanged: ");
        // 重写这个方法，取到子Fragment的数量，用于下面的判断，以执行多少次刷新
        //mChildCount = getCount();
        super.notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        Log.v("Err", position + " ");
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return titles.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.elementAt(position);
    }
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
