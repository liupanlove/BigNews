package bignews.myapplication.db;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.SynchronousQueue;

/**
 * Created by lazycal on 2017/9/7.
 */

public class Dao {
    private static final String TAG = "Dao";
    static int cnt = 0;
    private ArrayList<String> Headlines = new ArrayList<String>();
    {
        Headlines.add("Article One");
        Headlines.add("Article Two");
    };

    private String[] Articles = {
            "Article One\n\nExcepteur pour-over occaecat squid biodiesel umami gastropub, nulla laborum salvia dreamcatcher fanny pack. Ullamco culpa retro ea, trust fund excepteur eiusmod direct trade banksy nisi lo-fi cray messenger bag. Nesciunt esse carles selvage put a bird on it gluten-free, wes anderson ut trust fund twee occupy viral. Laboris small batch scenester pork belly, leggings ut farm-to-table aliquip yr nostrud iphone viral next level. Craft beer dreamcatcher pinterest truffaut ethnic, authentic brunch. Esse single-origin coffee banksy do next level tempor. Velit synth dreamcatcher, magna shoreditch in american apparel messenger bag narwhal PBR ennui farm-to-table.",
            "Article Two\n\nVinyl williamsburg non velit, master cleanse four loko banh mi. Enim kogi keytar trust fund pop-up portland gentrify. Non ea typewriter dolore deserunt Austin. Ad magna ethical kogi mixtape next level. Aliqua pork belly thundercats, ut pop-up tattooed dreamcatcher kogi accusamus photo booth irony portland. Semiotics brunch ut locavore irure, enim etsy laborum stumptown carles gentrify post-ironic cray. Butcher 3 wolf moon blog synth, vegan carles odd future."
    };
    private static Dao dao;

    private Dao() {}
    public synchronized static Dao getInstance() {
        if (dao == null) dao = new Dao();
        return dao;
    }
    public String getNews(DaoParameter param)
    {
        return Articles[param.id];
    }
    public synchronized ArrayList<String> getNewsList(DaoParameter param)
    {
        cnt += 1;
        if (cnt % 1000 == 0) try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "getNewsList: Before: "+Headlines);
        Headlines.add(String.valueOf(cnt));
        Log.i(TAG, "getNewsList: After: "+Headlines);
        return (ArrayList<String>) Headlines.clone();
    }
}
