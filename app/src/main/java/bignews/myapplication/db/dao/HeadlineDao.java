package bignews.myapplication.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Update;

import java.util.ArrayList;
import java.util.List;

import bignews.myapplication.db.DAOParam;
import bignews.myapplication.db.Headline;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by lazycal on 2017/9/9.
 */
@Dao
public interface HeadlineDao {
    @Query("select * FROM Headline order by news_Time")
    Single<List<Headline>> getAll();

    @Query("select * from Headline where newsClassTag = :newsClassTag  order by news_Time desc limit :limit offset :offset")
    Single<List<Headline>> load(String newsClassTag, int offset, int limit);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void addHeadline(Headline headline);

    @Update
    void updateHeadline(Headline headline);

    @Delete()
    void deleteHeadline(Headline headline);

    @Query("select * FROM Headline where news_Id = :newsID")
    Single<List<Headline>> findHeadlineByID(String newsID);

    @Query("select * FROM Headline where isFavorite = 1 order by news_Time desc limit :limit offset :offset")
    Single<List<Headline>> findHeadlineByFavorite(int offset, int limit);
}
