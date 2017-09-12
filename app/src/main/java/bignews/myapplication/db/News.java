package bignews.myapplication.db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.support.v4.util.Pair;

import java.util.ArrayList;

/**
 * Created by lazycal on 2017/9/9.
 */
@Entity
public class News extends Headline {
    public String seggedTitle;
    @Ignore
    public ArrayList<String> seggedPListOfContent;
    public String news_Content;
    //ArrayList<String> persons;
    @Ignore
    public ArrayList<Keyword> Keywords;

    @Override
    public String toString() {
        return "News{" +
                super.toString() +
                "Keywords=" + Keywords +
                '}';
    }
}
