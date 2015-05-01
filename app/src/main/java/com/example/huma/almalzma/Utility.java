package com.example.huma.almalzma;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.util.Patterns;

/**
 * Created by huma on 4/30/2015.
 */
public class Utility {

    //make sure the user has enter wright Uri >> and add http:// to it if it don't.
    public static String validateLinkWithAlerDailog(String link, Context context) {
        if (Patterns.WEB_URL.matcher(link).matches()) {
            if (!link.startsWith("http://") && !link.startsWith("https://"))
                link = "http://" + link;
            return link;

        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(context.getString(R.string.wrong_uri_error_title))
                    .setMessage(R.string.wrong_uri_error_message)
                    .setPositiveButton(android.R.string.ok, null)
                    .create().show();
        }
        return null;
    }

    public static String validateLink(String link) {
        if (Patterns.WEB_URL.matcher(link).matches()) {
            if (!link.startsWith("http://") && !link.startsWith("https://"))
                link = "http://" + link;
            return link;

        }
        return null;
    }

}
