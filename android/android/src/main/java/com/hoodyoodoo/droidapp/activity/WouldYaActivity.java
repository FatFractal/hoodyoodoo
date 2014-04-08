/**
 * 
 */
package com.hoodyoodoo.droidapp.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.fatfractal.ffef.FFException;
import com.fatfractal.ffef.FatFractal;
import com.hoodyoodoo.droidapp.Hoodyoodoo;
import com.hoodyoodoo.droidapp.R;
import com.hoodyoodoo.droidapp.model.Celebrity;

import java.util.List;
import java.util.Random;

/**
 * The WouldYaActivity class retrieves two random, unique Celebrity objects of a specified gender, and then presents their 
 * names and profile images for selection (and rejection!). When the user clicks one of the images, that celebrity's guid is set 
 * to the selectedGuid parameter of a WouldYa and the other Celebrity is set to the rejectedGuid. When complete, and when the user
 * is verified to be logged in, the object is stored on the back-end as a new WouldYa object.
 * <p>
 * Note: The respective Celebrity resources selectedCount or rejectedCount Fields are updated when a WouldYa is created 
 * automatically using an FFEF Event Handler called "WouldYaCreate" which calls the "handleWouldYaCreate" function in the 
 * WouldYaEventHandlers.js file that was placed in the /ff-scripts directory for this application. This Javascript function 
 * increments the selectedCount or rejectedCount values and then checks to see if that has caused a new high score to have been
 * reached. In that event, the TopCelebrity collection which stores a Celebrity objecttype is updated with the Celebrity 
 * resource, in this case by reference as the back-end is aware of the Celebrity object. This is a good example of how you
 * can reuse objecttypes in various collections on the FFEF emergent framework very efficiently.
 * <p>
 * @see Celebrity
 * @see FatFractal
 */
public class WouldYaActivity extends Activity {
	/** m_leftCeleb private data member is the first random Celebrity to be CRUD Retrieved by the WouldYaActivity.*/
	private Celebrity   m_leftCeleb;
	/** m_leftCelebImageView private data member is the ImageView used to display the first Celebrity image.*/
	private ImageView   m_leftCelebImageView;
	/** m_leftCelebNameTextView private data member is the TextView used to display the first Celebrity name.*/
	private TextView    m_leftCelebNameTextView;

	/**
	 * ff is the local instance of the FatFractal class used in this class.
	 */
	private FatFractal ff = Hoodyoodoo.getFF();		

	/**
	 * We use the onCreate method to set up the ui layout from wouldya_layout.xml and bind the elements there to the 
	 * private data members of this class. 
	 */	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wouldya_layout);
		m_leftCelebImageView      = (ImageView)   findViewById(R.id.leftCelebImageView);
		m_leftCelebNameTextView   = (TextView)    findViewById(R.id.leftCelebTextView);
		loadCelebrities();
	}

	/**
	 * The loadCelebrities method demonstrates how to retrieve objects both synchronously and asynchronously.
	 * The first Celebrity Object is retrieved using the synchronous method directly. The second
	 * is wrapped in an AsyncTask which calls onPostExecute when completed. Using this standard
	 * Android method means that there is no need for the FatFractal SDK to implement asynchronous
	 * methods which would further intrude into your code unnecessarily.
	 */
	private void loadCelebrities() {
		// Get a Celebrity Object called leftCeleb synchronously using getObjFrom(String url).
		// Note: this is not generally recommended, you should usually use wrap with AsyncTask as below.
		try {
            List<Celebrity> celebArray = ff.getArrayFromUri("/Celebrity");
            int r = new Random().nextInt(celebArray.size());
            m_leftCeleb = celebArray.get(r);
			m_leftCelebNameTextView.setText(m_leftCeleb.getFirstName() + " " + m_leftCeleb.getLastName());
			if(m_leftCeleb.getImageData() != null) {
				Bitmap bm = BitmapFactory.decodeByteArray(m_leftCeleb.getImageData(), 0, m_leftCeleb.getImageData().length);
				m_leftCelebImageView.setImageBitmap(bm);
			}
		} catch (FFException e) {
			e.printStackTrace();
		} 
	}
}


