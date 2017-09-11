package bignews.myapplication.db;

import android.util.Log;

import java.util.ArrayList;

import bignews.myapplication.db.service.APIService;
import bignews.myapplication.db.service.HeadlineResponse;
import bignews.myapplication.utils.Tools;
import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;

/**
 * Created by lazycal on 2017/9/9.
 */

public class APICaller {

    private static APICaller instance;
    private APICaller() {}
    static APICaller getInstance()
    {
        if (instance == null) { instance = new APICaller(); }
        return instance;
    }
    Single<News> loadNews(final DAOParam param) {
        return Tools.getRetrofit()
                .create(APIService.class)
                .loadNews(param.newsID);
    }
    Single<ArrayList<Headline>> loadHeadlines(final DAOParam param) {
        return Tools.getRetrofit()
                .create(APIService.class)
                .loadHeadlines(1, param.limit + param.offset, param.category)
        /*Single.just(new HeadlineResponse())
                .map(new Function<HeadlineResponse, HeadlineResponse>() {
                    @Override
                    public HeadlineResponse apply(@NonNull HeadlineResponse headlineResponse) throws Exception {
                        headlineResponse.headlines = new ArrayList<Headline>();
                        for (int i = 0; i < param.offset + param.limit; ++i) {
                            Headline headline = new Headline();
                            headline.news_Title = "" + i;
                            headline.news_ID = "" + i;
                            headlineResponse.headlines.add(headline);
                        }
                        return headlineResponse;
                    }
                })*/
                .flattenAsObservable(new Function<HeadlineResponse, ArrayList<Headline>>() {
                    @Override
                    public ArrayList<Headline> apply(@NonNull HeadlineResponse headlineResponse) throws Exception {
                        return headlineResponse.headlines;
                    }
                })
                .skip(param.offset)
                .reduce(new ArrayList<Headline>(), new BiFunction<ArrayList<Headline>, Headline, ArrayList<Headline>>() {
                    @Override
                    public ArrayList<Headline> apply(@NonNull ArrayList<Headline> headlines, @NonNull Headline headline) throws Exception {
                        headlines.add(headline);
                        return headlines;
                    }
                });


                //subList(param.offset, param.offset + param.limit);
    }

    Single<ArrayList<Headline>> searchHeadlines(DAOParam param) {
        return Tools.getRetrofit()
                .create(APIService.class)
                .loadHeadlines(1, param.limit + param.offset, param.category, param.keywords)
                .flattenAsObservable(new Function<HeadlineResponse, ArrayList<Headline>>() {
                    public static final String TAG = "searchHeadlines";

                    @Override
                    public ArrayList<Headline> apply(@NonNull HeadlineResponse headlineResponse) throws Exception {
                        Log.i(TAG, "apply: "+headlineResponse);
                        return headlineResponse.headlines;
                    }
                })
                .skip(param.offset)
                .reduce(new ArrayList<Headline>(), new BiFunction<ArrayList<Headline>, Headline, ArrayList<Headline>>() {
                    @Override
                    public ArrayList<Headline> apply(@NonNull ArrayList<Headline> headlines, @NonNull Headline headline) throws Exception {
                        headlines.add(headline);
                        return headlines;
                    }
                });
    }
}
