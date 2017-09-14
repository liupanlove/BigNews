package bignews.myapplication.db;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.annotation.MainThread;
import android.util.Log;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.Callable;

import bignews.myapplication.MyApplication;
import bignews.myapplication.db.dao.HeadlineDao;
import bignews.myapplication.db.dao.KeywordDao;
import bignews.myapplication.db.dao.NewsDao;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import retrofit2.http.HEAD;

import static java.util.Collections.sort;

/**
 * Created by lazycal on 2017/9/7.
 */

public class DAO {
    public static HashMap<Integer, String> classTags;
    static {
        classTags = new HashMap<>();
        //classTags.put(DAOParam.FAVORITE, "收藏");
        //classTags.put(DAOParam.RECOMMENDATION, "推荐");
        String tmp[] = {"", "科技", "军事", "国内", "社会", "文化", "汽车", "国际", "体育", "财经", "健康", "娱乐"};
        for (int i = 1; i < tmp.length; ++i)
            classTags.put(i, tmp[i]);
    };
    private static final String TAG = "dao";
    private static DAO dao;
    private AppDatabase mDb;

    public HeadlineDao getHeadlineDao() {
        return headlineDao;
    }

    public NewsDao getNewsDao() {
        return newsDao;
    }

    public KeywordDao getKeywordDao() {
        return keywordDao;
    }

    private HeadlineDao headlineDao;
    private NewsDao newsDao;
    private KeywordDao keywordDao;
    private Context context;

    public Preferences getSettings() {
        return new Preferences(context);
    }

    public boolean setSettings(Preferences settings) {
        return settings.commit(context);
    }

    private DAO() {}

    public synchronized static DAO getInstance() {
        if (dao == null) {
            dao = new DAO();
            dao.context = MyApplication.getAppContext();
            dao.mDb = Room.databaseBuilder(dao.context.getApplicationContext(), AppDatabase.class, "bignews").build();
            dao.headlineDao = dao.mDb.headlineDao();
            dao.newsDao = dao.mDb.newsDao();
            dao.keywordDao = dao.mDb.keywordDao();
        }
        return dao;
        //if (dao == null) throw new AssertionError("dao is null. please call init first");
        //return dao;
    }
    /**
     * Return a singleton.
     * @return the instance of dao
     */
    public synchronized static DAO init(Context context) {
        return getInstance();
    }


    Single<Headline> getHeadline(DAOParam param) {
        Log.i(TAG, "getHeadline: "+param);
        return headlineDao.findHeadlineByID(param.newsID)
                .map(new Function<List<Headline>, Headline>() {
                    @Override
                    public Headline apply(@NonNull List<Headline> headlines) throws Exception {
                        if (headlines.size() == 0) throw new RuntimeException("No such headline.");
                        Log.i(TAG, "apply: check if visited");
                        for (Headline headline : headlines)
                            headline.isVisited = !newsDao.findByID(headline.news_ID)
                                    .blockingGet().isEmpty();
                        return headlines.get(0);
                    }
                });
    }

    public Single<ArrayList<Headline>> headlineObservable(final DAOParam param) {
        return Single.defer(new Callable<SingleSource<? extends ArrayList<Headline>>>() {
            public String keyword = null;
            @Override
            public SingleSource<? extends ArrayList<Headline>> call() throws Exception {
                if (param.category != null && param.category == DAOParam.RECOMMENDATION) {
                    if (keyword == null) { //first setup
                        Log.i(TAG, "first setup");
                        List<Keyword> keywords = keywordDao.getHighFreqWord().blockingGet();
                        if (keywords.isEmpty()) keyword = "国际";
                        else {
                            keyword = "";
                            for (Keyword item : keywords) {
                                keyword += item.word + " ";
                            }
                        }
                    }
                    Log.i(TAG, "Thread: "+Thread.currentThread());
                    Log.i(TAG, "best keywords: "+keyword);
                    return APICaller.getInstance().searchHeadlines(DAOParam.fromKeyword(keyword, param.offset, param.limit))
                            .map(new Function<List<Headline>, ArrayList<Headline>>() {
                                @Override
                                public ArrayList<Headline> apply(@NonNull List<Headline> headlines) throws Exception {
                                    Log.i(TAG, "apply: check if visited");
                                    for (Headline headline : headlines)
                                        headline.isVisited = !newsDao.findByID(headline.news_ID)
                                                .blockingGet().isEmpty();
                                    return (ArrayList) headlines;
                                }
                            });
                }
                return getHeadlineList(param);
            }
        }).doAfterSuccess(
                new Consumer<ArrayList<Headline>>() {
                    @Override
                    public void accept(ArrayList<Headline> headlines) throws Exception {
                        Log.i(TAG, "doAfterSuccess: " + headlines);
                        param.offset += param.limit;
                        Log.i(TAG, "doAfterSuccess: " + param);
                        Log.i(TAG, "doAfterSuccess insert into Headline Database begin");
                        for (Headline headline : headlines)
                            headlineDao.addHeadline(headline);
                        Log.i(TAG, "doAfterSuccess insert into Headline Database done");
                    }
                });
    }

    /**
     * Get news body.
     * @param param parameter
     * @return The news body
     */
    public Single<News> getNews(final DAOParam param)
    {
        Log.i(TAG, "getNews: "+param);
        Single<News> fetchFromDB = newsDao.findByID(param.newsID)
                .map(new Function<List<News>, News>() { // get news. workaround
                    @Override
                    public News apply(@NonNull List<News> newses) throws Exception {
                        Log.i(TAG, "fetchFromDB: "+newses);
                        if (newses.size() == 0) throw new NoSuchNewsException();
                        return newses.get(0);
                    }
                });
        final Single<News> fetchFromAPI = APICaller.getInstance().loadNews(param)
                .doAfterSuccess(new Consumer<News>() {
                    @Override
                    public void accept(News news) throws Exception {
                        Log.i(TAG, "fetchFrom API: doAfterSuccess begin"+news);
                        for (Keyword keyword:
                                news.Keywords) { //adding keywords
                            List<Keyword> keywords = keywordDao.findKeywordByText(keyword.word).blockingGet();
                            Keyword newKeyword = new Keyword(keyword.word, keyword.score);
                            if (keywords.size() != 0) newKeyword.score += keywords.get(0).score;
                            keywordDao.addKeyword(newKeyword);
                        }
                        newsDao.addNews(news); //add news into database
                        Log.i(TAG, "fetchFrom API: doAfterSuccess done");
                    }
                })
                .map(new Function<News, News>() {
                    @Override
                    public News apply(@NonNull News news) throws Exception {
                        Log.i(TAG, "fetchFromAPI: begin");
                        sort(news.persons);
                        for (News.Person person:
                                news.persons) { //add baidu baidke link
                            String encoded = URLEncoder.encode(person.word, "UTF-8");
                            news.news_HTMLContent = news.news_Content.replaceAll(person.word,
                                    String.format("<a href=\"https://baike.baidu.com/item/%s\">%s</a>",
                                            encoded, person.word));
                        }
                        Log.i(TAG, "fetchFromAPI: done "+news);
                        return news;

                    }
                });
        return (getSettings().isOffline
                ? fetchFromDB
                : fetchFromDB.onErrorResumeNext(new Function<Throwable, SingleSource<? extends News>>() {
                    @Override
                    public SingleSource<? extends News> apply(@NonNull Throwable throwable) throws Exception {
                        Log.i(TAG, "onErrorResumeNext: ", throwable);
                        return NoSuchNewsException.class.isInstance(throwable)
                                ? fetchFromAPI
                                : Single.<News>error(throwable);
                    }
                }))
                .map(new Function<News, News>() {
                    @Override
                    public News apply(@NonNull News news) throws Exception {
                        List<Headline> headline = headlineDao.findHeadlineByID(news.news_ID)
                                .blockingGet();
                        news.isFavorite = (!headline.isEmpty() && headline.get(0).isFavorite);
                        return news;
                    }
                });
    }

    /**
     * Get headline list.
     * User can specify @link{DAOParam#category}, @link{DAOParam#Keywords}, @link{DAOParam#mode}
     * and so on.
     * @param param parameter
     * @return An ArrayList containing the headlines
     */
    @Deprecated
    public Single<ArrayList<Headline>> getHeadlineList(final DAOParam param)
    {
        /*if (Math.PI > 0) return Single.create(new SingleOnSubscribe<ArrayList<Headline>>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<ArrayList<Headline>> e) throws Exception {
                ArrayList<Headline> headlines = new ArrayList<Headline>();
                Headline headline = new Headline();
                headline.news_Pictures = "http://www.people.com.cn/mediafile/pic/20150528/5/9571866183301822149.jpg";
                headline.news_Title = "s";
                headline.news_ID = "1";
                headlines.add(headline);
                e.onSuccess(headlines);
            }
        });*/
        Log.i(TAG, "getHeadlineList: "+param);
        Single<List<Headline>> fetch;
        if (param.keywords != null)
            fetch = APICaller.getInstance().searchHeadlines(param);
        else if (param.category == DAOParam.FAVORITE)
            fetch = headlineDao.findHeadlineByFavorite(param.offset, param.limit);
        else if (param.category == DAOParam.RECOMMENDATION) {
            /*
            fetch = Single.create(new SingleOnSubscribe<List<Headline>>() {
                DAOParam daoParam = null;
                @Override
                public void subscribe(@NonNull SingleEmitter<List<Headline>> e) throws Exception {
                    if (daoParam == null) {
                        daoParam = param;
                        List<Keyword> keywords = keywordDao.getHighFreqWord().blockingGet();
                        if (keywords.isEmpty()) daoParam.keywords = "国际";
                        else {
                            daoParam.keywords = "";
                            for (Keyword keyword : keywords) {
                                daoParam.keywords += keyword.word + " ";
                            }
                        }
                        daoParam.category = null;
                        Log.i(TAG, "all best keywords: "+keywords+" daoParam: "+daoParam);
                        Log.i(TAG, "best keywords: "+daoParam.keywords);
                    }
                    e.onSuccess(APICaller.getInstance().searchHeadlines(daoParam).blockingGet());
                }
            });
            */
            final Single<List<Keyword>> highFreq = keywordDao.getHighFreqWord();
            fetch = highFreq.map(new Function<List<Keyword>, List<Headline>>() {
                @Override
                public List<Headline> apply(@NonNull List<Keyword> keywords) throws Exception {
                    Log.i(TAG, "all best keywords: "+keywords);
                    if (keywords.isEmpty()) param.keywords = "国际";
                    else {
                        param.keywords = "";
                        for (Keyword keyword : keywords) {
                            param.keywords += keyword.word + " ";
                        }
                    }
                    param.category = null;
                    Log.i(TAG, "best keywords: "+param.keywords);
                    return APICaller.getInstance().searchHeadlines(param).blockingGet();
                }
            });
        }else if (param.category == DAOParam.HISTORY) {
            fetch = headlineDao.loadHistory(param.offset, param.limit);
        }
        else fetch = ((getSettings().isOffline)
                    ? headlineDao.load(classTags.get(param.category), param.offset, param.limit)
                    : APICaller.getInstance().loadHeadlines(param));
        return fetch.map(new Function<List<Headline>, ArrayList<Headline>>() {// check if visited
                    @Override
                    public ArrayList<Headline> apply(@NonNull List<Headline> headlines) throws Exception {
                        Log.i(TAG, "apply: check if visited");
                        for (Headline headline : headlines)
                            headline.isVisited = !newsDao.findByID(headline.news_ID)
                                    .blockingGet().isEmpty();
                        return new ArrayList<>(headlines);
                    }
                })
                /*.map(new Function<ArrayList<Headline>, ArrayList<Headline>>() {// insert into Headline Database
                    @Override
                    public ArrayList<Headline> apply(@NonNull ArrayList<Headline> headlines) throws Exception {
                        Log.i(TAG, "insert into Headline Database begin");
                        for (Headline headline : headlines)
                            headlineDao.addHeadline(headline);
                        Log.i(TAG, "insert into Headline Database done");
                        return headlines;
                    }
                })*/;
    }

    /**
     * Add a piece of news into favorites
     * @param newsID news ID
     */
    public Completable star(final String newsID) {
        Log.i(TAG, "star: "+newsID);
        return Completable.fromRunnable(new Runnable() {
            @Override
            public void run() {
                Headline headline = getHeadline(DAOParam.fromNewsId(newsID)).blockingGet();
                headline.isFavorite = true;
                headlineDao.updateHeadline(headline);
                Log.i(TAG, "run: star begin "+newsID);
                /*try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
                Log.i(TAG, "run: star end "+newsID);
            }
        });
    }
    /**
     * Remove a piece of news from favorites
     * @param newsID news ID
     */
    public Completable unStar(final String newsID) {
        Log.i(TAG, "unStar: "+newsID);
        return Completable.fromRunnable(new Runnable() {
            @Override
            public void run() {
                Headline headline = getHeadline(DAOParam.fromNewsId(newsID)).blockingGet();
                headline.isFavorite = false;
                headlineDao.updateHeadline(headline);
            }
        });
    }

    /**
     * Clear cached files
     */
    public Completable clearCache()
    {
        return Completable.fromRunnable(new Runnable() {
            @Override
            public void run() {

            }
        });
    }


    static DAO init(Context context, AppDatabase build) {//FOR TEST
        if (dao == null) {
            dao = new DAO();
            dao.context = context;
            dao.mDb = build;
            dao.headlineDao = dao.mDb.headlineDao();
            dao.newsDao = dao.mDb.newsDao();
            dao.keywordDao = dao.mDb.keywordDao();
        }
        return dao;
    }

    static private class NoSuchNewsException extends RuntimeException {
    }
}
