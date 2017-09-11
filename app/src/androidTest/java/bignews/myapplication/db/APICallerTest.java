package bignews.myapplication.db;

import android.util.Log;

import org.junit.Test;

import java.util.ArrayList;

import bignews.myapplication.db.service.APIService;
import bignews.myapplication.db.service.HeadlineResponse;
import bignews.myapplication.utils.Tools;
import io.reactivex.SingleObserver;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;

import static org.junit.Assert.*;

/**
 * Created by lazycal on 2017/9/11.
 */
public class APICallerTest {
    public static final String TAG = "APICallerTest";
    @Test
    public void searchHeadlines() throws Exception {
        DAOParam param = DAOParam.fromKeyword("浙江 江苏", 0, 10);
        Log.i(TAG, "searchHeadlines: "+param);
        Tools.getRetrofit()
                .create(APIService.class)
                .loadHeadlines(1, 10, null, "浙江 江苏")
                .subscribe();
        Tools.getRetrofit()
                .create(APIService.class)
                .loadHeadlines(1, param.limit + param.offset, param.category, param.keywords)
                .flattenAsObservable(new Function<HeadlineResponse, ArrayList<Headline>>() {

                    @Override
                    public ArrayList<Headline> apply(@NonNull HeadlineResponse headlineResponse) throws Exception {
                        Log.i(TAG, "apply: " + headlineResponse);
                        return headlineResponse.headlines;
                    }
                })
                .subscribe();
        Tools.getRetrofit()
                .create(APIService.class)
                .loadHeadlines(1, param.limit + param.offset, param.category, param.keywords)
                .flattenAsObservable(new Function<HeadlineResponse, ArrayList<Headline>>() {

                    @Override
                    public ArrayList<Headline> apply(@NonNull HeadlineResponse headlineResponse) throws Exception {
                        Log.i(TAG, "apply: " + headlineResponse);
                        return headlineResponse.headlines;
                    }
                })
                .skip(param.offset)
                .subscribe();
        Tools.getRetrofit()
                .create(APIService.class)
                .loadHeadlines(1, param.limit + param.offset, param.category, param.keywords)
                .flattenAsObservable(new Function<HeadlineResponse, ArrayList<Headline>>() {

                    @Override
                    public ArrayList<Headline> apply(@NonNull HeadlineResponse headlineResponse) throws Exception {
                        Log.i(TAG, "apply: " + headlineResponse);
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
                })
                .blockingGet();
    }

    @Test
    public void stable() throws Exception {
        DAOParam param = DAOParam.fromKeyword("浙江 江苏", 0, 10);
        param.category = 0;
        for (int i = 0; i < 10; ++i)
        APICaller.getInstance().searchHeadlines(param)
                .subscribe(new SingleObserver<ArrayList<Headline>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull ArrayList<Headline> headlines) {

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.i(TAG, "onError: ", e);
                    }
                });

    }
}