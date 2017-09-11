package bignews.myapplication.db.service;

import android.net.http.HttpResponseCache;
import android.util.Log;

import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;

import bignews.myapplication.db.Headline;
import bignews.myapplication.utils.Tools;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static org.junit.Assert.*;

/**
 * Created by lazycal on 2017/9/10.
 */
class FakeInterceptor implements Interceptor {
    // FAKE RESPONSES.
    private final static String TEACHER_ID_1 = "{\"id\":1,\"age\":28,\"name\":\"Victor Apoyan\"}";
    private final static String TEACHER_ID_2 = "{\"id\":1,\"age\":16,\"name\":\"Tovmas Apoyan\"}";
    private static final String TAG = "FakeInterceptor";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = null;
        /*
        if(BuildConfig.DEBUG) {
            String responseString;
            // Get Request URI.
            final URI uri = chain.request().uri();
            // Get Query String.
            final String query = uri.getQuery();
            // Parse the Query String.
            final String[] parsedQuery = query.split("=");
            if(parsedQuery[0].equalsIgnoreCase("id") && parsedQuery[1].equalsIgnoreCase("1")) {
                responseString = TEACHER_ID_1;
            }
            else if(parsedQuery[0].equalsIgnoreCase("id") && parsedQuery[1].equalsIgnoreCase("2")){
                responseString = TEACHER_ID_2;
            }
            else {
                responseString = "";
            }

            response = new Response.Builder()
                    .code(200)
                    .message(responseString)
                    .request(chain.request())
                    .protocol(Protocol.HTTP_1_0)
                    .body(ResponseBody.create(MediaType.parse("application/json"), responseString.getBytes()))
                    .addHeader("content-type", "application/json")
                    .build();
        }
        else {
            response = chain.proceed(chain.request());
        }
*/
        response = chain.proceed(chain.request());
        Log.d(TAG, "intercept: "+response);
        return response;
    }
}
public class APIServiceTest {
    private static final String TAG = "APIServiceTest";
    private Retrofit retrofit;
    @Before
    public void init()
    {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        retrofit = Tools.getRetrofit();
    }
    @Test
    public void gsonTest() throws Exception {
        String response = "{\"list\":[{\"lang_Type\":\"zh-CN\",\"newsClassTag\":\"科技\",\"news_Author\":\"创事记 微博 作者： 广州阿超\",\"news_ID\":\"20160913041301d5fc6a41214a149cd8a0581d3a014f\",\"news_Pictures\":\"\",\"news_Source\":\"新浪新闻\",\"news_Time\":\"20160912000000\",\"news_Title\":\"iPhone 7归来，友商们吊打苹果的姿势正确吗？\",\"news_URL\":\"http://tech.sina.com.cn/zl/post/detail/mobile/2016-09-12/pid_8508491.htm\",\"news_Video\":\"\",\"news_Intro\":\"　　欢迎关注“创事记”的微信订阅号：sinachuangshiji 文/罗超...\"},{\"lang_Type\":\"zh-CN\",\"newsClassTag\":\"科技\",\"news_Author\":\"环球网\",\"news_ID\":\"201609130413080e91293fb5402b80437a65970fcb7d\",\"news_Pictures\":\"http://himg2.huanqiu.com/attachment2010/2016/0912/13/16/20160912011621140.png;http://himg2.huanqiu.com/attachment2010/2016/0912/13/16/20160912011630240.png;http://himg2.huanqiu.com/attachment2010/2016/0912/13/16/20160912011642992.png;http://himg2.huanqiu.com/attachment2010/2016/0912/13/16/20160912011655316.png;http://himg2.huanqiu.com/attachment2010/2016/0912/13/17/20160912011703267.png;http://himg2.huanqiu.com/statics/images/more-icoCopy.png\",\"news_Source\":\"环球网\",\"news_Time\":\"20160912000000\",\"news_Title\":\"乐视921邀请函曝光 乐Pro 3悬疑即将揭晓\",\"news_URL\":\"http://tech.huanqiu.com/diginews/2016-09/9432234.html\",\"news_Video\":\"\",\"news_Intro\":\"乐视921邀请函曝光 乐Pro 3悬疑即将揭晓 2016-09-12 13:...\"},{\"lang_Type\":\"zh-CN\",\"newsClassTag\":\"科技\",\"news_Author\":\"中国网\",\"news_ID\":\"201609130413037a073b2cdb4768aff90eff46f2665b\",\"news_Pictures\":\"http://imge.gmw.cn/attachement/png/site2/20160912/f44d305ea5951940d3fd4f.png;http://imge.gmw.cn/attachement/png/site2/20160912/f44d305ea5951940d3fd50.png;http://imge.gmw.cn/attachement/png/site2/20160912/f44d305ea5951940d3fd51.png;http://imge.gmw.cn/attachement/png/site2/20160912/f44d305ea5951940d3fd52.png;http://imge.gmw.cn/attachement/png/site2/20160912/f44d305ea5951940d3fd53.png\",\"news_Source\":\"光明网\",\"news_Time\":\"20160912000000\",\"news_Title\":\"吴亦凡亮相荣耀极光派对 全新定义不凡潮品荣耀8\",\"news_URL\":\"http://e.gmw.cn/2016-09/12/content_21944565.htm\",\"news_Video\":\"\",\"news_Intro\":\"　　9月9日，一场科技撞击潮流的派对在北京举行。荣耀品牌携手吴亦凡华丽亮相荣...\"},{\"lang_Type\":\"zh-CN\",\"newsClassTag\":\"科技\",\"news_Author\":\"\",\"news_ID\":\"2016091304131a39c78c43b047b6b64b7a13abc81305\",\"news_Pictures\":\"\",\"news_Source\":\"金融界\",\"news_Time\":\"20160912000000\",\"news_Title\":\"税网用上新技术 用户体验“蛮好咯”\",\"news_URL\":\"http://finance.jrj.com.cn/2016/09/12145021447520.shtml\",\"news_Video\":\"\",\"news_Intro\":\"　　近日,从事代理记账工作的小王用手机浏览上海税务网,突然发现变化很大:阅读...\"},{\"lang_Type\":\"zh-CN\",\"newsClassTag\":\"科技\",\"news_Author\":\"环球网\",\"news_ID\":\"2016091304131732ac05cc9d4c41bd93856dfa85509c\",\"news_Pictures\":\"http://upload.qianlong.com/2016/0912/1473646149318.jpeg http://upload.qianlong.com/2016/0912/1473674371218.jpeg\",\"news_Source\":\"其他\",\"news_Time\":\"20160912000000\",\"news_Title\":\"美运营商重启对苹果手机补贴 iPhone 6换iPhone 7\",\"news_URL\":\"http://tech.qianlong.com/2016/0912/923008.shtml\",\"news_Video\":\"\",\"news_Intro\":\"据美国《华尔街日报》9月12日报道，美国移动运营商花费了数年时间才推动用户以...\"},{\"lang_Type\":\"zh-CN\",\"newsClassTag\":\"科技\",\"news_Author\":\"中国新闻网\",\"news_ID\":\"20160913041320c710ffede54b4fbdb45e8e61388171\",\"news_Pictures\":\"\",\"news_Source\":\"其他\",\"news_Time\":\"20160912000000\",\"news_Title\":\"iPhone7遭吐槽 网友称买天价机不如出国旅游\",\"news_URL\":\"http://news.dayoo.com/society/201609/12/140000_50211513.htm\",\"news_Video\":\"\",\"news_Intro\":\"日盼夜盼，全球果粉们终于盼来了9月7日iPhone7的正式发布。然而iPho...\"},{\"lang_Type\":\"zh-CN\",\"newsClassTag\":\"科技\",\"news_Author\":\"环球网\",\"news_ID\":\"2016091304130bb6d594c82e47a7a5b088602f8fb912\",\"news_Pictures\":\"http://upload.qianlong.com/2016/0912/1473642904882.jpg\",\"news_Source\":\"其他\",\"news_Time\":\"20160912000000\",\"news_Title\":\"网曝10万部锤子T3全部返厂 官方愤怒否认\",\"news_URL\":\"http://tech.qianlong.com/2016/0912/922107.shtml\",\"news_Video\":\"\",\"news_Intro\":\"各大手机厂商热热闹闹轮番发布新品，罗永浩的锤子却异常淡定，新旗舰T3迟迟不来...\"},{\"lang_Type\":\"zh-CN\",\"newsClassTag\":\"科技\",\"news_Author\":\"京华时报\",\"news_ID\":\"2016091304130b71f06f6a924b148ee113eea5bb2d97\",\"news_Pictures\":\"\",\"news_Source\":\"新华网\",\"news_Time\":\"20160912000000\",\"news_Title\":\"三星手机频“爆炸” 美官方发布警告 建议停用\",\"news_URL\":\"http://sh.xinhuanet.com/2016-09/12/c_135681414.htm\",\"news_Video\":\"\",\"news_Intro\":\"　　 据新华社电美国消费产品安全委员会9日发布消费者警告，敦促停止使用三星盖...\"},{\"lang_Type\":\"zh-CN\",\"newsClassTag\":\"科技\",\"news_Author\":\"新浪网\",\"news_ID\":\"2016091304132a94c8ed99814a0ab3546612e762939c\",\"news_Pictures\":\"\",\"news_Source\":\"其他\",\"news_Time\":\"20160912000000\",\"news_Title\":\"三星召回Note 7后续：备用机是入门级Galaxy J系列\",\"news_URL\":\"http://tech.qianlong.com/2016/0912/921578.shtml\",\"news_Video\":\"\",\"news_Intro\":\"目前，三星正在多个国家和地区召回电池有爆炸隐患的Galaxy Note 7，...\"},{\"lang_Type\":\"zh-CN\",\"newsClassTag\":\"科技\",\"news_Author\":\"\",\"news_ID\":\"20160913041321ba5bd225b24a95a193bf01aad928f9\",\"news_Pictures\":\"\",\"news_Source\":\"金融界\",\"news_Time\":\"20160912000000\",\"news_Title\":\"李彦宏剑桥演讲再谈“下一幕”：人工智能将颠覆更多行业\",\"news_URL\":\"http://finance.jrj.com.cn/tech/2016/09/12100321445659.shtml\",\"news_Video\":\"\",\"news_Intro\":\"　　李彦宏认为移动互联网的时代正在成为过去，今年以来，互联网开启了“人工智能...\"}],\"pageNo\":1,\"pageSize\":10,\"totalPages\":2947,\"totalRecords\":29467}";
        HeadlineResponse hlr = Tools.getGson().fromJson(response, HeadlineResponse.class);
        assertNotNull("HeadlineResponse is NULL", hlr);
        assertNotNull("Headlines is NULL", hlr.headlines);
        Headline first = hlr.headlines.get(0);
        Log.i(TAG, "gsonTest: "+first);
        assertEquals(first.news_ID, "20160913041301d5fc6a41214a149cd8a0581d3a014f");
        //assertEquals(first.news_Time, new SimpleDateFormat("yyyyMMddhhmmss").parse("20160912000000"));
    }
    @Test
    public void loadHeadlines_okhttp() throws Exception {
        Call<ResponseBody> call = retrofit.create(APIService.class)
                .loadHeadlines_okhttp(1, 10, 1);
        retrofit2.Response<ResponseBody> body = call.execute();
        String res = body.body().string();
        Log.i(TAG, "loadHeadlines_okhttp: "+res);
        String response1 = "{\"list\":[{\"lang_Type\":\"zh-CN\",\"newsClassTag\":\"科技\",\"news_Author\":\"创事记 微博 作者： 广州阿超\",\"news_ID\":\"20160913041301d5fc6a41214a149cd8a0581d3a014f\",\"news_Pictures\":\"\",\"news_Source\":\"新浪新闻\",\"news_Time\":\"20160912000000\",\"news_Title\":\"iPhone 7归来，友商们吊打苹果的姿势正确吗？\",\"news_URL\":\"http://tech.sina.com.cn/zl/post/detail/mobile/2016-09-12/pid_8508491.htm\",\"news_Video\":\"\",\"news_Intro\":\"　　欢迎关注“创事记”的微信订阅号：sinachuangshiji 文/罗超...\"},{\"lang_Type\":\"zh-CN\",\"newsClassTag\":\"科技\",\"news_Author\":\"环球网\",\"news_ID\":\"201609130413080e91293fb5402b80437a65970fcb7d\",\"news_Pictures\":\"http://himg2.huanqiu.com/attachment2010/2016/0912/13/16/20160912011621140.png;http://himg2.huanqiu.com/attachment2010/2016/0912/13/16/20160912011630240.png;http://himg2.huanqiu.com/attachment2010/2016/0912/13/16/20160912011642992.png;http://himg2.huanqiu.com/attachment2010/2016/0912/13/16/20160912011655316.png;http://himg2.huanqiu.com/attachment2010/2016/0912/13/17/20160912011703267.png;http://himg2.huanqiu.com/statics/images/more-icoCopy.png\",\"news_Source\":\"环球网\",\"news_Time\":\"20160912000000\",\"news_Title\":\"乐视921邀请函曝光 乐Pro 3悬疑即将揭晓\",\"news_URL\":\"http://tech.huanqiu.com/diginews/2016-09/9432234.html\",\"news_Video\":\"\",\"news_Intro\":\"乐视921邀请函曝光 乐Pro 3悬疑即将揭晓 2016-09-12 13:...\"},{\"lang_Type\":\"zh-CN\",\"newsClassTag\":\"科技\",\"news_Author\":\"中国网\",\"news_ID\":\"201609130413037a073b2cdb4768aff90eff46f2665b\",\"news_Pictures\":\"http://imge.gmw.cn/attachement/png/site2/20160912/f44d305ea5951940d3fd4f.png;http://imge.gmw.cn/attachement/png/site2/20160912/f44d305ea5951940d3fd50.png;http://imge.gmw.cn/attachement/png/site2/20160912/f44d305ea5951940d3fd51.png;http://imge.gmw.cn/attachement/png/site2/20160912/f44d305ea5951940d3fd52.png;http://imge.gmw.cn/attachement/png/site2/20160912/f44d305ea5951940d3fd53.png\",\"news_Source\":\"光明网\",\"news_Time\":\"20160912000000\",\"news_Title\":\"吴亦凡亮相荣耀极光派对 全新定义不凡潮品荣耀8\",\"news_URL\":\"http://e.gmw.cn/2016-09/12/content_21944565.htm\",\"news_Video\":\"\",\"news_Intro\":\"　　9月9日，一场科技撞击潮流的派对在北京举行。荣耀品牌携手吴亦凡华丽亮相荣...\"},{\"lang_Type\":\"zh-CN\",\"newsClassTag\":\"科技\",\"news_Author\":\"\",\"news_ID\":\"2016091304131a39c78c43b047b6b64b7a13abc81305\",\"news_Pictures\":\"\",\"news_Source\":\"金融界\",\"news_Time\":\"20160912000000\",\"news_Title\":\"税网用上新技术 用户体验“蛮好咯”\",\"news_URL\":\"http://finance.jrj.com.cn/2016/09/12145021447520.shtml\",\"news_Video\":\"\",\"news_Intro\":\"　　近日,从事代理记账工作的小王用手机浏览上海税务网,突然发现变化很大:阅读...\"},{\"lang_Type\":\"zh-CN\",\"newsClassTag\":\"科技\",\"news_Author\":\"环球网\",\"news_ID\":\"2016091304131732ac05cc9d4c41bd93856dfa85509c\",\"news_Pictures\":\"http://upload.qianlong.com/2016/0912/1473646149318.jpeg http://upload.qianlong.com/2016/0912/1473674371218.jpeg\",\"news_Source\":\"其他\",\"news_Time\":\"20160912000000\",\"news_Title\":\"美运营商重启对苹果手机补贴 iPhone 6换iPhone 7\",\"news_URL\":\"http://tech.qianlong.com/2016/0912/923008.shtml\",\"news_Video\":\"\",\"news_Intro\":\"据美国《华尔街日报》9月12日报道，美国移动运营商花费了数年时间才推动用户以...\"},{\"lang_Type\":\"zh-CN\",\"newsClassTag\":\"科技\",\"news_Author\":\"中国新闻网\",\"news_ID\":\"20160913041320c710ffede54b4fbdb45e8e61388171\",\"news_Pictures\":\"\",\"news_Source\":\"其他\",\"news_Time\":\"20160912000000\",\"news_Title\":\"iPhone7遭吐槽 网友称买天价机不如出国旅游\",\"news_URL\":\"http://news.dayoo.com/society/201609/12/140000_50211513.htm\",\"news_Video\":\"\",\"news_Intro\":\"日盼夜盼，全球果粉们终于盼来了9月7日iPhone7的正式发布。然而iPho...\"},{\"lang_Type\":\"zh-CN\",\"newsClassTag\":\"科技\",\"news_Author\":\"环球网\",\"news_ID\":\"2016091304130bb6d594c82e47a7a5b088602f8fb912\",\"news_Pictures\":\"http://upload.qianlong.com/2016/0912/1473642904882.jpg\",\"news_Source\":\"其他\",\"news_Time\":\"20160912000000\",\"news_Title\":\"网曝10万部锤子T3全部返厂 官方愤怒否认\",\"news_URL\":\"http://tech.qianlong.com/2016/0912/922107.shtml\",\"news_Video\":\"\",\"news_Intro\":\"各大手机厂商热热闹闹轮番发布新品，罗永浩的锤子却异常淡定，新旗舰T3迟迟不来...\"},{\"lang_Type\":\"zh-CN\",\"newsClassTag\":\"科技\",\"news_Author\":\"京华时报\",\"news_ID\":\"2016091304130b71f06f6a924b148ee113eea5bb2d97\",\"news_Pictures\":\"\",\"news_Source\":\"新华网\",\"news_Time\":\"20160912000000\",\"news_Title\":\"三星手机频“爆炸” 美官方发布警告 建议停用\",\"news_URL\":\"http://sh.xinhuanet.com/2016-09/12/c_135681414.htm\",\"news_Video\":\"\",\"news_Intro\":\"　　 据新华社电美国消费产品安全委员会9日发布消费者警告，敦促停止使用三星盖...\"},{\"lang_Type\":\"zh-CN\",\"newsClassTag\":\"科技\",\"news_Author\":\"新浪网\",\"news_ID\":\"2016091304132a94c8ed99814a0ab3546612e762939c\",\"news_Pictures\":\"\",\"news_Source\":\"其他\",\"news_Time\":\"20160912000000\",\"news_Title\":\"三星召回Note 7后续：备用机是入门级Galaxy J系列\",\"news_URL\":\"http://tech.qianlong.com/2016/0912/921578.shtml\",\"news_Video\":\"\",\"news_Intro\":\"目前，三星正在多个国家和地区召回电池有爆炸隐患的Galaxy Note 7，...\"},{\"lang_Type\":\"zh-CN\",\"newsClassTag\":\"科技\",\"news_Author\":\"\",\"news_ID\":\"20160913041321ba5bd225b24a95a193bf01aad928f9\",\"news_Pictures\":\"\",\"news_Source\":\"金融界\",\"news_Time\":\"20160912000000\",\"news_Title\":\"李彦宏剑桥演讲再谈“下一幕”：人工智能将颠覆更多行业\",\"news_URL\":\"http://finance.jrj.com.cn/tech/2016/09/12100321445659.shtml\",\"news_Video\":\"\",\"news_Intro\":\"　　李彦宏认为移动互联网的时代正在成为过去，今年以来，互联网开启了“人工智能...\"}],\"pageNo\":1,\"pageSize\":10,\"totalPages\":2947,\"totalRecords\":29467}";
        String response2 = "{\"list\":[{\"lang_Type\":\"zh-CN\",\"newsClassTag\":\"科技\",\"news_Author\":\"人民网\",\"news_ID\":\"2016091304130fa2b951a9994d4ab5e4b938a0eef205\",\"news_Pictures\":\"http://upload.qianlong.com/2016/0912/1473661206139.jpg http://upload.qianlong.com/2016/0912/1473665750616.jpg\",\"news_Source\":\"其他\",\"news_Time\":\"20160912000000\",\"news_Title\":\"《口袋妖怪GO》重拳出击打击作弊 不再支持越狱设备\",\"news_URL\":\"http://tech.qianlong.com/2016/0912/924162.shtml\",\"news_Video\":\"\",\"news_Intro\":\"9月12日消息，据外媒报道，作为《口袋妖怪GO》开发商之一的Niantic游...\"},{\"lang_Type\":\"zh-CN\",\"newsClassTag\":\"科技\",\"news_Author\":\"来源：新浪科技\",\"news_ID\":\"201609130413031846254b4248b3b2208d6e51547011\",\"news_Pictures\":\"http://img.gmw.cn/pic/content_logo.png\",\"news_Source\":\"光明网\",\"news_Time\":\"20160912000000\",\"news_Title\":\"三星Note 7爆炸致纽约6岁男孩受伤\",\"news_URL\":\"http://tech.gmw.cn/2016-09/12/content_21945559.htm\",\"news_Video\":\"\",\"news_Intro\":\"　　新浪科技讯 北京时间9月12日晚间消息，据《纽约邮报》报道，正在召回的三...\"},{\"lang_Type\":\"zh-CN\",\"newsClassTag\":\"科技\",\"news_Author\":\"来源：中国新闻网\",\"news_ID\":\"201609130413104c2206d36d44edae764746137875b1\",\"news_Pictures\":\"\",\"news_Source\":\"其他\",\"news_Time\":\"20160912000000\",\"news_Title\":\"场景化改造促消费者回流实体 国美“超级福利日”大卖50亿\",\"news_URL\":\"http://www.chinanews.com/cj/2016/09-12/8002075.shtml\",\"news_Video\":\"\",\"news_Intro\":\"　　谈起过去北方人抢购冬储大白菜的热闹场面，估计好多年轻人没有印象，不过，9...\"},{\"lang_Type\":\"zh-CN\",\"newsClassTag\":\"科技\",\"news_Author\":\"北京商报\",\"news_ID\":\"201609130413270d7f7215bb41929706eddc4df3a237\",\"news_Pictures\":\"http://p1.ifengimg.com/a/2016_38/00a0f099bcdd5b4_size26_w636_h420.jpg\",\"news_Source\":\"凤凰网\",\"news_Time\":\"20160912000000\",\"news_Title\":\"网易邮箱仍有违规广告\",\"news_URL\":\"http://tech.ifeng.com/a/20160912/44450361_0.shtml\",\"news_Video\":\"\",\"news_Intro\":\"不做明显“广告”标识的现象在新浪邮箱中也有少量出现，搜狐邮箱中有一些应用推荐...\"},{\"lang_Type\":\"zh-CN\",\"newsClassTag\":\"科技\",\"news_Author\":\"网易科技报道\",\"news_ID\":\"2016091304131fa22f9e7cd443849f4f2d1b5f938c5b\",\"news_Pictures\":\"http://himg2.huanqiu.com/attachment2010/2016/0912/09/58/20160912095857940.jpeg;http://cms-bucket.nosdn.127.net/603f4f0bf39043f3bfe9cc1e2c81524420160912080557.jpeg?imageView&thumbnail=550x0\",\"news_Source\":\"环球网\",\"news_Time\":\"20160912000000\",\"news_Title\":\"外媒调侃 苹果还会“咔嚓掉”什么线缆\",\"news_URL\":\"http://tech.huanqiu.com/news/2016-09/9430162.html\",\"news_Video\":\"\",\"news_Intro\":\"外媒调侃 苹果还会“咔嚓掉”什么线缆 2016-09-12 08:28:00...\"},{\"lang_Type\":\"zh-CN\",\"newsClassTag\":\"科技\",\"news_Author\":\"\",\"news_ID\":\"20160913041312a7260c0c264a77b4a81c11c669f802\",\"news_Pictures\":\"\",\"news_Source\":\"金融界\",\"news_Time\":\"20160912000000\",\"news_Title\":\"美国运营商重启补贴模式 iPhone6系列可免费换iPhone7\",\"news_URL\":\"http://finance.jrj.com.cn/tech/2016/09/12145621447558.shtml\",\"news_Video\":\"\",\"news_Intro\":\"　　9月12日消息，据《华尔街日报》报道，美国移动运营商花费了数年时间才推动...\"},{\"lang_Type\":\"zh-CN\",\"newsClassTag\":\"科技\",\"news_Author\":\"中国新闻网\",\"news_ID\":\"2016091304131dd4d119bfa14bb0a2e073d0b0c4b78e\",\"news_Pictures\":\"\",\"news_Source\":\"其他\",\"news_Time\":\"20160912000000\",\"news_Title\":\"贵15% 澳大利亚买iPhone7要比美国多交钱\",\"news_URL\":\"http://news.dayoo.com/world/201609/12/139998_50211537.htm\",\"news_Video\":\"\",\"news_Intro\":\"中新网9月12日电 据澳洲网报道，苹果公司的iPhone7于近日上市，“果粉...\"},{\"lang_Type\":\"zh-CN\",\"newsClassTag\":\"科技\",\"news_Author\":\"新浪网\",\"news_ID\":\"2016091304132ef74ee0256847d28f4212ddca84fb26\",\"news_Pictures\":\"http://upload.qianlong.com/2016/0912/1473650915446.jpg\",\"news_Source\":\"其他\",\"news_Time\":\"20160912000000\",\"news_Title\":\"索尼公布PS4 Pro游戏专属标识 首批支持游戏公布\",\"news_URL\":\"http://tech.qianlong.com/2016/0912/923619.shtml\",\"news_Video\":\"\",\"news_Intro\":\"索尼的PS4 Pro的将兼容以前发售的PS4游戏，所以各大厂商纷纷瞄准了这台...\"},{\"lang_Type\":\"zh-CN\",\"newsClassTag\":\"科技\",\"news_Author\":\"中关村在线\",\"news_ID\":\"20160913041337970c2ada674069830bd2677e2f4816\",\"news_Pictures\":\"http://n.sinaimg.cn/tech/crawl/20160912/gu8U-fxvukhv8152360.jpg;http://n.sinaimg.cn/tech/crawl/20160912/8KUe-fxvukhx4894059.jpg;http://n.sinaimg.cn/8ee96216/20150312/weixin_apple.jpg\",\"news_Source\":\"新浪新闻\",\"news_Time\":\"20160912000000\",\"news_Title\":\"太火？iPhone7 Plus预约自提渠道被关闭\",\"news_URL\":\"http://tech.sina.com.cn/mobile/n/n/2016-09-12/doc-ifxvukhv8152567.shtml\",\"news_Video\":\"\",\"news_Intro\":\"　　现在无论你在苹果官网上订购iPhone7哪一个型号，发货时间最少都是一到...\"},{\"lang_Type\":\"zh-CN\",\"newsClassTag\":\"科技\",\"news_Author\":\"新浪网\",\"news_ID\":\"2016091304131c1376b3db72473fa06b633c0a3d1140\",\"news_Pictures\":\"http://upload.qianlong.com/2016/0912/1473673543585.jpg\",\"news_Source\":\"其他\",\"news_Time\":\"20160912000000\",\"news_Title\":\"赶制苹果iPhone 7，富士康启用1万台机器人\",\"news_URL\":\"http://tech.qianlong.com/2016/0912/925246.shtml\",\"news_Video\":\"\",\"news_Intro\":\"据报道，为了提高苹果iPhone 7的产能，代工商富士康在其组装车间一共部署...\"}],\"pageNo\":1,\"pageSize\":10,\"totalPages\":2947,\"totalRecords\":29467}";
        if (!res.equals(response1) && !res.equals(response2))
            assertEquals(res, response1);
    }

    @Test
    public void loadHeadlines() throws Exception {
        HeadlineResponse response = retrofit.create(APIService.class)
                .loadHeadlines(1, 10, 1)
                .blockingGet();
        for (Headline hl : response.headlines)
            Log.d(TAG, "loadHeadlines: "+hl);
    }

    @Test
    public void searchHeadlines() throws Exception {
        HeadlineResponse response = retrofit.create(APIService.class)
                .loadHeadlines(1, 10, null, "浙江 江苏")
                .blockingGet();
        Log.i(TAG, "searchHeadlines: response="+response);
        for (Headline hl : response.headlines)
            Log.d(TAG, "searchHeadlines: "+hl);


    }
    @Test
    public void searchHeadlines_okhttp() throws Exception {
        Call<ResponseBody> call = retrofit.create(APIService.class)
                .loadHeadlines_okhttp(1, 10, null, "浙江 江苏");
        retrofit2.Response<ResponseBody> body = call.execute();
        String res = body.body().string();
        Log.i(TAG, "searchHeadlines_okhttp: "+res);
        HeadlineResponse hlr = Tools.getGson().fromJson(res, HeadlineResponse.class);
        Log.i(TAG, "searchHeadlines_okhttp: "+hlr);
    }

    @Test
    public void loadNews() throws Exception {

    }

}