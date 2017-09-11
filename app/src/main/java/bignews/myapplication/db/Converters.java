package bignews.myapplication.db;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

/**
 * Created by lazycal on 2017/9/11.
 */

public class Converters {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
