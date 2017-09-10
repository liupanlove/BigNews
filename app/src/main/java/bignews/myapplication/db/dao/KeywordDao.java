package bignews.myapplication.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import bignews.myapplication.db.Keyword;

/**
 * Created by lazycal on 2017/9/10.
 */

@Dao
public interface KeywordDao {

    @Query("select * from Keyword order by score desc limit 1 ")
    Keyword getHighFreqWord();

    @Insert
    void addKeyword(Keyword keyword);
}
