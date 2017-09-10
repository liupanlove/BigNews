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

import java.io.IOError;
import java.io.IOException;
import java.util.List;

import bignews.myapplication.db.dao.HeadlineDao;
import bignews.myapplication.db.dao.KeywordDao;
import bignews.myapplication.db.dao.NewsDao;

import static org.junit.Assert.*;

/**
 * Created by lazycal on 2017/9/10.
 */
@RunWith(AndroidJUnit4.class)
public class AppDatabaseTest {
    private static final String TAG = "AppDatabaseTest";
    private NewsDao newsDao;
    private HeadlineDao headlineDao;
    private KeywordDao keywordDao;
    private AppDatabase mDb;
    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getTargetContext();
        mDb = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        headlineDao = mDb.headlineDao();
        newsDao = mDb.newsDao();
        keywordDao = mDb.keywordDao();

    }
    @After
    public void closeDb() throws IOException {
        mDb.close();
    }

    @Test
    public void headlineDao() throws Exception {
        Log.i(TAG, "headlineDao: ");
        Headline headline = new Headline();
        headline.news_Title = "first";
        headline.news_ID = "firstID";
        headline.newsClassTag = "classTag";
        headlineDao.addHeadline(headline);
        List<Headline> list = headlineDao.load("classTag", 0, 1);
        assertEquals(list.size(), 1);
        Headline fetched = list.get(0);
        assertEquals(fetched.news_Title, headline.news_Title);
        assertEquals(fetched.news_ID, headline.news_ID);
        assertEquals(fetched.newsClassTag, headline.newsClassTag);
    }

    @Test
    public void newsDao() throws Exception {

    }

    @Test
    public void keywordDao() throws Exception {

    }

}