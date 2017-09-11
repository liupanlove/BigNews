package bignews.myapplication.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import bignews.myapplication.db.Keyword;
import io.reactivex.Single;

/**
 * Created by lazycal on 2017/9/10.
 */

@Dao
public interface KeywordDao {

    @Query("select * from Keyword order by score desc limit 1 ")
    Single<List<Keyword>> getHighFreqWord();

    @Query("select * from Keyword where word = :word limit 1 ")
    Single<List<Keyword>> findKeywordByText(String word);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addKeyword(Keyword keyword);
}
