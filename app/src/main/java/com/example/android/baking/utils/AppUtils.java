package com.example.android.baking.utils;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import androidx.test.espresso.IdlingResource;

import com.example.android.baking.testing.SimpleIdlingResource;

public class AppUtils {
    private AppUtils(){ }

    public static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net";
    public static final String JSON_LOC = "/topher/2017/May/59121517_baking/baking.json";

    // the recipe being added as "extra" to the recipe info activity
    public static final String EXTRAS_RECIPE = "recipe";
    // the step extras passed from the recipe info activity to the details activity
    public static final String EXTRAS_STEP = "step";
    public static final String EXTRAS_RECIPE_NAME = "recipe_name";

    // preferences
    public static final String PREFERENCES_ID = "ID";
    public static final String PREFERENCES_WIDGET_TITLE = "WIDGET_TITLE";
    public static final String PREFERENCES_WIDGET_CONTENT = "WIDGET_CONTENT";

    public static final String MIME_VIDEO = "video/";
    public static final String MIME_IMAGE = "image/";

    public static String getMimeType(Context context, Uri uri){
        String mimeType = null;
        if(uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)){
            ContentResolver resolver = context.getContentResolver();
            mimeType = resolver.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension.toLowerCase());
        }
        return mimeType;
    }

    /*
     * Return a well formatted String representation of an integer or decimal with correct decimal places
     * @param d number that can be either float or integer
     * @return String representation of number
     */
    @SuppressLint("DefaultLocale")
    public static String fmt(double d) {
        if(d == (long) d)
            return String.format("%d",(long)d);
        else
            return String.format("%s",d);
    }

    /**
     * Use this method to improve appearance of Ingredients, having the first letter capitalized
     * @param input - String
     * @return String
     */
    public static String capitalizeFirstLetter(String input){
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

    private static SimpleIdlingResource sIdlingResource;
    public static IdlingResource getIdlingResource() {
        if (sIdlingResource == null) {
            sIdlingResource = new SimpleIdlingResource();
        }
        return sIdlingResource;
    }

    public static void setIdleResourceTo(boolean isIdleNow){
        if (sIdlingResource == null) {
            sIdlingResource = new SimpleIdlingResource();
        }
        sIdlingResource.setIdleState(isIdleNow);
    }

    /**
     * Check network availability
     *
     * @return true if network is available, false otherwise
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
