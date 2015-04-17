package com.example.huma.almalzma.parse;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by huma on 4/17/2015.
 *
 */
public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "WlwPPMnIytEuhvDiKbwJUPNbrLjiIQ4qpukJnzzW", "yfghC2E5Qhs8sOmqvkNKQoxrFqxhZKwXUMqJjvo4");
    }
}
