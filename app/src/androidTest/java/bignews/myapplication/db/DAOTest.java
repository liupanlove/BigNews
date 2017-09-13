package bignews.myapplication.db;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import bignews.myapplication.db.dao.HeadlineDao;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

import static org.junit.Assert.*;

/**
 * Created by lazycal on 2017/9/11.
 */
@RunWith(AndroidJUnit4.class)
public class DAOTest {

    private static final String TAG = "DAOTest";
    private DAO dao;

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getTargetContext();
        dao = DAO.init(context, Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build());
        Preferences preferences = dao.getSettings();
        preferences.isOffline = false;
        preferences.isNight = false;
        preferences.onlyText = false;
        preferences.shieldKeywords = new HashSet<>();
        preferences.classTags = 0;
        dao.setSettings(preferences);
    }
    @After
    public void closeDb() throws IOException {
        //dao.mDb.close();
    }
    @Test
    public void getNews() throws Exception {
        News news = dao.getNews(DAOParam.fromNewsId("201609130413080e91293fb5402b80437a65970fcb7d")).blockingGet();
        Log.i(TAG, "getNews: first="+news);
        assertEquals(news.news_Title, "乐视921邀请函曝光 乐Pro 3悬疑即将揭晓");
        news = dao.getNews(DAOParam.fromNewsId("201609130413080e91293fb5402b80437a65970fcb7d")).blockingGet();
        Log.i(TAG, "getNews: second="+news);
        assertEquals(news.news_Title, "乐视921邀请函曝光 乐Pro 3悬疑即将揭晓");
        Preferences preferences = dao.getSettings();
        Log.i(TAG, "getNews: "+preferences);
        preferences.isOffline = true;
        dao.setSettings(preferences);
        news = dao.getNews(DAOParam.fromNewsId("201609130413080e91293fb5402b80437a65970fcb7d")).blockingGet();
        Log.i(TAG, "getNews: third="+news);
        assertEquals(news.news_Title, "乐视921邀请函曝光 乐Pro 3悬疑即将揭晓");
        preferences.isOffline = false;
        dao.setSettings(preferences);
    }

    @Test
    public void speedTest() throws Exception {
        dao.headlineObservable(DAOParam.fromCategory(1, 0, 10)).subscribe();
        dao.headlineObservable(DAOParam.fromCategory(1, 10, 10)).subscribe();
    }

    @Test
    public void recommendation() throws Exception {
        dao.headlineObservable(DAOParam.fromCategory(DAOParam.RECOMMENDATION, 0, 10)).blockingGet();
        dao.headlineObservable(DAOParam.fromCategory(1, 0, 2))
                .map(new Function<ArrayList<Headline>, ArrayList<Headline>>() {
                    @Override
                    public ArrayList<Headline> apply(@NonNull ArrayList<Headline> headlines) throws Exception {
                        Log.i(TAG, "apply: get all news begin");
                        for (Headline headline :
                                headlines) {
                            dao.getNews(DAOParam.fromNewsId(headline.news_ID)).subscribe();
                        }
                        Log.i(TAG, "apply: get all news end");
                        return headlines;
                    }
                }).blockingGet();
        Single<ArrayList<Headline>> single = dao.headlineObservable(DAOParam.fromCategory(DAOParam.RECOMMENDATION, 0, 10));
        Log.i(TAG, "recommendation: first");
        single.blockingGet();
        dao.headlineObservable(DAOParam.fromCategory(2, 0, 10))
                .map(new Function<ArrayList<Headline>, ArrayList<Headline>>() {
                    @Override
                    public ArrayList<Headline> apply(@NonNull ArrayList<Headline> headlines) throws Exception {
                        Log.i(TAG, "apply: get all news begin");
                        for (Headline headline :
                                headlines) {
                            dao.getNews(DAOParam.fromNewsId(headline.news_ID)).subscribe();
                        }
                        Log.i(TAG, "apply: get all news end");
                        return headlines;
                    }
                }).blockingGet();
        Log.i(TAG, "recommendation: second");
        dao.headlineObservable(DAOParam.fromCategory(DAOParam.RECOMMENDATION, 0, 10)).blockingGet();
        Log.i(TAG, "recommendation: third");
        single.blockingGet();
    }

    @Test
    public void url() throws Exception {
        News news = dao.getNews(DAOParam.fromNewsId("201608090432c815a85453c34d8ca43a591258701e9b")).blockingGet();
        Log.i(TAG, "url: origin content: "+news.news_Content);
        Log.i(TAG, "url: html content: "+news.news_HTMLContent);
        assertNotEquals(-1, news.news_HTMLContent.indexOf("href"));

    }

    @Test
    public void getHeadlineList() throws Exception {
        ArrayList<Headline> headlines = dao.getHeadlineList(DAOParam.fromCategory(1, 0, 10))
                .map(new Function<ArrayList<Headline>, ArrayList<Headline>>() {
                    @Override
                    public ArrayList<Headline> apply(@NonNull ArrayList<Headline> headlines) throws Exception {
                        Log.i(TAG, "apply: getHeadlineList");
                        for (Headline headline :
                                headlines) {
                            dao.getNews(DAOParam.fromNewsId(headline.news_ID)).subscribe();
                        }
                        return headlines;
                    }
                })
                .repeat(3)
                .blockingLast();
        Log.i(TAG, "getHeadlineList: get All News="+dao.getNewsDao().getAll().blockingGet());
        Headline headline = dao.getHeadline(DAOParam.fromNewsId(headlines.get(0).news_ID)).blockingGet();
        Log.i(TAG, "getHeadlineList: headlinefirst="+headline);
        assertEquals(headlines.get(0).isVisited, true);

        headlines = dao.getHeadlineList(DAOParam.fromKeyword("杭州 江苏", 0, 10))
                .map(new Function<ArrayList<Headline>, ArrayList<Headline>>() {
                    @Override
                    public ArrayList<Headline> apply(@NonNull ArrayList<Headline> headlines) throws Exception {
                        Log.i(TAG, "apply: getHeadlineList");
                        for (Headline headline :
                                headlines) {
                            dao.getNews(DAOParam.fromNewsId(headline.news_ID)).subscribe();
                        }
                        return headlines;
                    }
                })
                .repeat(3)
                .blockingLast();
        Log.i(TAG, "getHeadlineList: headline="+headlines);
    }

    @Test
    public void loadHistory() throws Exception {
        for (News news : dao.getNewsDao().getAll().blockingGet()) {
            dao.getNewsDao().deleteNews(news);
        }
        ArrayList<Headline> headlines = dao.headlineObservable(DAOParam.fromCategory(1, 0, 10))
                .map(new Function<ArrayList<Headline>, ArrayList<Headline>>() {
                    @Override
                    public ArrayList<Headline> apply(@NonNull ArrayList<Headline> headlines) throws Exception {
                        Log.i(TAG, "apply: getHeadlineList");
                        for (Headline headline :
                                headlines) {
                            dao.getNews(DAOParam.fromNewsId(headline.news_ID)).subscribe();
                        }
                        return headlines;
                    }
                }).blockingGet();
        HashSet<String> hashset1 = new HashSet<>(), hashset2 = new HashSet<>();
        for (Headline headline : headlines) {
            Log.i(TAG, "loadHistory: headline"+headline);
            hashset1.add(headline.news_ID);
        }
        List<Headline> histories = dao.getHeadlineDao().loadHistory(0, 100).blockingGet();
        for (Headline history : histories) {
            Log.i(TAG, "loadHistory: history"+history);
            hashset2.add(history.news_ID);
        }
        assertEquals(hashset1, hashset2);
    }

    @Test
    public void headlineObservable() throws Exception {
        Single<ArrayList<Headline>> headlineObservable = dao.headlineObservable(DAOParam.fromCategory(1, 0, 10))
                .timeout(1, TimeUnit.SECONDS)
                .map(new Function<ArrayList<Headline>, ArrayList<Headline>>() {
                    @Override
                    public ArrayList<Headline> apply(@NonNull ArrayList<Headline> headlines) throws Exception {
                        Log.i(TAG, "After process: ");
                        return headlines;
                    }
                });
        Thread.sleep(2000);
        Log.i(TAG, "headlineObservable: first ten.");
        List<Headline> headlines = null;
        Disposable disposable = null;
        headlines = headlineObservable
                .blockingGet();

                    /*int i = 0;
                    @Override
                    public void accept(List<Headline> headlines) throws Exception {
                        Log.i(TAG, "headlineObservable: i=" + i);
                        for (Headline headline : headlines) {
                            Log.i(TAG, "headlineObservable: " + headline);
                        }

                    }
                });*/
        for (Headline headline: headlines) {
            Log.i(TAG, "headlineObservable: " + headline);
        }
        assertEquals(10, headlines.size());
        Log.i(TAG, "headlineObservable: second ten.");
        headlines = headlineObservable
                .blockingGet();

        /*
                headlineObservable.take(10)
                        .reduce(new ArrayList<Headline>(), new BiFunction<ArrayList<Headline>, Headline, ArrayList<Headline>>() {
                            @Override
                            public ArrayList<Headline> apply(@NonNull ArrayList<Headline> headlines, @NonNull Headline headline) throws Exception {
                                headlines.add(headline);
                                return headlines;
                            }
                        })
                        .blockingGet();*/
        for (Headline headline: headlines) {
            Log.i(TAG, "headlineObservable: " + headline);
        }
        assertEquals(10, headlines.size());
    }

    @Test
    public void star() throws Exception {
        dao.headlineObservable(DAOParam.fromCategory(1, 0, 10)).repeat(5).blockingSubscribe();
        Headline headline = dao.getHeadline(DAOParam.fromNewsId("201609130413080e91293fb5402b80437a65970fcb7d")).blockingGet();//may fail sometimes
        Log.i(TAG, "star: "+headline);
        assertEquals(headline.news_Title, "乐视921邀请函曝光 乐Pro 3悬疑即将揭晓");

        // get favorites (empty).
        ArrayList<Headline> headlines = dao.headlineObservable(DAOParam.fromCategory(DAOParam.FAVORITE, 0, 10)).blockingGet();
        assertEquals(headlines.size(), 0);

        // get news && headline (isFavorite == false)
        News news = dao.getNews(DAOParam.fromNewsId(headline.news_ID)).blockingGet();
        assertEquals(false, news.isFavorite);
        headline = dao.getHeadline(DAOParam.fromNewsId(headline.news_ID)).blockingGet();
        assertEquals(false, headline.isFavorite);

        // Star one piece of news
        dao.star(headline.news_ID).subscribe();
        Log.i(TAG, "star: all headlines="+dao.getHeadlineDao().getAll().blockingGet());

        // get favorites (one item)
        headlines = dao.headlineObservable(DAOParam.fromCategory(DAOParam.FAVORITE, 0, 10)).blockingGet();
        assertEquals(headlines.size(), 1);

        // get news && headline (isFavorite == true)
        news = dao.getNews(DAOParam.fromNewsId(headline.news_ID)).blockingGet();
        assertEquals(true, news.isFavorite);
        headline = dao.getHeadline(DAOParam.fromNewsId(headline.news_ID)).blockingGet();
        assertEquals(true, headline.isFavorite);

        // unstar
        dao.unStar(headline.news_ID).subscribe();

        // get favorites (empty).
        headlines = dao.headlineObservable(DAOParam.fromCategory(DAOParam.FAVORITE, 0, 10)).blockingGet();
        assertEquals(headlines.size(), 0);

        // get news && headline
        news = dao.getNews(DAOParam.fromNewsId(headline.news_ID)).blockingGet();
        assertEquals(false, news.isFavorite);
        headline = dao.getHeadline(DAOParam.fromNewsId(headline.news_ID)).blockingGet();
        assertEquals(false, headline.isFavorite);
    }

    @Test
    public void unStar() throws Exception {

    }

    @Test
    public void clearCache() throws Exception {

    }

    @Test
    public void setSettings() throws Exception {

        Preferences preferences = dao.getSettings();
        Log.i(TAG, "setSettings: "+preferences);
        int before = preferences.classTags;
        preferences.classTags += 1;
        assertEquals(dao.setSettings(preferences), true);
        preferences = dao.getSettings();
        Log.i(TAG, "setSettings: "+preferences);
        assertEquals(preferences.classTags, before + 1);
    }
}