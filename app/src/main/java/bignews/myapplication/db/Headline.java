package bignews.myapplication.db;

/**
 * Created by lazycal on 2017/9/9.
 */

public class Headline {
    public String newsClass, newsID, newsSource, newsTitle, newsTime, newsURL, newsAuthor, langType,
    newsPictures, newsVideo, newsIntro;

    @Override
    public String toString() {
        return "Headline{" +
                "newsTitle='" + newsTitle + '\'' +
                "newsClass='" + newsClass + '\'' +
                '}';
    }
}
