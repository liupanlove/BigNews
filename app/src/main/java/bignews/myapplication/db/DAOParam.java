package bignews.myapplication.db;

/**
 * Created by lazycal on 2017/9/8.
 */

public class DAOParam {
    public static final int FAVORITE = -1;
    public static final int RECOMMENDATION = 0;
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
    public String newsID;
    /**
     * When doing a query via @link{DAO}, specifying offset is useful for avoiding loading data
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
        public static final int OFF_LINE = 1 << 1;
        public static final int ON_LINE = 0 << 1;
    }
    /**
     * Text Mode or Normal Mode
     * Match or shield keywords
     */
    public int mode;

    public DAOParam() {}

    public static DAOParam fromCategory(int category, int offset, int limit) {
        DAOParam dp = new DAOParam();
        dp.category = category;
        dp.offset = offset;
        dp.limit = limit;
        return dp;
    }

    public static DAOParam fromNewsId(String newsID) {
        DAOParam dp = new DAOParam();
        dp.newsID = newsID;
        return dp;
    }
}

