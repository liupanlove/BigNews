package bignews.myapplication.db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.util.Date;

/**
 * Created by lazycal on 2017/9/9.
 */

@Entity(primaryKeys = {"news_ID", "newsClassTag"})
public class Headline {
    public String news_ID;
    public String newsClassTag, news_Category, news_Source, news_Title, news_URL, news_Author, lang_Type,
            news_Pictures, news_Video, news_Intro;
    @TypeConverters({Converters.class})
    public String news_Time;
    public boolean isFavorite;

    @Override
    public String toString() {
        return "Headline{" +
                "news_ID='" + news_ID + '\'' +
                ", newsClassTag='" + newsClassTag + '\'' +
                ", news_Title='" + news_Title + '\'' +
                ", isFavorite=" + isFavorite +
                ", isVisited=" + isVisited +
                '}';
    }

    @Ignore
    public boolean isVisited;

}
