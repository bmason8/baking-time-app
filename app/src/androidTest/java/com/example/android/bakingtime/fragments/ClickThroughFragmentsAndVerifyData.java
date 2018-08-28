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
public class ClickThroughFragmentsAndVerifyData {

    private final static String RECIPE_STEP_SHORT_DESCRIPTION_TEXT = "Add sugars to wet mixture.";

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule
            = new ActivityTestRule<>(MainActivity.class);
    private MainActivity mMainActivity = null;

    @Before
    public void setUp() throws Exception {
        mMainActivity = mActivityTestRule.getActivity();
    }

    @Test
    public void testClickThroughFromRecipeCardsToStepInstructionFragmentAndVerifyCorrectRecipeStepName() {
        // click on RecipeCard recyclerView at given position
        onView(ViewMatchers.withId(R.id.recipe_cards_recyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, ViewActions.click()));
        // click on RecipeSteps recyclerView at given position
        onView(ViewMatchers.withId(R.id.recipe_steps_recyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(3, ViewActions.click()));
        // check that the short description is correct
        onView(withId(R.id.short_description))
                .check(matches(withText(RECIPE_STEP_SHORT_DESCRIPTION_TEXT)));
    }

    @After
    public void tearDown() throws Exception {
        mMainActivity = null;
    }
}