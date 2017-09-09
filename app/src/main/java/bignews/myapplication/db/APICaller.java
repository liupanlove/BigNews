package bignews.myapplication.db;

import java.util.ArrayList;

import bignews.myapplication.db.service.APIService;
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
    Single<ArrayList<Headline>> loadHeadlines(DAOParam param) {
        return Tools.getRetrofit()
                .create(APIService.class)
                .loadHeadlines(0, param.limit + param.offset, param.category)
                .flattenAsObservable(new Function<ArrayList<Headline>, ArrayList<Headline>>() {
                    @Override
                    public ArrayList<Headline> apply(@NonNull ArrayList<Headline> headlines) throws Exception {
                        return headlines;
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
}
