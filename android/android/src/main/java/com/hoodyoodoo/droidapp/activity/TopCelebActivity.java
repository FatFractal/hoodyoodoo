/**

 */
package com.hoodyoodoo.droidapp.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import com.fatfractal.ffef.FFException;
import com.fatfractal.ffef.FatFractal;
import com.hoodyoodoo.droidapp.Hoodyoodoo;
import com.hoodyoodoo.droidapp.R;
import com.hoodyoodoo.droidapp.model.Celebrity;

/**
 * TopCelebActivity provides the UI view for displaying the TopCelebrity object as well as the StatsObject that
 * relates to it and the user. 
 * <p>
 * Note: The TopCelebrity object is created and persisted by an FFEF Event Handler called "WouldYaCreate" which 
 * calls the "handleWouldYaCreate" function in the WouldYaEventHandlers.js file that was placed in the /ff-scripts 
 * directory for this application.
 * <p>
 * Note: The StatsObject is never persisted, but instead is created "on the fly" by an URL addressable FFEF Server 
 * Extension called "/Stats" which calls the "aggregateStats" function in the AggregateStats.js file that was placed 
 * in the /ff-scripts directory for this application.
 * <p>
 * Both of these behaviors are defined in this application's FFDL description in the application.ffdl file in 
 * the /ff-config directory that was created when the application was scaffolded. 
 * <p>
 * @see Celebrity
 * @see FatFractal
 */

public class TopCelebActivity extends Activity {
	/** The ImageView used to display the Bitmap image for the TopCelebrity.*/
	private ImageView m_topCelebImageView;
	/** The TextView used to display the String name of the Celebrity from the TopCelebrity object.*/
	private TextView m_celebrityNameTextView;
	/** The TextView used to display a String representation of the Integer value of the total selected ratings from the TopCelebrity object.*/
	private TextView m_selectedValueTextView;
	/** The TextView used to display a String representation of the Integer value of the total rejected ratings from the TopCelebrity object.*/
	private TextView m_rejectedValueTextView;
	/** The local instance of the FatFractal class used in this class.*/
	private FatFractal ff = Hoodyoodoo.getFF();

	/**
	 * We use the onCreate method to set up the ui layout from top_celeb_layout.xml and bind the elements there to the 
	 * private data members of this class. 
	 */	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.top_celeb_layout);
		m_topCelebImageView             = (ImageView) findViewById(R.id.topCelebImageView);
		m_celebrityNameTextView         = (TextView)  findViewById(R.id.celebrityNameTextView);
		m_selectedValueTextView         = (TextView)  findViewById(R.id.selectedValueTextView);
		m_rejectedValueTextView         = (TextView)  findViewById(R.id.rejectedValueTextView);
	}

	/**
	 * We use the onResume method to initiate retrieval of the data stored on the back-end for this class
	 * so that the data is refreshed each time the view appears. 
	 *
	 */	
	public void onResume() {
		super.onResume();
		getTopCelebrity();
	}

	/**
	 * This method demonstrates how to retrieve the TopCelebrity object synchronously.
	 * The TopCelebrity Object topCeleb is retrieved using the synchronous method directly.
	 * 
     * Note: The TopCelebrity object is created and persisted by an FFEF Event Handler called "WouldYaCreate" which 
     * calls the "handleWouldYaCreate" function in the WouldYaEventHandlers.js file that was placed in the /ff-scripts 
     * directory for this application.
	 * 
	 * This behavior is defined in this application's FFDL description in the application.ffdl file in the /ff-config directory
	 * for the application that was created when the application was scaffolded. 
	 */
	public void getTopCelebrity() {
		try {
			Celebrity topCeleb = ff.getObjFromUri("/ff/resources/TopCelebrity");
			if (topCeleb != null) {
				if (topCeleb.getImageData() != null) {
					Bitmap bm = BitmapFactory.decodeByteArray(topCeleb.getImageData(), 0, topCeleb.getImageData().length);
					m_topCelebImageView.setImageBitmap(bm);
				}
				String name = "";
				if (topCeleb.getFirstName() != null) name = topCeleb.getFirstName();
				if (topCeleb.getLastName() != null) name = name + " " + topCeleb.getLastName();
				m_celebrityNameTextView.setText(name);
				if (topCeleb.getSelectedCount() != null) m_selectedValueTextView.setText(topCeleb.getSelectedCount().toString());
				if (topCeleb.getRejectedCount() != null) m_rejectedValueTextView.setText(topCeleb.getRejectedCount().toString());
			} else {
                Log.i(this.getClass().getName(), "getTopCelebrity did not retrieve a Celebrity");
			}
		}
		catch (FFException e) {
			e.printStackTrace();
		}
	}

}

