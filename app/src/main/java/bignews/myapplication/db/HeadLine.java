package bignews.myapplication.db;

/**
 * Created by lazycal on 2017/9/9.
 */

public class HeadLine {
    public String newsClass, newsID, newsSource, newsTitle, newsTime, newsURL, newsAuthor, langType,
    newsPictures, newsVideo, newsIntro;

    @Override
    public String toString() {
        return "HeadLine{" +
                "newsTitle='" + newsTitle + '\'' +
                "newsClass='" + newsClass + '\'' +
                '}';
    }
}
