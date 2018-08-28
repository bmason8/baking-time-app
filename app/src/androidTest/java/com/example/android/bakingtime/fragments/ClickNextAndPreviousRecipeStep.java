package com.example.android.bakingtime.fragments;

import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.android.bakingtime.MainActivity;
import com.example.android.bakingtime.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class ClickNextAndPreviousRecipeStep {

    private final static String RECIPE_STEP_NEXT_SHORT_DESCRIPTION_TEXT = "Mix together dry ingredients.";
    private final static String RECIPE_STEP_PREVIOUS_SHORT_DESCRIPTION_TEXT = "Melt butter and bittersweet chocolate.";

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule
            = new ActivityTestRule<>(MainActivity.class);
            private MainActivity mMainActivity = null;

    @Before
    public void setUp() throws Exception {
        mMainActivity = mActivityTestRule.getActivity();
    }

    // These tests must be performed in portrait mode because the next/previous buttons are removed from view
    // when the app is in landscape mode.
    @Test
    public void verifyClickingNextLoadsCorrectData() {
        // click on RecipeCard recyclerView at given position
        onView(ViewMatchers.withId(R.id.recipe_cards_recyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, ViewActions.click()));
        // click on RecipeSteps recyclerView at given position
        onView(ViewMatchers.withId(R.id.recipe_steps_recyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(3, ViewActions.click()));
        // check that clicking the next button loads the correct next step
        onView(withId(R.id.next_button)).perform(ViewActions.click());
        onView(withId(R.id.short_description))
                .check(matches(withText(RECIPE_STEP_NEXT_SHORT_DESCRIPTION_TEXT)));
    }

    @Test
    public void verifyClickingPreviousLoadsCorrectData() {
        // click on RecipeCard recyclerView at given position
        onView(ViewMatchers.withId(R.id.recipe_cards_recyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, ViewActions.click()));
        // click on RecipeSteps recyclerView at given position
        onView(ViewMatchers.withId(R.id.recipe_steps_recyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(3, ViewActions.click()));
        // check that clicking the next button loads the correct next step
        onView(withId(R.id.previous_button)).perform(ViewActions.click());
        onView(withId(R.id.short_description))
                .check(matches(withText(RECIPE_STEP_PREVIOUS_SHORT_DESCRIPTION_TEXT)));
    }

    @After
    public void tearDown() throws Exception {
        mMainActivity = null;
    }
}
