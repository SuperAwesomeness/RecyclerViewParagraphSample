package com.example.samsung.recyclerviewparagraphsample;

import android.os.Bundle;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class MainActivity extends AppCompatActivity implements ParseJSONTask.AsyncResponse {

    private ArrayList<String> articles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new ParseJSONTask(this, this).execute();
    }

    @Override
    public void processFinish(ArrayList<String> output) {
        articles = output;

        ViewPager articleVP = findViewById(R.id.vp_article);
        MyPagerAdapter mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        articleVP.setAdapter(mPagerAdapter);
    }

    private class MyPagerAdapter extends FragmentStatePagerAdapter {

        MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return ArticleBodyFragment.newInstance(articles.get(position));
        }

        @Override
        public int getCount() {
            return articles.size();
        }
    }

}
