/**
 * 
 */
package com.hoodyoodoo.droidapp.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import com.fatfractal.ffef.FFException;
import com.fatfractal.ffef.FatFractal;
import com.hoodyoodoo.droidapp.Hoodyoodoo;
import com.hoodyoodoo.droidapp.R;
import com.hoodyoodoo.droidapp.model.Celebrity;

import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * CelebrityActivity class provides the UI view for creating and persisting a new Celebrity object to the FFEF back-end 
 * using the FatFractal Android SDK. This view requires authentication of the user as it writes data to the 
 * service and that requires authentication. Of note, is the seamless manner with which a complex Celebrity object
 * with image data represented as a byte array can be persisted with a single line of code in the client application.
 *  
 *@see Celebrity
 *@see FatFractal
 */
public class CelebrityActivity extends Activity {
	/**This Field is the EditText to capture the first name of the new Celebrity.*/
	private EditText    m_firstNameEditText;
	/**This Field is the EditText to capture the last name of the new Celebrity.*/
	private EditText    m_lastNameEditText;
	/**This Field is the ImageButton that when clicked will bring up an image chooser and displays the result.*/
	private ImageButton m_addCelebImageButton;
	/** This Field is the Celebrity object that will be created on the back-end.*/
	private Celebrity   m_celebrity;
	/** This Field is the Uri for the image stored on the device that is converted to a byte[] and stored with the Celebrity object m_celebrity.*/
	private Uri         m_imageCaptureUri;
	/** This is the local instance of the FatFractal class used in this class.*/
	private FatFractal ff = Hoodyoodoo.getFF();		
	/** The static variable {@link #PICK_FROM_CAMERA} is an int that is used to determine where the image will be selected from (Camera or File).*/
	private static final int PICK_FROM_CAMERA = 1;
	/** The static variable {@link #PICK_FROM_FILE} is an int that is used to determine where the image will be selected from (Camera or File).*/
	private static final int PICK_FROM_FILE = 2;

	/**
	 * We use the onCreate method to set up the ui layout from celebrity_layout.xml and bind the elements there to the 
	 * private data members of this class. 
	 */	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.celebrity_layout);
		m_firstNameEditText   = (EditText)    findViewById(R.id.firstNameEditText);
		m_lastNameEditText    = (EditText)    findViewById(R.id.lastNameEditText);
		m_addCelebImageButton = (ImageButton) findViewById(R.id.addCelebImageButton);
		m_celebrity = new Celebrity();
    }

	/**
	 *  This method creates an AlertDialog with buttons that are used to capture the gender of the new Celebrity.
	 */
	private void addGender() {
		if(m_celebrity == null) m_celebrity = new Celebrity();
		AlertDialog.Builder alert = new AlertDialog.Builder(this); 
		alert.setPositiveButton("Male", new DialogInterface.OnClickListener() { 
			public void onClick(DialogInterface dialog, int whichButton) {
				m_celebrity.setGender("male");
				addCelebrity();
			} 
		}); 
		alert.setNegativeButton("Female", new DialogInterface.OnClickListener() { 
			public void onClick(DialogInterface dialog, int whichButton) { 
				m_celebrity.setGender("female");
				addCelebrity();
			} 
		});
		alert.show();
	}

	/**
	 * This method receives a click from the addCelebButton, checks if the Celebrity name fields have content 
	 * and, if so, sets the values for firstName and lastName for the {@link #m_celebrity} object.
	 * <p>
	 * This method then calls {@link #addCelebrity()} if they are present else does nothing. 
	 *
	 * @param  view the mandatory View from the sender as defined in celebrity_layout.xml
	 * @see    Celebrity
	 */
	public void doneAction(View view) {
		if(m_celebrity == null) m_celebrity = new Celebrity();
		if((m_firstNameEditText.getText().toString().length() >0) && (m_lastNameEditText.getText().toString().length() >0)) {
			m_celebrity.setLastName(m_lastNameEditText.getText().toString());
			m_celebrity.setFirstName(m_firstNameEditText.getText().toString());
			addCelebrity();
		}
	}

	/**
	 * This method is called from either {@link #addGender()} or {@link #doneAction(View)}, and will attempt to store
	 * the new Celebrity object to the back-end.
	 * <p>
	 * The method first checks to see that the user is logged in as this is required in order to write an
	 * object to the back-end. If not, it will call the #loginDialog(Context c, String message) method of the Hoodyoodoo 
	 * class.
	 * <p>
	 * The method then checks that Celebrity {@link #m_celebrity} name fields are not null. If not, brings up an alert 
	 * dialog to request that these are provided and does nothing more.
	 * <p>
	 * The method then checks that Celebrity image data is not null and, if so, sets the values for the 
	 * first and last name have some content. If not, brings up an alert dialog to request that these are
	 * provided and does nothing more.
	 * <p>
	 * This method then calls the {@link FatFractal#createObjAt(Object, String)} method to store the object.
	 * <p>
	 * Lastly, if successful, resets the {@link #m_celebrity}, {@link #m_addCelebImageButton}, {@link #m_firstNameEditText}
	 * and {@link #m_lastNameEditText} private data members. 
	 * 
	 * @param  view the mandatory View from the sender as defined in celebrity_layout.xml
	 * @see    Celebrity
	 * @see    Hoodyoodoo
	 * @see    FatFractal
	 */
	public void addCelebrity() {
		if(!ff.isLoggedIn()) {
			Hoodyoodoo hoodyoodoo = new Hoodyoodoo();
			AlertDialog alert = hoodyoodoo.loginDialog(this, "You must be logged in to save a new Celebrity.");
			alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
					if(ff.isLoggedIn()) addCelebrity(); // this is a hack to allow cancel - need to find a better way
				}
			});
			alert.show();
		} else {
			if((m_celebrity.getFirstName() == null) || (m_celebrity.getLastName() == null)) {
				AlertDialog.Builder alert = new AlertDialog.Builder(this); 
				alert.setTitle("Additional Information needed"); 
				alert.setMessage("Please include a first and last name for the Celebrity"); 
				alert.setNegativeButton("OK", new DialogInterface.OnClickListener() { 
					public void onClick(DialogInterface dialog, int whichButton) { 
						// Canceled. 
					}
				});
				alert.show();
			} 
			else if(m_celebrity.getImageData() == null) {
				AlertDialog.Builder alert = new AlertDialog.Builder(this); 
				alert.setTitle("Additional Information needed"); 
				alert.setMessage("Please include an image for the Celebrity"); 
				alert.setNegativeButton("OK", new DialogInterface.OnClickListener() { 
					public void onClick(DialogInterface dialog, int whichButton) { 
					} 
				});
				alert.show();
			} 
			else if(m_celebrity.getGender() == null) {
				addGender();
			} 
			else {
				try {
					ff.createObjAtUri(m_celebrity, "/Celebrity");
					m_celebrity = new Celebrity();
					m_addCelebImageButton.setImageResource(R.drawable.addphoto);
					m_firstNameEditText.setText(null);					
					m_lastNameEditText.setText(null);					
				} catch (FFException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * This method receives a click from the addCelebImageButton, and then brings up an AlertDialog to allow the user
	 * to choose from getting an image from their Camera or from their local storage (SD Card).
	 * <p>
	 * the {@link #onActivityResult(int, int, Intent)} method will then handle the result when completed.
	 * 
	 * @param  view the mandatory View from the sender as defined in celebrity_layout.xml
	 */
	public void selectImage(View v) {
		final String [] items			= new String [] {"From Camera", "From SD Card"};				
		ArrayAdapter<String> adapter	= new ArrayAdapter<String> (this, android.R.layout.select_dialog_item,items);
		AlertDialog.Builder builder		= new AlertDialog.Builder(this);
		builder.setTitle("Select Image");
		builder.setAdapter( adapter, new DialogInterface.OnClickListener() {
			public void onClick( DialogInterface dialog, int item ) {
				if (item == 0) {
					Intent intent 	 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					File file		 = new File(Environment.getExternalStorageDirectory(),
							"tmp_avatar_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
					m_imageCaptureUri = Uri.fromFile(file);
					try {			
						intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, m_imageCaptureUri);
						intent.putExtra("return-data", true);
						startActivityForResult(intent, PICK_FROM_CAMERA);
					} catch (Exception e) {
						e.printStackTrace();
					}			
					dialog.cancel();
				} else {
					Intent intent = new Intent();
					intent.setType("image/*");
					intent.setAction(Intent.ACTION_GET_CONTENT);
					startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_FROM_FILE);
				}
			}
		} );
		final AlertDialog dialog = builder.create();
		dialog.show();
	}

	/**
	 * This method is called when the user selects an image for the Celebrity object, and then sets the image for the
	 * {@link #m_celebrity} object's imageData Field and the {@link #m_addCelebImageButton} image data.
	 * <p>
	 * @param requestCode is an int that defines if the data is returned from the Camera or from a File.
	 * @param resultCode is an int that is used to test for successful return.
	 * @param data is the Intent data about the media from which we will get the Uri path to the image.
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) return;
		Bitmap bitmap = null;
		String path = "";
		if (requestCode == PICK_FROM_FILE) {
			m_imageCaptureUri = data.getData();
			String [] proj = {MediaStore.Images.Media.DATA};
			Cursor cursor = getContentResolver().query(m_imageCaptureUri, proj, null, null, null);
			if (cursor != null) {
				int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				cursor.moveToFirst();
				path = cursor.getString(column_index);
				if (path == null)
					path = m_imageCaptureUri.getPath();
				if (path != null)
					bitmap = BitmapFactory.decodeFile(path);
			}
		} else {
			path = m_imageCaptureUri.getPath();
			bitmap= BitmapFactory.decodeFile(path);
		}
		ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
		bitmap.compress(CompressFormat.PNG, 0 /*ignored for PNG*/, bos); 
		byte[] bitmapdata = bos.toByteArray();
		m_celebrity.setImageData(bitmapdata);
		m_addCelebImageButton.setImageBitmap(bitmap);
	}
}


