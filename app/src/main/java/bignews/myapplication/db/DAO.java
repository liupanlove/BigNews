package bignews.myapplication.db;

import java.util.ArrayList;

import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Created by lazycal on 2017/9/7.
 */

public class DAO {
    private static final String TAG = "dao";
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
    private static DAO dao;

    private DAO() {}

    /**
     * Return a singleton.
     * @return the instance of dao
     */
    public synchronized static DAO getInstance() {
        if (dao == null) dao = new DAO();
        return dao;
    }

    /**
     * Get news body.
     * @param param parameter
     * @return The news body
     */
    public News getNews(final DAOParam param)
    {
        return Single.just(new News())
                .map(new Function<News, News>() {
                    @Override
                    public News apply(@NonNull News news) throws Exception {
                        news.newsTitle = ""+param.newsID;
                        return news;
                    }
                })
                .blockingGet();
    }

    /**
     * Get headline list.
     * User can specify @link{DAOParam#category}, @link{DAOParam#keywords}, @link{DAOParam#mode}
     * and so on.
     * @param param parameter
     * @return An ArrayList containing the headlines
     */
    public ArrayList<HeadLine> getNewsList(final DAOParam param)
    {
        /*
        cnt += 1;
        if (cnt % 5 == 0) try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "getNewsList: Before: "+Headlines);
        Headlines.add(String.valueOf(cnt));
        Log.i(TAG, "getNewsList: After: "+Headlines);
        return (ArrayList<String>) Headlines.clone();
        */
        return Single.just(new ArrayList<HeadLine>())
                .map(new Function<ArrayList<HeadLine>, ArrayList<HeadLine>>() {
                    @Override
                    public ArrayList<HeadLine> apply(@NonNull ArrayList<HeadLine> headLines) throws Exception {

                        for (int i = param.offset; i < param.offset + param.limit; ++i) {
                            HeadLine headline = new HeadLine();
                            headline.newsTitle = "title:" + i;
                            headline.newsClass = param.category + "";
                            headline.newsID = i + "";
                            headLines.add(headline);
                        }
                        return headLines;
                    }
                })
                .blockingGet();
    }

    /**
     * Add a piece of news into favorites
     * @param newsID news ID
     */
    public void star(int newsID) {}

    /**
     * Remove a piece of news from favorites
     * @param newsID news ID
     */
    public void unStar(int newsID) {}

    /**
     * Clear cached files
     */
    public void clearCache() {}


}
