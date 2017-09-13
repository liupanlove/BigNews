package bignews.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import bignews.myapplication.db.DAO;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import bignews.myapplication.db.DAOParam;
import bignews.myapplication.db.Headline;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lazycal on 2017/9/7.
 */

public class HeadlinesFragment extends Fragment implements AdapterView.OnItemClickListener {

    private static final String TAG = "HeadlinesFragment";
    private static final int LIMIT = 5;
    DAO dao = DAO.getInstance(); //should be a singleton.
    Single<ArrayList<Headline>> headlineObservable = null;


    private int mode;
    private String mText;      // Type
    private int mID;   // mID == -2 表示search

    @BindView(R.id.listView)
    PullToRefreshListView listView;

    private List<Headline> news = new ArrayList<>();
    OnHeadlineSelectedListener mCallback;
    private HeadlineAdapter adapter;
    private SingleObserver<? super ArrayList<Headline>> subscriber = new SingleObserver<ArrayList<Headline>>() {
        @Override
        public void onSubscribe(@NonNull Disposable d) {
            disposable = d;
        }

        @Override
        public void onSuccess(@NonNull ArrayList<Headline> strings) {
            news.addAll(strings);
            adapter.notifyDataSetChanged();
            listView.onRefreshComplete();
            Log.i(TAG, "onSuccess: loadNewsDatasuccess "+mText+" "+news);
        }

        @Override
        public void onError(@NonNull Throwable e) {
            Log.d(TAG, "onError: "+Log.getStackTraceString(e));
            listView.onRefreshComplete();
        }
    };
    private Disposable disposable;

    public interface OnHeadlineSelectedListener {
        /** Called by HeadlinesFragment when a list item is selected */
        public void onArticleSelected(String id);  // int postion
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // Notify the parent activity of selected item
        Log.i(TAG, "ArticleClick");
        mCallback = (OnHeadlineSelectedListener)getActivity();
        Log.i(TAG, news.get((int)id).news_ID);
        news.get(position - 1).isVisited = true;
        mCallback.onArticleSelected((news.get(position - 1).news_ID));  // Integer.parseInt
    }

    @Override
    public void onCreate(@Nullable Bundle bundle) {
        Log.i("Err", "onCreate: "+mText);
        super.onCreate(bundle);
        if(getArguments()!=null){
            mText = getArguments().getString("text");
            mID = getArguments().getInt("id");

            if(mID == -2)
            {
                headlineObservable = dao.headlineObservable(DAOParam.fromKeyword(mText, 0, LIMIT));
            }
            else
                headlineObservable = dao.headlineObservable(DAOParam.fromCategory(mID, 0, LIMIT));
        }
        if (news.size() == 0)
            loadNewsData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView: "+mText);
        View view = inflater.inflate(R.layout.fragment_layout, container, false);
        ButterKnife.bind(this, view); //??? mogai
        return view;
    }

    @Override
    public void onResume() {
        Log.i(TAG, "onResume: "+mText);
        super.onResume();

        listView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        // 下拉刷新
        //listView.getLoadingLayoutProxy(true, false).setPullLabel("下拉刷新");
        //listView.getLoadingLayoutProxy(true, false).setRefreshingLabel("正在刷新...");
        //listView.getLoadingLayoutProxy(true, false).setReleaseLabel("释放立即刷新");
        // 下拉加载
        listView.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载");
        listView.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载...");
        listView.getLoadingLayoutProxy(false, true).setReleaseLabel("释放立即加载");
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                Log.i("1", "onPullUpToLoad: hhh");
                loadNewsData();
            }
        });

        listView.setOnItemClickListener(this);

        adapter = new HeadlineAdapter(getActivity().getLayoutInflater(), news);
        listView.setAdapter(adapter);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onViewCreated: "+mText);
        super.onViewCreated(view, savedInstanceState);
    }

    private void loadNewsData() {//!!!BUG
        /*final DAOParam param;
        if(mID == -2)
        {
            param = DAOParam.fromKeyword(mText, news.size(), LIMIT);
            Log.i(TAG, "害怕");
        }
        else
            param = DAOParam.fromCategory(mID, news.size(), LIMIT);*/
        //news = dao.getHeadlineList(param);
        /*Single.create(new SingleOnSubscribe<ArrayList<Headline>>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<ArrayList<Headline>> e) throws Exception {
                e.onSuccess(dao.getHeadlineList(param));
            }
        })*/
        //dao.getHeadlineList(param)
        headlineObservable
                .timeout(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        Log.i(TAG, "loadNewsData: "+news+" "+mText);

    }
    @Override
    public void onPause() {
        super.onPause();
        if (disposable != null) {
            Log.i(TAG, "onPause: " + mText + " Disposing.");
            disposable.dispose();
        }
    }
}
