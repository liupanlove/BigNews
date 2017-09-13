package bignews.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.ScrollingMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
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

import com.facebook.drawee.view.SimpleDraweeView;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;


import org.w3c.dom.Text;

import java.io.File;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import bignews.myapplication.db.DAO;
import bignews.myapplication.db.DAOParam;
import bignews.myapplication.db.News;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ArticleFragment extends AppCompatActivity implements View.OnClickListener{
    final static String TAG = "ArticleFragment";
    final static String ARG_POSITION = "id";
    //int mCurrentPosition = -1;
    String newsID = "";
    DAO dao = DAO.getInstance();

    PopupWindow pop;
    TextView textView;
    View view;
    ImageButton weChatFriend;
    //ImageButton weChatZone;
    ImageButton qq;
    ImageButton weiBo;
    Button cancel;
    TextView article;
    TextView headline;
    private Button collect;
    private Button speaker;
    private LinearLayout linearLayout;
    //private TextToSpeech tts;
    private Button back;
    private Disposable disposable;
    private String newsContent = "";
    private boolean isFavourite;
    private String auther;
    private TextView newsAuther;
    private String pictures;
    private String title;
    private String shareContent = "";
    private SpeechSynthesizer mTts;
    private LinearLayout linearLayout1;
    private LinearLayout linearLayout2;
   // private SimpleDraweeView [] imageViews;
    //private TextView headline;
    private SingleObserver<? super News> subscriber = new SingleObserver<News>() {
        @Override
        public void onSubscribe(@NonNull Disposable d) {
            disposable = d;
        }

        @Override
        public void onSuccess(@NonNull News news) {
            /*for(String e: news.seggedPListOfContent)
            {
                newsContent += e;
                Log.d(TAG, e);
            }*/
            title = news.news_Title;
            newsContent = news.news_Content;
            isFavourite = news.isFavorite;
            auther = news.news_Author;
            newsAuther.setText(auther);
            pictures = news.news_Pictures;
            Log.d(TAG, "Pictues" + pictures);
            Log.d(TAG, news.news_Pictures);
            //int headlineLength = title.length();
            //String str = title + "\n\n" + newsContent;     // ------------------
            headline.setText(title);
            //newsContent += news.news_Content;
            //newsContent = newsContent.replaceAll("\\s*", "\\n");
            Log.d(TAG, newsContent);

            shareContent += ("标题：" + title + "\n" + "新闻详情：" + news.news_URL);
            /*Spannable textSpan = new SpannableStringBuilder(str);
            textSpan.setSpan(new AbsoluteSizeSpan(80), 0, headlineLength, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            textSpan.setSpan(new AbsoluteSizeSpan(50), headlineLength, str.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);*/
            article.setText(newsContent);
            Log.d(TAG, "isFavourite" + isFavourite);
            if(isFavourite)
            {
                collect.setText("取消收藏");
            }
            else
                collect.setText("收藏");
            if(BaseActivity.config_struct.picture_mode)       // 无图模式的接口
            {
                String [] imgs = pictures.split("( )|(;)|(；)");
                for(int i = 0; i < imgs.length; ++i)
                {
                    if(!imgs[i].equals(""))
                    {
                        Uri uri = Uri.parse(imgs[i]);
                        SimpleDraweeView image = new SimpleDraweeView(ArticleFragment.this);
                        image.setMinimumHeight(600);
                        image.setPadding(20, 20, 20, 20);
                        image.setImageURI(uri);
                        if(i == 0)
                        {
                            linearLayout2.addView(image);
                        }
                        else
                        {
                            Log.d(TAG, "Picture[" + i + "]: " + imgs[i]);
                            linearLayout1.addView(image);
                        }
                    }
                }
            }
        }

        @Override
        public void onError(@NonNull Throwable e) {
            Log.d(TAG, "onError: "+Log.getStackTraceString(e));
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_view);

        SpeechUtility.createUtility(getApplicationContext(), SpeechConstant.APPID +"=59b54aff");

        mTts = SpeechSynthesizer.createSynthesizer(ArticleFragment.this, null);
        article = (TextView) findViewById(R.id.article);
        //article.setMovementMethod(ScrollingMovementMethod.getInstance());

        ttsInit();
        initView();
        initData();
        Intent intent = getIntent();
        //mCurrentPosition = intent.getIntExtra(ARG_POSITION, -1);          //

        newsID = intent.getStringExtra(ARG_POSITION);
        Log.i(TAG, newsID);
        Uri uri = Uri.parse("http://himg2.huanqiu.com/attachment2010/2016/0912/13/16/20160912011621140.png");
        //SimpleDraweeView image = new SimpleDraweeView(ArticleFragment.this);
        //image.setImageURI(uri);
        //linearLayout1 = (LinearLayout) findViewById(R.id.articlelinearlayout);
        //Button tmp = new Button(this);
        //tmp.setText("haipa");


        //updateArticleView(newsID);
        if(newsContent.equals(""))
            loadNewsData();
        speaker = (Button) findViewById(R.id.speaker);

        Button imageView = (Button) findViewById(R.id.menu);
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


        //tts.getMaxSpeechInputLength();
        //Log.i(TAG, tts.getMaxSpeechInputLength() + "害怕");

        speaker.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //tts.speak(newsContent, TextToSpeech.QUEUE_ADD, null); //"1月1日，这是元旦节
                //     "
                mTts.startSpeaking(newsContent, null);
            }
        });
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
                if(isFavourite){
                    collect.setText("收藏");
                    isFavourite = false;
                    dao.unStar(newsID).subscribeOn(Schedulers.newThread()).subscribe();
                    //BaseActivity.config_struct
                }
                else
                {
                    collect.setText("取消收藏");
                    isFavourite = true;
                    dao.star(newsID).subscribeOn(Schedulers.newThread())
                            .subscribe();
                }
                return;
            case R.id.back:
                finish();
                return;
        }
        String pakName = "";

        switch(v.getId())
        {
            case R.id.weChatFriend:
                pakName = "com.tencent.mm";          //com.tencent.mm.ui.tools.ShareToTimeLineUI
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
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, shareContent);
        intent.setPackage(pakName);
        this.startActivity(Intent.createChooser(intent, ""));
    }

    public void updateArticleView(String id)
    {
       // TextView article = (TextView) findViewById(R.id.article);
        Log.i(TAG, id);
        Log.i(TAG, dao.getNews(DAOParam.fromNewsId(id)).blockingGet().news_Title);
        article.setText(dao.getNews(DAOParam.fromNewsId(id)).blockingGet().news_Title);
        //mCurrentPosition = position;
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

        headline = (TextView) findViewById(R.id.headline);
        textView = (TextView) findViewById(R.id.article);
        newsAuther = (TextView) findViewById(R.id.newsauther);
        back = (Button) findViewById(R.id.back);
        linearLayout = (LinearLayout) findViewById(R.id.linear);
        linearLayout1 = (LinearLayout) findViewById(R.id.articlelinearlayout);
        linearLayout2 = (LinearLayout) findViewById(R.id.articlelinearlayout1);
        weChatFriend = (ImageButton) view.findViewById(R.id.weChatFriend);
        //weChatZone = (ImageButton) view.findViewById(R.id.weChatZone);
        qq = (ImageButton) view.findViewById(R.id.qq);
        weiBo = (ImageButton) view.findViewById(R.id.weiBo);
        cancel = (Button) view.findViewById(R.id.cancel);
        collect = (Button) view.findViewById(R.id.collect);
        //toolBar = (Toolbar) findViewById(R.id.toolbar1);
        /*imageViews = new SimpleDraweeView[8];
        imageViews[0] = (SimpleDraweeView) findViewById(R.id.image1);
        imageViews[1] = (SimpleDraweeView) findViewById(R.id.image2);
        imageViews[2] = (SimpleDraweeView) findViewById(R.id.image3);
        imageViews[3] = (SimpleDraweeView) findViewById(R.id.image4);
        imageViews[4] = (SimpleDraweeView) findViewById(R.id.image5);
        imageViews[5] = (SimpleDraweeView) findViewById(R.id.image6);
        imageViews[6] = (SimpleDraweeView) findViewById(R.id.image7);
        imageViews[7] = (SimpleDraweeView) findViewById(R.id.image8);*/
    }
    private void initData()
    {
        pop = new PopupWindow(view, ViewPager.LayoutParams.MATCH_PARENT, 750, true); // 有点问题
        //pop.setAnimationStyle(R.style.MenuAnimationFade);
        pop.setBackgroundDrawable(new BitmapDrawable());
        //pop.setContentView(view);
        cancel.setOnClickListener(this);
        back.setOnClickListener(this);
        collect.setOnClickListener(this);
        weChatFriend.setOnClickListener(this);
        //weChatZone.setOnClickListener(this);
        qq.setOnClickListener(this);
        weiBo.setOnClickListener(this);
    }

    private void ttsInit()
    {
        /*tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS)
                {
                    int result = tts.setLanguage(Locale.CHINESE);
                    tts.setPitch(1.0f);
                    tts.setSpeechRate(1.0f);
                    if(result != TextToSpeech.LANG_COUNTRY_AVAILABLE && result != TextToSpeech.LANG_AVAILABLE) {
                        Toast.makeText(getApplicationContext(), "暂不支持", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "无法初始化", Toast.LENGTH_SHORT).show();
                }
            }
        });*/
        mTts.setParameter(SpeechConstant.SPEED, "50");
        mTts.setParameter(SpeechConstant.VOLUME, "80");
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH,"./sdcard/iflytek.pcm");

    }

    private void loadNewsData()
    {
        final DAOParam param = DAOParam.fromNewsId(newsID);

        dao.getNews(param)
                .timeout(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
    @Override
    public void onPause() {
        super.onPause();
        if (disposable != null) {
            Log.i(TAG, "onPause: " + " Disposing.");
            disposable.dispose();
        }
    }
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if(mTts != null)
        {
            mTts.stopSpeaking();
            mTts.destroy();
        }

    }
}