package com.tanishqsaluja.convex;

import android.app.ActionBar;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tanishqsaluja on 11/4/18.
 */

public class SettingsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, SeekBar.OnSeekBarChangeListener {
    LinearLayout upper;
    TextView ecolor, ncolor, ethickness, cradius;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        setContentView(R.layout.activiy_settings);
        Spinner nodeColor = findViewById(R.id.spinner2);
        Spinner edgeColor = findViewById(R.id.spinner);
        SeekBar sizeSeek = findViewById(R.id.seekBar);
        SeekBar edgeSeek = findViewById(R.id.edgeThickness);
        ecolor = findViewById(R.id.ecolor);
        ncolor = findViewById(R.id.ncolor);
        ethickness = findViewById(R.id.ethickness);
        cradius = findViewById(R.id.cradius);
        final Typeface custom = Typeface.createFromAsset(getAssets(), "fonts/gooddog.ttf");
        ecolor.setTypeface(custom);
        ncolor.setTypeface(custom);
        ethickness.setTypeface(custom);
        cradius.setTypeface(custom);
        sizeSeek.setOnSeekBarChangeListener(this);
        sizeSeek.setProgress(Settings.SKIP_VALUE);
        edgeSeek.setOnSeekBarChangeListener(this);
        edgeSeek.setProgress(Settings.EDGE_WEIGHT);
        //nodeColor.set

        //Spinner Color listener
        nodeColor.setOnItemSelectedListener(this);
        edgeColor.setOnItemSelectedListener(this);
        //Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Blue");
        categories.add("Green");
        categories.add("Pink");
        categories.add("Black");
        categories.add("Gray");


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, categories) {

            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                Typeface externalFont=Typeface.createFromAsset(getAssets(), "fonts/gooddog.ttf");
                ((TextView) v).setTextSize(20f);
                ((TextView) v).setTypeface(externalFont);
                return v;
            }

//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            public View getDropDownView(int position,  View convertView,  ViewGroup parent) {
                View v =super.getDropDownView(position, convertView, parent);
                Typeface externalFont=Typeface.createFromAsset(getAssets(), "fonts/gooddog.ttf");
                ((TextView) v).setTextSize(20f);
                ((TextView) v).setTypeface(externalFont);
                return v;
            }
        };

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        nodeColor.setAdapter(adapter);
        edgeColor.setAdapter(adapter);

        String defaultPosition = Settings.NODE_COLOR_NAME; //the value you want the position for
        ArrayAdapter myAdap = (ArrayAdapter) nodeColor.getAdapter(); //cast to an ArrayAdapter
        int spinnerPosition = myAdap.getPosition(defaultPosition);
        nodeColor.setSelection(spinnerPosition);

        defaultPosition = Settings.EDGE_COLOR_NAME; //the value you want the position for
        myAdap = (ArrayAdapter) edgeColor.getAdapter(); //cast to an ArrayAdapter
        spinnerPosition = myAdap.getPosition(defaultPosition);
        edgeColor.setSelection(spinnerPosition);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        Log.d("Val", item.toString() + Coordinate.getCoordinateRGB(item));

        if (parent.getId() == R.id.spinner) { //this would be the first spinner :-)
            Settings.EDGE_COLOR = Coordinate.getCoordinateRGB(item);
            Settings.EDGE_COLOR_NAME = item;
        } else {
            Settings.NODE_COLOR = Coordinate.getCoordinateRGB(item);
            Settings.NODE_COLOR_NAME = item;
        }
        refresh();
    }

    public void refresh() {
        upper = (LinearLayout) findViewById(R.id.LinearLayout01);
        upper.removeAllViews();
        upper.addView(new GridSettingSample(this));
        Log.d("ShouldUpdate", "Updated!");
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (seekBar.getId() == R.id.seekBar) {
            Settings.SKIP_VALUE = progress;
            refresh(); //refresh everything!
        } else { //edge weight!
            Settings.EDGE_WEIGHT = progress;
            refresh();
        }
    }

    /**
     * This method is used to start tracking touch.
     *
     * @param seekBar
     */
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    /**
     * This method is used to stop tracking touch.
     *
     * @param seekBar
     */
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }
}
