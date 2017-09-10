package bignews.myapplication.db;

import android.support.v4.util.Pair;

import java.util.ArrayList;

/**
 * Created by lazycal on 2017/9/9.
 */

public class News extends Headline {
    public String seggedTitle;
    public ArrayList<String> seggedPListOfContent;
    //ArrayList<String> persons;
    public ArrayList<Pair<String, Double>> keywords;
}
