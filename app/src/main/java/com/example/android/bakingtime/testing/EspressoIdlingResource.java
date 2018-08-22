package com.example.android.bakingtime.testing;

import android.support.test.espresso.IdlingResource;

// Figure this IdlingResource approach out using this tutorial...
// http://myhexaville.com/2018/01/22/android-espresso-idling-resouces-firebase/
public class EspressoIdlingResource {
    private static final String RESOURCE = "GLOBAL";

    private static SimpleCountingIdlingResource mCountingIdlingResource =
            new SimpleCountingIdlingResource(RESOURCE);

    public static void increment() {
        mCountingIdlingResource.increment();
    }

    public static void decrement() {
        mCountingIdlingResource.decrement();
    }

    public static IdlingResource getIdlingResource() {
        return mCountingIdlingResource;
    }
}
