package com.tanishqsaluja.convex.FIrstTime;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tanishqsaluja.convex.MainActivity;
import com.tanishqsaluja.convex.R;

/**
 * Created by tanishqsaluja on 13/4/18.
 */

public class Entry_Act extends AppCompatActivity {

    private ViewPager viewPager;
    private TextView[] dots;
    private LinearLayout linearLayout;
    private SliderAdapter sliderAdapter;
    private Button back, next;
    private int pos;
    Typeface custom;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_onetime);
        Boolean isFirstRun=getSharedPreferences("PREFERENCES",MODE_PRIVATE).getBoolean("isfirstrun",true);
        if(isFirstRun){
            Typeface.createFromAsset(getAssets(), "fonts/fabrica.otf");
            viewPager = findViewById(R.id.viewPager);
            linearLayout = findViewById(R.id.linear);
            back = findViewById(R.id.button);
            next = findViewById(R.id.button2);
            sliderAdapter = new SliderAdapter(this, getAssets());
            viewPager.setAdapter(sliderAdapter);
            //dotIndicator();
            dotIndicator(0);
            viewPager.addOnPageChangeListener(viewListener);
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (pos == 2) {
                        getSharedPreferences("PREFERENCES",MODE_PRIVATE).edit().putBoolean("isfirstrun",false).commit();
                        Intent intent = new Intent(Entry_Act.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        viewPager.setCurrentItem(pos + 1);
                    }
                }
            });
        }

    }

    public void dotIndicator(int position) {
        dots = new TextView[3];
        linearLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.transwhite));
            linearLayout.addView(dots[i]);
        }
        if (dots.length > 0) {
            dots[position].setTextColor(getResources().getColor(R.color.white));
        }
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            dotIndicator(position);
            pos = position;
            if (position == 0) {
                back.setEnabled(false);
                next.setEnabled(true);
                back.setVisibility(View.INVISIBLE);
                next.setText("Next");
                next.setTypeface(custom);
            } else if (position == 1) {
                back.setEnabled(true);
                next.setEnabled(true);
                back.setVisibility(View.VISIBLE);
                next.setText("Next");
                back.setText("Back");
                next.setTypeface(custom);
            } else {
                back.setEnabled(true);
                next.setEnabled(true);
                back.setVisibility(View.VISIBLE);
                next.setText("Finish");
                back.setText("Back");
                next.setTypeface(custom);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
