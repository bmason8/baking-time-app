package com.example.android.bakingtime.widget;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.bakingtime.R;
import com.example.android.bakingtime.database.RecipeDatabase;
import com.example.android.bakingtime.model.Ingredients;
import com.example.android.bakingtime.model.Recipe;

import java.text.DecimalFormat;
import java.util.List;

// https://blog.fossasia.org/creating-a-widget-for-your-android-app/
// https://stackoverflow.com/questions/22262867/android-widget-with-listview-doesnt-load-items-correclty
public class WidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetRemoteViewsFactory(this.getApplicationContext());
    }

    class WidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
        final Context mContext;
//        RecipeDatabase mRecipeDatabase;
        RecipeDatabase mRecipeDatabase = Room.databaseBuilder(getApplicationContext(), RecipeDatabase.class, "Recipe_db").allowMainThreadQueries().build();
        List<Recipe> mRecipeList;
        List<Ingredients> mIngredientsList;
        Recipe mRecipe;
        Ingredients mIngredient;

        WidgetRemoteViewsFactory(Context applicationContext) {
            mContext = applicationContext.getApplicationContext();
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            // Need to change this so the recipeId is stored in PreferencesManager
            int recipeId = 2;
            mRecipe = mRecipeDatabase.recipeDao().getRecipeById(recipeId);
            mIngredientsList = mRecipe.getIngredients();
        }

        @Override
        public void onDestroy() {
            mRecipeDatabase.close();
        }

        @Override
        public int getCount() {
            if (mIngredientsList == null) return 0;
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
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }

}
