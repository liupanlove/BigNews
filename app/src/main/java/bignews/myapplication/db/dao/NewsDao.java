package bignews.myapplication.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import bignews.myapplication.db.News;

/**
 * Created by lazycal on 2017/9/10.
 */
@Dao
public interface NewsDao {

    @Query("select * FROM News")
    List<News> getAll();

    @Query("select * from News where newsClassTag = :category limit :limit offset :offset")
    List<News> load(int category, int offset, int limit);

    @Query("select * from News where news_id = :newsID")
    News findByID(int newsID);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addNews(News news);

    @Delete()
    void deleteNews(News news);
}
