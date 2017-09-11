package bignews.myapplication.db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by lazycal on 2017/9/10.
 */
@Entity
public class Keyword {
    public Keyword(String word, Double score) {
        this.score = score;
        this.word = word;
    }

    @Override
    public String toString() {
        return "Keyword{" +
                "score=" + score +
                ", word='" + word + '\'' +
                '}';
    }

    public Double score;
    @PrimaryKey
    public String word;
}
