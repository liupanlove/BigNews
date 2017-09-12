package bignews.myapplication.utils;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import bignews.myapplication.MainActivity;
import bignews.myapplication.R;
import bignews.myapplication.db.Headline;

/**
 * Created by guoye on 2017/9/12.
 */

public class HeadlineAdapter extends BaseAdapter {
    List<Headline> headlines;
    LayoutInflater inflater;

    public HeadlineAdapter(LayoutInflater inflater, List<Headline> headlines) {
        super();
        this.headlines = headlines;
        this.inflater = inflater;
    }

    @Override
    public int getCount() {
        return headlines.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view_, ViewGroup viewGroup) {
        View view = null;
        view = inflater.inflate(R.layout.search_item, null);
        Headline u = headlines.get(i);

        //通过回调这个方法传过来的position参数获取到指定数据源中的对象
        //找到布局文件中的控件
        TextView title = (TextView) view.findViewById(R.id.title);
        SimpleDraweeView image = (SimpleDraweeView) view.findViewById(R.id.image);
        title.setText(u.news_Title);
        String[] imgs = u.news_Pictures.split("( )|(;)");
        String img = "";
        for (int j = 0; j < imgs.length; ++j)
            if (!imgs[j].equals("")) {
                img = imgs[j];
                break;
            }

        if (!img.equals("")) {
            Uri uri = Uri.parse(imgs[0]);
            Log.v("Err", i + "  " + imgs[0]);
            image.setImageURI(uri);
        } else {
            image.setVisibility(View.GONE);
        }
        return view;
    }
}
