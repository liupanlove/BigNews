package bignews.myapplication.db;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

import bignews.myapplication.db.dao.HeadlineDao;
import bignews.myapplication.db.dao.KeywordDao;
import bignews.myapplication.db.dao.NewsDao;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Created by lazycal on 2017/9/7.
 */

public class DAO {

    Context context;
    private static final String TAG = "dao";
    private static DAO dao;
    private AppDatabase mDb;
    private HeadlineDao headlineDao;
    private NewsDao newsDao;
    private KeywordDao keywordDao;

    public Preferences getSettings() {
        return new Preferences(context);
    }

    public boolean setSettings(Preferences settings) {
        return new Preferences(context, settings).commit();
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
            dao.context = context;
            dao.mDb = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "bignews").build();
            dao.headlineDao = dao.mDb.headlineDao();
            dao.newsDao = dao.mDb.newsDao();
            dao.keywordDao = dao.mDb.keywordDao();
        }
        return dao;
    }

    /**
     * Get news body.
     * @param param parameter
     * @return The news body
     */
    public Single<News> getNews(final DAOParam param)
    {
        return Single.just(new News())
                .map(new Function<News, News>() {
                    @Override
                    public News apply(@NonNull News news) throws Exception {
                        news.news_Title = ""+param.newsID;
                        return news;
                    }
                });
    }

    /**
     * Get headline list.
     * User can specify @link{DAOParam#category}, @link{DAOParam#keywords}, @link{DAOParam#mode}
     * and so on.
     * @param param parameter
     * @return An ArrayList containing the headlines
     */
    public Single<ArrayList<Headline>> getHeadlineList(final DAOParam param)
    {
        return APICaller.getInstance().loadHeadlines(param);
        /*
        cnt += 1;
        if (cnt % 5 == 0) try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "getHeadlineList: Before: "+Headlines);
        Headlines.add(String.valueOf(cnt));
        Log.i(TAG, "getHeadlineList: After: "+Headlines);
        return (ArrayList<String>) Headlines.clone();
        */
        /*
        return Single.just(new ArrayList<Headline>())
                .map(new Function<ArrayList<Headline>, ArrayList<Headline>>() {
                    @Override
                    public ArrayList<Headline> apply(@NonNull ArrayList<Headline> headLines) throws Exception {

                        for (int i = param.offset; i < param.offset + param.limit; ++i) {
                            Headline headline = new Headline();
                            headline.news_Title = "title:" + i;
                            headline.newsClassTag = param.category + "";
                            headline.newsID = i + "";
                            headLines.add(headline);
                        }
                        return headLines;
                    }
                })
                .blockingGet();
                */
    }

    /**
     * Add a piece of news into favorites
     * @param newsID news ID
     */
    public Completable star(int newsID) {
        return Completable.fromRunnable(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    /**
     * Remove a piece of news from favorites
     * @param newsID news ID
     */
    public Completable unStar(int newsID) {
        return Completable.fromRunnable(new Runnable() {
            @Override
            public void run() {

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


}
