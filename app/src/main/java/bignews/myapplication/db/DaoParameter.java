package bignews.myapplication.db;

/**
 * Created by lazycal on 2017/9/8.
 */

public class DaoParameter {
    String category;
    public int id;

    public DaoParameter(String category) {
        this.category = category;
    }

    public DaoParameter(int id) {
        this.id = id;
    }
}

