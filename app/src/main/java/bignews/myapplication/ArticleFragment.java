/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package bignews.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import bignews.myapplication.db.DAO;
import bignews.myapplication.db.DAOParam;

public class ArticleFragment extends Activity {
    final static String ARG_POSITION = "position";
    int mCurrentPosition = -1;
    DAO dao = DAO.getInstance();

    PopupWindow pop;
    TextView textView;
    View view;
    //Toolbar toolBar;
    LinearLayout linearLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_view);

        initView();
        initData();
        Intent intent = getIntent();
        mCurrentPosition = intent.getIntExtra(ARG_POSITION, -1);          //
        updateArticleView(mCurrentPosition);

        ImageView imageView = (ImageView) findViewById(R.id.menu);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pop.showAtLocation(linearLayout, Gravity.BOTTOM, 0, 0);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void updateArticleView(int position)
    {
        TextView article = (TextView) findViewById(R.id.article);
        article.setText(dao.getNews(DAOParam.fromNewsId(position)).blockingGet().newsTitle);
        mCurrentPosition = position;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save the current article selection in case we need to recreate the fragment
        //outState.putInt(ARG_POSITION, mCurrentPosition);
    }

    private void initView()
    {
        LayoutInflater inflater = LayoutInflater.from(this);
        view = inflater.inflate(R.layout.popup, null);
        textView = (TextView) findViewById(R.id.article);
        linearLayout = (LinearLayout) findViewById(R.id.linear);
        //toolBar = (Toolbar) findViewById(R.id.toolbar1);
    }
    private void initData()
    {
        pop = new PopupWindow(view, ViewPager.LayoutParams.MATCH_PARENT, 700, true); // 有点问题
        //pop.setAnimationStyle(R.style.MenuAnimationFade);
        pop.setBackgroundDrawable(new BitmapDrawable());
    }
}