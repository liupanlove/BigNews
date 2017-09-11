package bignews.myapplication.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by lazycal on 2017/9/9.
 */

public class Tools {
    private static final String API_URL = "http://166.111.68.66:2042/news/action/query/";
    private static Retrofit retrofit;
    private static Gson gson;
    public static Retrofit getRetrofit() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(API_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(getGson()))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static Gson getGson() {
        return
                new GsonBuilder()
                        //序列化null
                        .serializeNulls()
                        // 设置日期时间格式，另有2个重载方法
                        // 在序列化和反序化时均生效
                        //.setDateFormat("yyyyMMddhhmmss")
                        // 禁此序列化内部类
                        .disableInnerClassSerialization()
                        //生成不可执行的Json（多了 )]}' 这4个字符）
                        .generateNonExecutableJson()
                        //禁止转义html标签
                        .disableHtmlEscaping()
                        //格式化输出
                        .setPrettyPrinting()
                        .create();
    }
}
