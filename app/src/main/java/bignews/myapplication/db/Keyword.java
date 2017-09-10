package bignews.myapplication.db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by lazycal on 2017/9/10.
 */
@Entity
public class Keyword {
    @PrimaryKey
    public Integer id;
    public Double score;
    public String text;
}
