package bignews.myapplication.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import bignews.myapplication.db.dao.HeadlineDao;
import bignews.myapplication.db.dao.KeywordDao;
import bignews.myapplication.db.dao.NewsDao;

/**
 * Created by lazycal on 2017/9/10.
 */

@Database(entities = {Headline.class, News.class, Keyword.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract HeadlineDao headlineDao();
    public abstract NewsDao newsDao();
    public abstract KeywordDao keywordDao();
}
