package bignews.myapplication.db;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import bignews.myapplication.MyApplication;
import bignews.myapplication.db.dao.HeadlineDao;
import bignews.myapplication.db.dao.KeywordDao;
import bignews.myapplication.db.dao.NewsDao;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Created by lazycal on 2017/9/7.
 */

public class DAO {
    public static HashMap<Integer, String> classTags;
    static {
        classTags = new HashMap<>();
        classTags.put(DAOParam.FAVORITE, "收藏");
        classTags.put(DAOParam.RECOMMENDATION, "推荐");
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
        if (dao == null) throw new AssertionError("dao is null. please call init first");
        return dao;
    }
    /**
     * Return a singleton.
     * @return the instance of dao
     */
    public synchronized static DAO init(Context context) {
        if (dao == null) {
            dao = new DAO();
            dao.context = MyApplication.getAppContext();
            dao.mDb = Room.databaseBuilder(dao.context.getApplicationContext(), AppDatabase.class, "bignews").build();
            dao.headlineDao = dao.mDb.headlineDao();
            dao.newsDao = dao.mDb.newsDao();
            dao.keywordDao = dao.mDb.keywordDao();
        }
        return dao;
    }


    Single<Headline> getHeadline(DAOParam param) {
        return headlineDao.findHeadlineByID(param.newsID)
                .map(new Function<List<Headline>, Headline>() {
                    @Override
                    public Headline apply(@NonNull List<Headline> headlines) throws Exception {
                        if (headlines.size() == 0) throw new RuntimeException("No such headline.");
                        return headlines.get(0);
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
        Log.i(TAG, "getNews: settings="+getSettings());
        return (getSettings().isOffline //TODO: optimize
                ? newsDao.findByID(param.newsID)
                .map(new Function<List<News>, News>() { // workaround
                    @Override
                    public News apply(@NonNull List<News> newses) throws Exception {
                        if (newses.size() == 0) throw new RuntimeException("No such news.");
                        return newses.get(0);
                    }
                })
                : (APICaller.getInstance().loadNews(param)
                .map(new Function<News, News>() {
                    @Override
                    public News apply(@NonNull News news) throws Exception {
                        newsDao.addNews(news);
                        return news;
                    }
                })))
                .map(new Function<News, News>() {//TODO: Strange
                    @Override
                    public News apply(@NonNull News news) throws Exception {
                        if (news.Keywords != null) //TODO: workaround
                        for (Keyword keyword:
                                news.Keywords) {
                            List<Keyword> keywords = keywordDao.findKeywordByText(keyword.word).blockingGet();
                            Keyword newKeyword = new Keyword(keyword.word, keyword.score);
                            if (keywords.size() != 0) newKeyword.score += keywords.get(0).score;
                            keywordDao.addKeyword(newKeyword);
                        }
                        return news;
                    }
                }); //TODO: image? 词条 add news, Keywords to database

    }

    /**
     * Get headline list.
     * User can specify @link{DAOParam#category}, @link{DAOParam#Keywords}, @link{DAOParam#mode}
     * and so on.
     * @param param parameter
     * @return An ArrayList containing the headlines
     */
    public Single<ArrayList<Headline>> getHeadlineList(final DAOParam param)
    {
        Single<List<Headline>> fetch;
        if (param.keywords != null)
            fetch = APICaller.getInstance().searchHeadlines(param);
        else if (param.category == DAOParam.FAVORITE)
            fetch = headlineDao.findHeadlineByFavorite(param.offset, param.limit);
        else if (param.category == DAOParam.RECOMMENDATION) {
            //Single<List<Keyword>> highFreq = keywordDao.getHighFreqWord();
            //fetch = Single.fheadlineDao.findHeadlineByFavorite(param.offset, param.limit);
            fetch = headlineDao.findHeadlineByFavorite(param.offset, param.limit);
        }
        else fetch = ((getSettings().isOffline) //TODO: class RECOMMENDATION
                    ? headlineDao.load(classTags.get(param.category), param.offset, param.limit)
                    : APICaller.getInstance().loadHeadlines(param));
        return fetch.map(new Function<List<Headline>, ArrayList<Headline>>() {// check if visited
                    @Override
                    public ArrayList<Headline> apply(@NonNull List<Headline> headlines) throws Exception {
                        Log.i(TAG, "apply: getHeadlineList");
                        for (Headline headline : headlines)
                            headline.isVisited = !newsDao.findByID(headline.news_ID)
                                    .blockingGet().isEmpty();
                        return new ArrayList<>(headlines);
                    }
                })
                .map(new Function<ArrayList<Headline>, ArrayList<Headline>>() {// insert into Headline Database
                    @Override
                    public ArrayList<Headline> apply(@NonNull ArrayList<Headline> headlines) throws Exception {
                        for (Headline headline : headlines)
                            headlineDao.addHeadline(headline);
                        return headlines;
                    }
                });
    }

    /**
     * Add a piece of news into favorites
     * @param newsID news ID
     */
    public Completable star(final String newsID) {
        return Completable.fromRunnable(new Runnable() {
            @Override
            public void run() {
                Headline headline = getHeadline(DAOParam.fromNewsId(newsID)).blockingGet();
                headline.isFavorite = true;
                headlineDao.addHeadline(headline);
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
        return Completable.fromRunnable(new Runnable() {
            @Override
            public void run() {
                Headline headline = getHeadline(DAOParam.fromNewsId(newsID)).blockingGet();
                headline.isFavorite = false;
                headlineDao.addHeadline(headline);
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
}
