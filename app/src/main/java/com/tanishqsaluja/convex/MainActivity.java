package com.tanishqsaluja.convex;

import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tanishqsaluja.convex.FIrstTime.Entry_Act;
import com.tanishqsaluja.convex.FIrstTime.SliderAdapter;

import java.util.ArrayList;
import java.util.LinkedList;


public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

    private ViewPager viewPager;
    private TextView[] dots;
    private LinearLayout linearLayout;
    private SliderAdapter sliderAdapter;
    private Button back, next;
    private int pos;
    Typeface custom;

    /**
     * mDetector GestureDetectorCompat This is the GestureDetector reference.
     */

    private GestureDetectorCompat mDetector;
    /**
     * scaleGestureDetector ScaleGestureDetector This is used to detect gestures.
     */
    ScaleGestureDetector scaleGestureDetector;
    /**
     * gridRenderer GridView This is the GridView reference.
     */
    GridView gridRenderer;
    /**
     * grid Grid This is the grid reference.
     */
    private Grid grid;
    /**
     * settings Settings This is the settings reference to store the settings.
     */
    private Settings settings;
    /**
     * points ArrayList This is the arraylist of points.
     */
    private ArrayList<Coordinate> points = new ArrayList<Coordinate>();
    /**
     * hull Hull This is the hull reference.
     */
    private static Hull hull; //ehh, seems a bit sketchy lol.

    /**
     * This method sets the grid to a specified value.
     *
     * @param grid Grid This is the Grid reference.
     */
    public void setGrid(Grid grid) {
        this.grid = grid;
    }

    /**
     * This method returns the grid.
     *
     * @return Settings
     */
    public Settings getSettings() {
        return settings;
    }

    /**
     * This method sets the starting x value.
     *
     * @param x int This is the starting value.
     */
    public void setStartX(int x) {
        settings.setXStart(x);
        refresh();
    }

    /**
     * This method sets the starting y value.
     *
     * @param y int This is the starting y value set.
     */
    public void setStartY(int y) {
        settings.setYStart(y);
        refresh();
    }

    /**
     * This method sets the skip value.
     *
     * @param skip int This is the new skip value.
     */
    public void setSkip(int skip) {
        settings.setSkip(skip);
        refresh();
    }

    /**
     * This method returns the hull.
     *
     * @return Hull This would be the hull.
     */
    public static Hull getHull() {
        return hull;
    }

    /**
     * This method sets up the graphics on the screen.
     *
     * @param savedInstanceState
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Boolean isFirstRun = getSharedPreferences("PREFERENCES", MODE_PRIVATE).getBoolean("isfirstrun", true);
        if (isFirstRun) {
            Log.e("tes", "first time");
            super.onCreate(savedInstanceState);
            Typeface.createFromAsset(getAssets(), "fonts/fabrica.otf");
            getSupportActionBar().hide();
            setContentView(R.layout.activity_onetime);
            viewPager = findViewById(R.id.viewPager);
            linearLayout = findViewById(R.id.linear);
            back = findViewById(R.id.button);
            next = findViewById(R.id.button2);
            sliderAdapter = new SliderAdapter(MainActivity.this, getAssets());
            viewPager.setAdapter(sliderAdapter);
            //dotIndicator();
            dotIndicator(0);
            viewPager.addOnPageChangeListener(viewListener);
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (pos == 2) {
                        getSharedPreferences("PREFERENCES", MODE_PRIVATE).edit().putBoolean("isfirstrun", false).commit();
                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        viewPager.setCurrentItem(pos + 1);
                    }
                }
            });

        } else {
            Log.e("tes", "dont care which time");
            super.onCreate(savedInstanceState);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            settings = new Settings(Settings.DEFAULT_START_X, Settings.DEFAULT_START_Y, Settings.DEFAULT_SKIP);
            Coordinate.populateColorConverter();
            gridRenderer = new GridView(this, grid, settings);
            setContentView(R.layout.activity_main);

            Button settin = findViewById(R.id.settings);
            Button rest = findViewById(R.id.rest);
            final Typeface custom = Typeface.createFromAsset(getAssets(), "fonts/fabrica.otf");
            settin.setTypeface(custom);
            rest.setTypeface(custom);
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.actionbar);

            LinearLayout upper = (LinearLayout) findViewById(R.id.LinearLayout01);
            upper.addView(gridRenderer);

            scaleGestureDetector = new ScaleGestureDetector(this, new MyOnScaleGestureListener(this));
            mDetector = new GestureDetectorCompat(this, this);
            mDetector.setOnDoubleTapListener(this);
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


    /**
     * This is the settings onClick call
     *
     * @param v
     */

    public void settings(View v) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Log.e("yolo", "back button pressed");
            LinearLayout upper = (LinearLayout) findViewById(R.id.LinearLayout01);
            reset(upper);
            refresh();
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * This method resets the grid.
     *
     * @param v
     */
    public void reset(View v) {
        if (points == null) //safety? no lol still crashes. odd..
            return;
        for (int x = 0; x < points.size(); x++) {
            grid.setFalse(points.get(x).getX(), points.get(x).getY());
        }
        points = new ArrayList<Coordinate>();
        if (grid == null) //should this ever happen?
            return;
        grid.setPoints(new LinkedList<Coordinate>());
        hull = new Hull(new Coordinate[]{});

        refresh();
    }


    /**
     * This method is called when there is a touch event.
     *
     * @param event
     * @return boolean Determines if there was a touch event.
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleGestureDetector.onTouchEvent(event);
        this.mDetector.onTouchEvent(event);
        return true;
    }

    /**
     * This method is called whenever something is tapped down.
     *
     * @param event This is the MotionEvent reference.
     * @return boolean This determines if something was pressed.
     */
    @Override
    public boolean onDown(MotionEvent event) {
        Log.d("Sensor", "onDown: " + event.toString());
        return true;
    }

    /**
     * This method is called when a fling occured.
     *
     * @param event1    This is the first event.
     * @param event2    This is the second event.
     * @param velocityX This is the velocity of the starting flick.
     * @param velocityY This is the velocity of the second flick.
     * @return boolean This returns that there was a fling.
     */
    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
        Log.d("Sensor", "onFling: " + event1.toString() + event2.toString());
        return true;
    }

    /**
     * This method is called when there was a long click.
     *
     * @param event This is the event reference.
     */
    @Override
    public void onLongPress(MotionEvent event) {
    }

    /**
     * This method is used to scroll through the grid.
     *
     * @param e1        This is the first event.
     * @param e2        This is the second event.
     * @param distanceX This is the x distance covered.
     * @param distanceY This is the y distance covered.
     * @return boolean This is the boolean if there has been a scroll.
     */
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        if (!Settings.SHOULD_MOVE)
            return true;
        Log.d("Sensor", "onScroll: " + e1.toString() + e2.toString());
        if (settings.getXStart() + (int) distanceX >= 0)
            setStartX(settings.getXStart() + (int) distanceX);
        if (settings.getYStart() + (int) distanceY >= 0)
            setStartY(settings.getYStart() + (int) distanceY);

        return true;
    }

    /**
     * This method shows whenever something is pressed.
     *
     * @param event
     */
    @Override
    public void onShowPress(MotionEvent event) {
        Log.d("Sensor", "onShowPress: " + event.toString());
    }

    /**
     * This method is called whenever there was a tap up.
     *
     * @param event
     * @return boolean This is true when there is a tap.
     */


    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        Log.d("Sensor", "onSingleTapUp: " + event.toString());
        return true;
    }

    /**
     * This method is called whenever there is a double tap.
     *
     * @param event This is the event reference variable.
     * @return This method returns if there was a double tap.
     */
    @Override
    public boolean onDoubleTap(MotionEvent event) {
        Log.d("Sensor", "onDoubleTap: " + event.toString());
        //compute?
        Coordinate[] arr = new Coordinate[points.size()];
        for (int x = 0; x < points.size(); x++) {
            arr[x] = points.get(x);
        }
        GrahamScan solve = new GrahamScan(arr); //return the size?
        hull = new Hull(solve.solve());
        refresh();
        return true;
    }

    /**
     * This method returns if there was a double tap.
     *
     * @param event
     * @return boolean Determines if there was a double tap.
     */
    @Override
    public boolean onDoubleTapEvent(MotionEvent event) {
        Log.d("Sensor", "onDoubleTapEvent: " + event.toString());
        return true;
    }

    /**
     * This method adds a point to the graph upon a single tap.
     * If skip is enabled then it calculates the appropriate point. Else, it will simply populate the grid.
     *
     * @param event This is the event reference.
     * @return boolean This is the return value.
     */
    @Override
    public boolean onSingleTapConfirmed(MotionEvent event) {
        Log.d("SensorTap", "Tap!" + event.toString());
        //add here...
        int xPos = (int) event.getRawX(); //why? wtf lol
        int yPos = (int) event.getRawY() - 150;
        yPos = Math.max(0, yPos);

        /* Error trap :) */
        if (xPos > GridView.width) {
            xPos = GridView.width - 1;
        }

        if (yPos > GridView.height) {
            yPos = GridView.height - 1;
        }

        int xIndex = (xPos / GridView.getSkip() + gridRenderer.getXStart());
        int yIndex = (yPos / GridView.getSkip() + gridRenderer.getYStart());

        if (grid == null) {
            grid = new Grid(gridRenderer.getWidth(), gridRenderer.getHeight());
        }
        Log.d("Grid", xIndex + " " + yIndex + " : " + xPos + " " + yPos);
        if (Settings.SHOULD_MOVE)
            grid.setTrue(xIndex, yIndex); //update the grid this way... NOW CHANGE THE RENDERING FOR THE GRID SYSTEM!
        else
            grid.setTrue(xPos, yPos); //good enough

        points.add(new Coordinate(xPos, yPos, GridView.getSkip())); //change coordinates? ADD GET SKIP -> OK
        grid.getPoints().add(new Coordinate(xPos, yPos, 1)); //standard...
        gridRenderer = new GridView(this, grid, settings);
        setContentView(R.layout.activity_main);
        LinearLayout upper = (LinearLayout) findViewById(R.id.LinearLayout01);
        upper.addView(gridRenderer);
        return true;
    }

    /**
     * This method refreshes the screen.
     * IT only resets the top part with the hull.
     */
    public void refresh() {
        if (grid == null) {
            grid = new Grid(gridRenderer.getWidth(), gridRenderer.getHeight());
        }
        gridRenderer = new GridView(this, grid, settings);
        gridRenderer = new GridView(this, grid, settings);
        setContentView(R.layout.activity_main);

        LinearLayout upper = (LinearLayout) findViewById(R.id.LinearLayout01);

        upper.addView(gridRenderer);
    }

    /**
     * This method is called whenever the app is restarted, ie, from sleep or multi-tasking.
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        refresh();
    }

    @Override
    protected void onStop() {
        super.onStop();
  //      refresh();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        refresh();
    }
}