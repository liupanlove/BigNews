package bignews.myapplication.db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by lazycal on 2017/9/9.
 */

@Entity
public class Headline {
    @PrimaryKey
    public String news_ID;
    public String newsClassTag, news_Category, news_Source, news_Title, news_Time, news_URL, news_Author, lang_Type,
            news_Pictures, news_Video, news_Intro;
    @Ignore
    public boolean isVisited;

    @Override
    public String toString() {
        return "Headline{" +
                "news_Title='" + news_Title + '\'' +
                "newsClassTag='" + newsClassTag + '\'' +
                '}';
    }
}
