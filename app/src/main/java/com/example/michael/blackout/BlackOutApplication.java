package com.example.michael.blackout;

import android.app.Application;

import com.example.michael.blackout.utils.ParseConstants;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseUser;

/**
 * Created by Michael on 5/22/2015.
 */
public class BlackOutApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "VQFMca7RQRF2SsvfrAfzkFewkXaoRhTWItQD5noF", "cQQgTTlciKxzmKDJmve6ORMAN1YovC3tejsJ3pUL");

        ParseInstallation.getCurrentInstallation().saveInBackground();


    }

    public static void updateParseInstallation(ParseUser user){
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put(ParseConstants.KEY_USER_ID, user.getObjectId());
        installation.saveInBackground();
    }
}
