package com.example.android.bakingtime.utilities;

import android.net.Uri;

public class NetworkUtils {

    public static Uri convertStringToUri(String mediaString) {
        return Uri.parse(mediaString);
    }
}
