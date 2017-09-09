package bignews.myapplication.db;

/**
 * Created by lazycal on 2017/9/8.
 */

public class DaoParam {
    /**
     * News category [-1-12], zero means recommendation, -1 means Favorites
     */
    public int category;
    /**
     * News keywords
     */
    public String keywords;
    /**
     * News id
     */
    public int newsID;
    /**
     * When doing a query via @link{Dao}, specifying offset is useful for avoiding loading data
     * that is previously loaded.
     */
    public int offset;
    /**
     * limit of the data
     */
    public int limit;
    public static class MODE {
        public static final int TEXT = 1;
        public static final int IMAGE = 0;
        public static final int SHIELD_KEYWORDS = 1 << 1;
        public static final int MATCH_KEYWORDS = 0 << 1;
        public static final int OFF_LINE = 1 << 2;
        public static final int ON_LINE = 0 << 2;
    }
    /**
     * Text Mode or Normal Mode
     * Match or shield keywords
     */
    public int mode;

    public DaoParam() {}

    public static DaoParam fromCategory(int category, int offset, int limit) {
        DaoParam dp = new DaoParam();
        dp.category = category;
        dp.offset = offset;
        dp.limit = limit;
        return dp;
    }

    public static DaoParam fromNewsId(int newsID) {
        DaoParam dp = new DaoParam();
        dp.newsID = newsID;
        return dp;
    }
}

