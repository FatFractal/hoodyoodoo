/**
 * 
 */
package com.hoodyoodoo.droidapp;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import com.fatfractal.ffef.FFException;
import com.fatfractal.ffef.FatFractal;
import com.fatfractal.ffef.impl.FFLocalStorageSQLite;
import com.fatfractal.ffef.impl.FFPrefsAndroid;
import com.fatfractal.ffef.impl.FatFractalHttpImpl;
import com.fatfractal.ffef.json.FFObjectMapper;
import com.google.android.gcm.GCMRegistrar;
import com.hoodyoodoo.droidapp.model.Celebrity;
import com.hoodyoodoo.droidapp.model.StatsObject;
import com.hoodyoodoo.droidapp.model.WouldYa;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * The Hoodyoodoo class provides provides shared settings and methods for the application.
 */
public class Hoodyoodoo extends Application {
    public static String GCM_SENDER_ID = "1041876629923";
    public static FatFractal ff = null;

    /**
	 * This method calls {@link #getFF()}to create the first instance of the FatFractal class when the application starts.
	 * <p>
	 * @see FatFractal
	 */
	@Override
	public void onCreate() {
        super.onCreate();

        if (ff == null) {
			ff = Hoodyoodoo.getFF();
		}

        FFLocalStorageSQLite storage = new FFLocalStorageSQLite("hoodyoodoo", this);
        ff.setLocalStorage(storage);
        ff.setAlwaysUseCache(true);
        ff.setDebug(true);

        FFPrefsAndroid.setContext(this);
        GCMRegistrar.checkDevice(this);
        GCMRegistrar.checkManifest(this);
        String registrationId = ff.getNotificationID();

        if (registrationId == null || registrationId.equals("")) {
            // no registration found, so register
            String msg = "Registering for GCM";
            Log.i(this.getClass().getName(), msg);
            GCMRegistrar.register(this, GCM_SENDER_ID);
        } else {
            String msg = "Found registrationId : " + registrationId;
            Log.i(this.getClass().getName(), msg);
        }
    }

	/**
	 * This method initializes and returns an instance of the FatFractal class.
	 * It also registers the object classes that will be used by the application when communicating with the FFEF back-end.
	 * <p>
	 * @see FatFractal
	 */
	public static FatFractal getFF() {
		if (ff == null) {
			String baseUrl = "http://acme.fatfractal.com/hoodyoodoo";
			String sslUrl = "https://acme.fatfractal.com/hoodyoodoo";
			try {
				ff = FatFractal.getInstance(new URI(baseUrl), new URI(sslUrl));
				FatFractalHttpImpl.addTrustedHost("acme.fatfractal.com");
				FFObjectMapper.registerClassNameForClazz(Celebrity.class.getName(), "Celebrity");
				FFObjectMapper.registerClassNameForClazz(WouldYa.class.getName(), "WouldYa");
				FFObjectMapper.registerClassNameForClazz(StatsObject.class.getName(), "StatsObject");
			}
            catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
		return ff;
	}

	/**
	 * This method provides a common login method for the application. It uses the {@link FatFractal#login(String, String)}
	 * synchronous method. 
	 * 
	 * It returns an AlertDialog to the calling class that can be shown and to which event handlers can be assigned as 
	 * needed.
	 * 
	 * Note: the {@link FatFractal#isLoggedIn()}will also be set with the results
	 * <p>
	 * @return an AlertDialog instance populated to perform login/ register functions.
	 * <p>
	 * @see FatFractal
	 * @see FFException
	 */
	public AlertDialog loginDialog(Context c, String message) {
		LayoutInflater factory = LayoutInflater.from(c);
		final View textEntryView = factory.inflate(R.layout.login, null);
		final AlertDialog.Builder failAlert = new AlertDialog.Builder(c);
		failAlert.setTitle("Login/ Register Failed"); 
		failAlert.setNegativeButton("OK", new DialogInterface.OnClickListener() { 
			public void onClick(DialogInterface dialog, int whichButton) { 
				// Cancelled
			}
		});
		AlertDialog.Builder alert = new AlertDialog.Builder(c); 
		alert.setTitle("Login/ Register"); 
		alert.setMessage(message); 
		alert.setView(textEntryView); 
		alert.setPositiveButton("Login", new DialogInterface.OnClickListener() { 
			public void onClick(DialogInterface dialog, int whichButton) { 
				try {
					final EditText usernameInput = (EditText) textEntryView.findViewById(R.id.userNameEditText);
					final EditText passwordInput = (EditText) textEntryView.findViewById(R.id.passwordEditText);
					ff.login(usernameInput.getText().toString(), passwordInput.getText().toString());
				}
                catch (FFException e) {
					e.printStackTrace();
				}
			}
		});
		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() { 
			public void onClick(DialogInterface dialog, int whichButton) {
				// Canceled. 
			}
		}); 
		return alert.create();
	}
}
