package bignews.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import bignews.myapplication.db.DAO;
import bignews.myapplication.db.DAOParam;

public class ArticleFragment extends Activity implements View.OnClickListener{
    final static String ARG_POSITION = "position";
    int mCurrentPosition = -1;
    DAO dao = DAO.getInstance();

    PopupWindow pop;
    TextView textView;
    View view;
    ImageButton weChatFriend;
    //ImageButton weChatZone;
    ImageButton qq;
    ImageButton weiBo;
    Button cancel;
    Button collect;
    //Toolbar toolBar;
    LinearLayout linearLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_view);

        initView();
        initData();
        Intent intent = getIntent();
        mCurrentPosition = intent.getIntExtra(ARG_POSITION, -1);          //
        updateArticleView(mCurrentPosition);

        ImageView imageView = (ImageView) findViewById(R.id.menu);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pop.showAtLocation(linearLayout, Gravity.BOTTOM, 0, 0);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pop.dismiss();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.cancel:
                pop.dismiss();
                return;
            case R.id.collect:
                Toast.makeText(getApplicationContext(), "已收藏", Toast.LENGTH_SHORT);
                return;
        }
        String pakName = "";

        switch(v.getId())
        {
            case R.id.weChatFriend:
                pakName = "com.tencent.mm";
                break;
            case R.id.weiBo:
                pakName = "com.sina.weibo";
                break;
            case R.id.qq:
                pakName = "com.tencent.mobileqq";
                break;
            /*case R.id.weChatZone:
                pakName = "com.qzone";
                break;*/
        }
        Intent intent = new Intent(Intent.ACTION_SEND);

        File f = new File("/storage/emulated/0/DCIM/Camera/IMG_20161125_094646.jpg");
        Uri u = Uri.fromFile(f);
        intent.putExtra(intent.EXTRA_STREAM, u);
        intent.setType("image/*");
        intent.putExtra("body", "233");

        intent.putExtra(Intent.EXTRA_SUBJECT, "这里是分享主题");
        intent.putExtra(Intent.EXTRA_TEXT, "这里是分享内容");

        intent.setPackage(pakName);
        this.startActivity(Intent.createChooser(intent, "分享到"));
    }

    public void updateArticleView(int position)
    {
        TextView article = (TextView) findViewById(R.id.article);
        article.setText(dao.getNews(DAOParam.fromNewsId(position)).blockingGet().newsTitle);
        mCurrentPosition = position;
    }
    /*@Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save the current article selection in case we need to recreate the fragment
        //outState.putInt(ARG_POSITION, mCurrentPosition);
    }*/

    private void initView()
    {
        LayoutInflater inflater = LayoutInflater.from(this);
        view = inflater.inflate(R.layout.popup, null);       // new View

        textView = (TextView) findViewById(R.id.article);
        linearLayout = (LinearLayout) findViewById(R.id.linear);
        weChatFriend = (ImageButton) view.findViewById(R.id.weChatFriend);
        //weChatZone = (ImageButton) view.findViewById(R.id.weChatZone);
        qq = (ImageButton) view.findViewById(R.id.qq);
        weiBo = (ImageButton) view.findViewById(R.id.weiBo);
        cancel = (Button) view.findViewById(R.id.cancel);
        collect = (Button) view.findViewById(R.id.collect);
        //toolBar = (Toolbar) findViewById(R.id.toolbar1);
    }
    private void initData()
    {
        pop = new PopupWindow(view, ViewPager.LayoutParams.MATCH_PARENT, 750, true); // 有点问题
        //pop.setAnimationStyle(R.style.MenuAnimationFade);
        pop.setBackgroundDrawable(new BitmapDrawable());
        //pop.setContentView(view);
        cancel.setOnClickListener(this);
        collect.setOnClickListener(this);
        weChatFriend.setOnClickListener(this);
        //weChatZone.setOnClickListener(this);
        qq.setOnClickListener(this);
        weiBo.setOnClickListener(this);
    }
}