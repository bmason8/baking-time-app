package com.example.android.bakingtime.fragments;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.FrameLayout;

import com.example.android.bakingtime.MainActivity;
import com.example.android.bakingtime.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertNotNull;

// https://www.youtube.com/watch?v=JoFN10FDm8U
@RunWith(AndroidJUnit4.class)
public class LaunchRecipeCardsFragmentTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule
            = new ActivityTestRule<>(MainActivity.class);
    private MainActivity mMainActivity = null;

    @Before
    public void setUp() throws Exception {
        mMainActivity = mActivityTestRule.getActivity();
    }

    // This test is for verifying that the initial launch of the app fetches data properly by checking that the
    // recyclerView is not null.
    @Test
    public void testRecipeCardsFragmentIsLaunchedAndRecyclerViewIsAdded() {
        // test if fragment is launched
        FrameLayout frameLayout = (FrameLayout) mMainActivity.findViewById(R.id.frame_fragment_holder);
        assertNotNull(frameLayout);

        RecipeCardsFragment recipeCardsFragment = new RecipeCardsFragment();
        mMainActivity.getSupportFragmentManager().beginTransaction().add(frameLayout.getId(), recipeCardsFragment).commitAllowingStateLoss();
        getInstrumentation().waitForIdleSync();
        View view = recipeCardsFragment.getView().findViewById(R.id.recipe_cards_recyclerView);
        assertNotNull(view);
    }

    @After
    public void tearDown() throws Exception {
        mMainActivity = null;
    }
}