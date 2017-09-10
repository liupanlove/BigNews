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

import android.content.Intent;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.widget.TextView;

import bignews.myapplication.db.DAO;
import bignews.myapplication.db.DAOParam;

public class ArticleFragment extends BaseActivity {
    final static String ARG_POSITION = "position";
    int mCurrentPosition = -1;
    DAO dao = DAO.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_view);

        Intent intent = getIntent();
        mCurrentPosition = intent.getIntExtra(ARG_POSITION, -1);
        updateArticleView(mCurrentPosition);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void updateArticleView(int position) {
        TextView article = (TextView) findViewById(R.id.article);
        article.setText(dao.getNews(DAOParam.fromNewsId(position)).blockingGet().news_Title);
        mCurrentPosition = position;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the current article selection in case we need to recreate the fragment
        //outState.putInt(ARG_POSITION, mCurrentPosition);
    }
}