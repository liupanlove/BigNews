package bignews.myapplication.db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

import java.util.ArrayList;

/**
 * Created by lazycal on 2017/9/9.
 */
@Entity
public class News extends Headline {
    //public String seggedTitle;
    @Ignore
    ArrayList<Person> persons;
    public String news_Content;
    public String news_HTMLContent;
    //ArrayList<String> persons;
    //public ArrayList<String> seggedPListOfContent;
    @Ignore
    public ArrayList<Keyword> Keywords;

    @Override
    public String toString() {
        return "News{" +
                super.toString() +
                "news_HTMLContent=" + news_HTMLContent +
                "news_Content=" + news_Content +
                '}';
    }
    static class Person implements Comparable {
        String word;
        int count;

        @Override
        public int compareTo(@NonNull Object o) {
            Person p = (Person)o;
            if (word.length() < p.word.length()) return -1;
            else if (word.length() > p.word.length()) return 1;
            else return word.compareTo(p.word);
        }
    }
}
