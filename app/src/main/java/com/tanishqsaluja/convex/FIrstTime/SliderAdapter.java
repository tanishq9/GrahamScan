package com.tanishqsaluja.convex.FIrstTime;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tanishqsaluja.convex.R;

/**
 * Created by tanishqsaluja on 13/4/18.
 */

public class SliderAdapter extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;
    AssetManager am;

    public SliderAdapter(Context c, AssetManager assetManager) {
        this.context = c;
        this.am=assetManager;
    }

    public int[] slide_images = {R.drawable.chull, R.drawable.single, R.drawable.duble};
    public String[] slide_title = {"Graham Scan", "Single Tap", "Doube Tap"};
    public String[] slide_desc = { "Given a set of points in the plane,the convex hull of the set is the smallest convex polygon that contains all the points of it.. Worst case time complexity of Jarvis’s Algorithm is O(n^2).Using Graham’s scan algorithm, we can find Convex Hull in O(nLogn) time. "
            , "Single tap to plot points on the screen"
            , "Double tap to generate the convex hull"
    };

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (LinearLayout) object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.from(context).inflate(R.layout.slide_layout,container,false);
        ImageView logo=view.findViewById(R.id.logo);
        TextView title=view.findViewById(R.id.title);
        TextView desc=view.findViewById(R.id.desc);
        final Typeface custom = Typeface.createFromAsset(am, "fonts/fabrica.otf");
        title.setTypeface(custom);
        desc.setTypeface(custom);
        logo.setImageResource(slide_images[position]);
        title.setText(slide_title[position]);
        desc.setText(slide_desc[position]);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout)object);
    }
}
