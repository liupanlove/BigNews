package bignews.myapplication.db.service;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import bignews.myapplication.db.Headline;

/**
 * Created by lazycal on 2017/9/9.
 */

public class HeadlineResponse {
    @SerializedName("list")
    public ArrayList<Headline> headlines;

}
