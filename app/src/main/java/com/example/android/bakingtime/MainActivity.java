package com.example.android.bakingtime;

import android.arch.persistence.room.Room;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.android.bakingtime.database.RecipeDatabase;
import com.example.android.bakingtime.database.RoomAccess;
import com.example.android.bakingtime.fragments.RecipeCardsFragment;
import com.example.android.bakingtime.fragments.RecipeStepsFragment;
import com.example.android.bakingtime.model.Recipe;
import com.example.android.bakingtime.utilities.ApiInterface;
import com.example.android.bakingtime.utilities.Constants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.android.bakingtime.utilities.ApiInterface.BAKING_RECIPE_JSON_URL;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    RecipeCardsFragment recipeCardsFragment;

    private List<Recipe> mRecipeList;
    private Recipe mRecipe;
    private int recipeId;
    private boolean setUpNewFragment;
    private boolean isFromWidget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                int stackHeight = getSupportFragmentManager().getBackStackEntryCount();
                if (stackHeight > 0) { // if we have something on the stack (doesn't include the current shown fragment)
                    getSupportActionBar().setHomeButtonEnabled(true);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                } else {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    getSupportActionBar().setHomeButtonEnabled(false);
                }
            }

        });

        mRecipeList = new ArrayList<>();

        if (savedInstanceState == null) {
            setUpNewFragment = true;

            // Check the database for Recipes. If not null then get them, if not then run fetchRecipes to download them.
            new GetAllRecipesFromDb().execute();

        } else {
            savedInstanceState.getSerializable("recipeList");
        }


        if (getIntent() != null && getIntent().getExtras() != null) {

            if (Objects.equals(getIntent().getStringExtra(Constants.RECIPE_INTENT_SOURCE), Constants.INTENT_FROM_WIDGET_CLICK)) {

                isFromWidget = true;
                recipeId = getIntent().getIntExtra(Constants.RECIPE_WIDGET_ID, 1);
                new GetRecipeById().execute();
            }
        }
    }

    private void startRecipeStepsFragmentFromWidgetClick(Recipe recipe) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        RecipeStepsFragment recipeStepsFragment = new RecipeStepsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("recipe", recipe);
        recipeStepsFragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.frame_fragment_holder, recipeStepsFragment, "TAG")
                .commit();
    }

    private void fetchRecipes() {

        // Retrofit set up
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BAKING_RECIPE_JSON_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        Call<List<Recipe>> call = apiInterface.getRecipes();

        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(@NonNull Call<List<Recipe>> call, @NonNull Response<List<Recipe>> response) {
                List<Recipe> result = response.body();
                if (result != null) {
//                    mAdapter.setRecipesList(result);
                    mRecipeList = result;
                    if (setUpNewFragment) {
                        setUpRecipeCardsFragment();
                    }

                    // Add all recipes to database
                    // http://androidkt.com/room-persistence-library/
                    RoomAccess.insertRecipes(mRecipeList, getApplicationContext());

                } else {
                    Toast.makeText(MainActivity.this, R.string.failed_to_fetch_error, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("fail: ", t.getMessage());
            }
        });
    }


    private void setUpRecipeCardsFragment() {
        recipeCardsFragment = new RecipeCardsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("recipeList", (Serializable) mRecipeList);
        recipeCardsFragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.frame_fragment_holder, recipeCardsFragment).commit();
    }

    private class GetRecipeById extends AsyncTask<Void, Void, Recipe> {

        @Override
        protected Recipe doInBackground(Void... voids) {
            RecipeDatabase mDatabase = Room.databaseBuilder(getApplicationContext(), RecipeDatabase.class, getString(R.string.recipe_db_name)).build();
            mRecipe = mDatabase.recipeDao().getRecipeById(recipeId);
            mDatabase.close();
            return mRecipe;
        }

        @Override
        protected void onPostExecute(Recipe recipe) {
            startRecipeStepsFragmentFromWidgetClick(recipe);
        }
    }

    private class GetAllRecipesFromDb extends AsyncTask<Void, Void, List<Recipe>> {

        @Override
        protected List<Recipe> doInBackground(Void... voids) {
            RecipeDatabase mDatabase = Room.databaseBuilder(getApplicationContext(), RecipeDatabase.class, getString(R.string.recipe_db_name)).build();
            mRecipeList = mDatabase.recipeDao().getAllRecipes();
            mDatabase.close();
            return mRecipeList;
        }

        @Override
        protected void onPostExecute(List<Recipe> recipeList) {
            if (recipeList.isEmpty()) {
                fetchRecipes();
            } else {
                mRecipeList = recipeList;
                if (!isFromWidget) {
                setUpRecipeCardsFragment();
                }
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("recipeList", (Serializable) mRecipeList);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                getSupportFragmentManager().popBackStack();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showUpButton() { getSupportActionBar().setDisplayHomeAsUpEnabled(true); }
    public void hideUpButton() { getSupportActionBar().setDisplayHomeAsUpEnabled(false); }
}
