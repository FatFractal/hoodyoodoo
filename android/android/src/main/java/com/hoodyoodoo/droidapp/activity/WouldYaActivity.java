/**
 * 
 */
package com.hoodyoodoo.droidapp.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.fatfractal.ffef.FFException;
import com.fatfractal.ffef.FatFractal;
import com.hoodyoodoo.droidapp.R;
import com.hoodyoodoo.droidapp.Hoodyoodoo;
import com.hoodyoodoo.droidapp.model.Celebrity;
import com.hoodyoodoo.droidapp.model.WouldYa;

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
 * @see WouldYa
 * @see Celebrity
 * @see FatFractal
 */
public class WouldYaActivity extends Activity {
	/** m_leftCeleb private data member is the first random Celebrity to be CRUD Retrieved by the WouldYaActivity.*/
	private Celebrity   m_leftCeleb;
	/** m_rightCeleb private data member is the second random, unique Celebrity to be CRUD Retrieved by the WouldYaActivity.*/
	private Celebrity   m_rightCeleb;
	/** m_m_wouldYa private data member is the WouldYa to be CRUD Created by the WouldYaActivity.*/
	private WouldYa     m_wouldYa;
	/** m_leftCelebImageView private data member is the ImageView used to display the first Celebrity image.*/
	private ImageView   m_leftCelebImageView;
	/** m_rightCelebImageView private data member is the ImageView used to display the second Celebrity image.*/
	private ImageView   m_rightCelebImageView;
	/** m_leftCelebNameTextView private data member is the TextView used to display the first Celebrity name.*/
	private TextView    m_leftCelebNameTextView;
	/** m_rightCelebNameTextView private data member is the TextView used to display the second Celebrity name.*/
	private TextView    m_rightCelebNameTextView;
	/** m_currentGender private data member is a String that is set for the queries to filter by gender from the back-end.*/
	private String      m_currentGender = "female";
	/** m_playAgainImageButton private data member is an ImageButton that is used to refresh the Celebrity Objects (to play again).*/
	private ImageButton m_playAgainImageButton;
	/** m_toggleGenderImageButton private data member is an ImageButton that is used to change the m_currentGender setting.*/
	private ImageButton m_toggleGenderImageButton;

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
		m_rightCelebImageView     = (ImageView)   findViewById(R.id.rightCelebImageView);
		m_leftCelebNameTextView   = (TextView)    findViewById(R.id.leftCelebTextView);
		m_rightCelebNameTextView  = (TextView)    findViewById(R.id.rightCelebTextView);
		m_playAgainImageButton    = (ImageButton) findViewById(R.id.playAgainImageButton);
		m_toggleGenderImageButton = (ImageButton) findViewById(R.id.toggleGenderImageButton);
		m_playAgainImageButton.setVisibility(View.INVISIBLE);
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
			String q0 = "/Celebrity/(gender eq '" + m_currentGender + "' and guid eq random(guid))";
			m_leftCeleb = ff.getObjFromUri(q0);
			m_leftCelebNameTextView.setText(m_leftCeleb.getFirstName() + " " + m_leftCeleb.getLastName());
			if (m_leftCeleb.getImageData() != null) {
				Bitmap bm = BitmapFactory.decodeByteArray(m_leftCeleb.getImageData(), 0, m_leftCeleb.getImageData().length);
				m_leftCelebImageView.setImageBitmap(bm);
			}
		} catch (FFException e) {
			e.printStackTrace();
		}
		// Get a Celebrity Object called rightCeleb asynchronously using getObjFrom(String url) wrapped with an AsyncTask.
		new AsyncTask<String, Void, Celebrity>() {
			@Override
			protected Celebrity doInBackground(String... params) {
				try {
					Celebrity celeb = new Celebrity();
					if (m_leftCeleb != null) {
						String guid = ff.getMetaDataForObj(m_leftCeleb).getGuid();
						String q2 = "/Celebrity/(gender eq '" + m_currentGender + "' and guid ne '" + guid + "' and guid eq random(guid))";
						celeb = ff.getObjFromUri(q2);
					}
                    return celeb;
				} catch (FFException e) {
					e.printStackTrace();
					return null;
				} 
			}
			@Override
			protected void onPostExecute(Celebrity rightCeleb) {
                if (rightCeleb != null) {
                    m_rightCeleb = rightCeleb;
                    m_rightCelebNameTextView.setText(m_rightCeleb.getFirstName() + " " + m_rightCeleb.getLastName());
                    if(m_rightCeleb.getImageData() != null) {
                        Bitmap bm = BitmapFactory.decodeByteArray(m_rightCeleb.getImageData(), 0, m_rightCeleb.getImageData().length);
                        m_rightCelebImageView.setImageBitmap(bm);
                    }
                }
			}
		}.execute();
		m_playAgainImageButton.setVisibility(View.INVISIBLE);	
	}

	/**
	 * This method receives a click from the {@link #m_toggleGenderImageButton} and toggles the 
	 * value of {@link #m_currentGender} as well as the image on {@link #m_toggleGenderImageButton}
	 * between values for male and female gender. 
	 * <p>
	 * After the values are set, the method calls {@link #loadCelebrities()} to load the two Celebrity objects 
	 * ({@link #m_leftCeleb} and {@link #m_rightCeleb})..
	 * @param view is mandatory to allow wouldya_layout.xml to set the click event, but is not actually used.
	 */
	public void toggleGender(View view) {
		if(m_currentGender.equals("male")) {
			m_currentGender = "female";
			m_toggleGenderImageButton.setImageResource(R.drawable.button_gender_female);
		}
		else {
			m_currentGender = "male";
			m_toggleGenderImageButton.setImageResource(R.drawable.button_gender_male);
		}		
		loadCelebrities();
	}

	/**
	 * This method receives a click from either the {@link #m_leftCelebImageView} or {@link #m_rightCelebImageView} 
	 * and attempts to populate the {@link #m_wouldYa} object to be stored on the back-end.
	 * <p>
	 * After the values are set, the method calls {@link #loadCelebrities()} to load the two Celebrity objects 
	 * ({@link #m_leftCeleb} and {@link #m_rightCeleb})..
	 * @param view is mandatory to allow wouldya_layout.xml to pass the click event, but is not actually used.
	 */
	public void celebrityWasPicked(View view) {
		m_wouldYa = new WouldYa();
		if (view == m_leftCelebImageView) {
			m_wouldYa.setSelectedGuid(ff.getMetaDataForObj(m_leftCeleb).getGuid());
			m_wouldYa.setRejectedGuid(ff.getMetaDataForObj(m_rightCeleb).getGuid());
		} else if (view == m_rightCelebImageView) {
			m_wouldYa.setSelectedGuid(ff.getMetaDataForObj(m_rightCeleb).getGuid());
			m_wouldYa.setRejectedGuid(ff.getMetaDataForObj(m_leftCeleb).getGuid());
		}
		if (m_wouldYa != null) {
			persistWouldYa();
		} else Log.i(this.getClass().getName(), "WouldYaViewController celebrityWasPicked failed: m_wouldYa is null");
	}

	/**
	 * This method is called after {@link #celebrityWasPicked(View)} is done populating the WouldYa object. It first 
	 * makes sure that the user is logged in as the FFEF framework requires authentication to store an object on the
	 * back-end. If login is required, it calls {@link Hoodyoodoo#loginDialog(android.content.Context, String)} and sets
	 * up a listener to wait for the dialog to finish, checks that the result succeeded and if so, calls {@link #persistWouldYa()}
	 * again to complete the process.
	 * <p>
	 * Storing the WouldYa is accomplished using the synchronous {@link FatFractal#createObjAt(Object, String)} method. 
	 * An FFException is caught in a try loop in the event of an error, and the exception is logged.
	 * Upon success, the {@link #m_playAgainImageButton} visibility is set to VISIBLE to allow the user to play again.
	 * <p>
	 * @see FatFractal
	 * @see FFException
	 * 
	 */
	public void persistWouldYa() {
		if (!ff.isLoggedIn()) {
            Log.i(this.getClass().getName(), "persistWouldYa found ff.isLoggedIn() false");
			Hoodyoodoo hoodyoodoo = new Hoodyoodoo();
			AlertDialog alert = hoodyoodoo.loginDialog(this, "You must be logged in to record a WouldYa selection.");
			alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
					if (ff.isLoggedIn()) persistWouldYa(); // this is a hack to allow cancel - need to find a better way
				}
			});
			alert.show();
		} else {
			if (m_wouldYa != null) {
				try {
					m_playAgainImageButton.setVisibility(View.VISIBLE);
					ff.createObjAtUri(m_wouldYa, "/WouldYa");
				} catch (FFException e) {
					e.printStackTrace();
				}
			} else Log.i(this.getClass().getName(), "WouldYaActivity persistWouldYa failed: m_wouldYa is null");
		}
	}

	/**
	 * This method is called by a click on the {@link #m_playAgainImageButton} which was made visible after a successful 
	 * {@link #persistWouldYa()} is completed. It then calls {@link #loadCelebrities()} to retrieve a new set of Celebrity
	 * objects for the next round of the game.
	 * ,p>
	 * @param view is mandatory to allow wouldya_layout.xml to pass the click event, but is not actually used.
	 */
	public void playAgain(View view) {
		loadCelebrities();
	}
}


