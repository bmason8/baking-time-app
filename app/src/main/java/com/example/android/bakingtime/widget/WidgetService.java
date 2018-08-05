package com.example.android.bakingtime.widget;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.bakingtime.R;
import com.example.android.bakingtime.database.RecipeDatabase;
import com.example.android.bakingtime.model.Ingredients;
import com.example.android.bakingtime.model.Recipe;
import com.example.android.bakingtime.utilities.Constants;

import java.text.DecimalFormat;
import java.util.List;

// https://blog.fossasia.org/creating-a-widget-for-your-android-app/
// https://stackoverflow.com/questions/22262867/android-widget-with-listview-doesnt-load-items-correclty
public class WidgetService extends RemoteViewsService {

    Recipe mRecipe;
    List<Recipe> mRecipeList;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Log.d("testWS: ", "onGetViewFactory");
        return new WidgetRemoteViewsFactory(this.getApplicationContext());
    }

    class WidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
        final Context mContext;

        List<Ingredients> mIngredientsList;

        Ingredients mIngredient;
        int tempRecipeId;

        WidgetRemoteViewsFactory(Context applicationContext) {
            mContext = applicationContext.getApplicationContext();
        }

        @Override
        public void onCreate() {
            Log.d("testWS: ", "onCreate");
        }

        @Override
        public void onDataSetChanged() {
            // Need to change this so the recipeId is stored in PreferencesManager
            SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            int recipeId = mPreferences.getInt(Constants.WIDGET_RECIPE_ID, 1);
            tempRecipeId = recipeId;
            Log.d("testWS: ", "onDataSetChangedStarted");

//            int recipeId = 2;
            if (mRecipeList == null) {
                RecipeDatabase mRecipeDatabase = Room.databaseBuilder(getApplicationContext(), RecipeDatabase.class, "Recipe_db").allowMainThreadQueries().build();
                mRecipeList = mRecipeDatabase.recipeDao().getAllRecipes();
                Log.d("testWS: ", "if_populatemRecipeList");
                mRecipeDatabase.close();
            }

                mRecipe = mRecipeList.get(recipeId);
                Log.d("testWS: ", "populatemRecipe");

//            mRecipe = mRecipeDatabase.recipeDao().getRecipeById(recipeId);

//            if (mRecipe == null) {
//                new GetRecipeById().execute(recipeId);
//            }


//            mRecipe = RoomAccess.getRecipeById(recipeId, getApplicationContext());

//            mRecipeViewModel.getRecipe(recipeId).observe((LifecycleOwner) getApplicationContext(), new Observer<Recipe>() {
//                @Override
//                public void onChanged(@Nullable Recipe recipe) {
//                    mRecipe = recipe;
//                }
//            });

            if (mRecipe != null) {
                mIngredientsList = mRecipe.getIngredients();
            }
            Log.d("testWS: ", "onDataSetChangedFinished");
        }

        @Override
        public void onDestroy() {
//            mRecipeDatabase.close();
            Log.d("testWS: ", "onDestroy");
        }

        @Override
        public int getCount() {
            if (mIngredientsList == null) return 0;
            Log.d("testWS: ", "getCount");
            return mIngredientsList.size();

        }

        @Override
        public RemoteViews getViewAt(int position) {
            if (mIngredientsList == null || mIngredientsList.size() == 0) return null;

            mIngredient = mIngredientsList.get(position);

            double ingredientQuantityLong = mIngredient.getQuantity();
            // https://stackoverflow.com/questions/8065114/how-to-print-a-double-with-two-decimals-in-android
            DecimalFormat quantityDecimalFormatter = new DecimalFormat("#.##");
            String ingredientQuantity = String.valueOf(quantityDecimalFormatter.format(ingredientQuantityLong));

            String ingredientMeasure = "(" + mIngredient.getMeasure() + ")";
            String ingredientName = mIngredient.getIngredient();

            RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_listview_item);

            views.setTextViewText(R.id.widget_ingredient_quantity, ingredientQuantity);
            views.setTextViewText(R.id.widget_ingredient_measurement, ingredientMeasure);
            views.setTextViewText(R.id.widget_ingredient_name, ingredientName);

            Log.d("testWS: ", "getViewAt");
            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int i) {
            Log.d("testWS: ", "getItemId: " + String.valueOf(i));
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }

    private class GetRecipeById extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(Integer... position) {
            int post = position[0];
            RecipeDatabase mDatabase = Room.databaseBuilder(getApplicationContext(), RecipeDatabase.class, "Recipe_db").build();
            mRecipe = mDatabase.recipeDao().getRecipeById(post);
            Log.d("testWS: ", "GetRecipeBy");
            mDatabase.close();
            return null;
        }
    }

}
