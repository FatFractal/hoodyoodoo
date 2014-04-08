package com.hoodyoodoo.droidapp.activity;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import com.hoodyoodoo.droidapp.R;

/**
 * HoodyoodooActivity sets the content view as a tabbed application. 
 *
 */
@SuppressWarnings("deprecation")
public class HoodyoodooActivity extends TabActivity {
    public static final String TAB_WOULDYA_TAG     = "WouldYa";
    public static final String TAB_ADD_CELEB_TAG   = "AddCelebrity";
    public static final String TAB_TOP_CELEB_TAG   = "TopCeleb";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        TabHost tabHost = getTabHost();
        
        // Tab for WouldYa
        TabSpec wouldYaSpec = tabHost.newTabSpec(TAB_WOULDYA_TAG);
        wouldYaSpec.setIndicator("WouldYa", getResources().getDrawable(R.drawable.icon_wouldya_tab));
        Intent wouldYaIntent = new Intent(this, WouldYaActivity.class);
        wouldYaSpec.setContent(wouldYaIntent);
        
        // Tab for Add Celebrity
        TabSpec addCelebritySpec = tabHost.newTabSpec(TAB_ADD_CELEB_TAG);
        // setting Title and Icon for the Tab
        addCelebritySpec.setIndicator("AddCelebrity", getResources().getDrawable(R.drawable.icon_celebrity_tab));
        Intent addCelebrityIntent = new Intent(this, CelebrityActivity.class);
        addCelebritySpec.setContent(addCelebrityIntent);
        
        // Tab for TopCeleb
        TabSpec topCelebritySpec = tabHost.newTabSpec(TAB_TOP_CELEB_TAG);
        topCelebritySpec.setIndicator("TopCeleb", getResources().getDrawable(R.drawable.icon_top_celeb_tab));
        Intent topCelebrityIntent = new Intent(this, TopCelebActivity.class);
        topCelebritySpec.setContent(topCelebrityIntent);
        
        // Adding all TabSpec to TabHost
        tabHost.addTab(wouldYaSpec); // Adding wouldYaSpec tab
        tabHost.addTab(addCelebritySpec); // Adding addCelebrity tab
        tabHost.addTab(topCelebritySpec); // Adding topCelebrity tab
        Log.i(this.getClass().getName(), "tabs r loaded");
    }
}