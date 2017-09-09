package bignews.myapplication.db.service;

import java.util.ArrayList;

import bignews.myapplication.db.Headline;
import bignews.myapplication.db.News;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by lazycal on 2017/9/9.
 */

public interface APIService {
    @GET("latest")//?pageNo={pageNO}&pageSize={pageSize}&category={category}")
    Single<ArrayList<Headline>> loadHeadlines(@Query("pageNo") int pageNo,
                                              @Query("pageSize") int pageSize,
                                              @Query("category") int category);
    @GET("search")//?keyword={keyword}&pageNo={pageNo}&pageSize={pageSize}&category={category}")
    Single<ArrayList<Headline>> loadHeadlines(@Query("pageNo") int pageNo,
                                              @Query("pageSize") int pageSize,
                                              @Query("category") int category,
                                              @Query("keyword") String keyword);
    @GET("detail")//?newsId={newsId}")
    Single<News> loadNews(@Query("newsId") int newsID);
}
