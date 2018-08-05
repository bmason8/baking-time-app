package com.example.android.bakingtime.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.arch.persistence.room.Room;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.android.bakingtime.MainActivity;
import com.example.android.bakingtime.R;
import com.example.android.bakingtime.database.RecipeDatabase;
import com.example.android.bakingtime.model.Recipe;
import com.example.android.bakingtime.utilities.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeIngredientsProvider extends AppWidgetProvider {

    List<Recipe> mRecipeList = new ArrayList<>();
    Recipe mRecipe;
    Context mContext;
    String recipeName;
    int mRecipeId;

    private RemoteViews updateWidget(final Context context) {
        mContext = context;
        Log.d("testRP: ", "updateWidget");
//        mRecipeList = new ArrayList<Recipe>();

        if (mRecipeList.isEmpty()) {
            Toast.makeText(context,"mRecipeList was null", Toast.LENGTH_SHORT).show();
            try {
                mRecipeList = new GetAllRecipes().execute().get();
                Log.d("testRP: ", "newGetAllRecipesRan");
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        final int position = mPreferences.getInt(Constants.WIDGET_RECIPE_ID, 1);
        Log.d("idFromPreferences: ", "id: " + String.valueOf(position));


        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_ingredients_widget);

        mRecipe = mRecipeList.get(position);
        mRecipeId = mRecipe.getId();
        recipeName = mRecipe.getName();
        Log.d("testRP: ", "mRecipe.getName: " + recipeName);

            views.setTextViewText(R.id.widget_recipe_name, recipeName);

            // This launches MainActivity when the recipe title is clicked
            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra(Constants.RECIPE_INTENT_SOURCE, Constants.INTENT_FROM_WIDGET_CLICK);
            Log.d("ID:mRecipeId: ", String.valueOf(mRecipeId));
            intent.putExtra(Constants.RECIPE_WIDGET_ID, mRecipeId);
            intent.putExtra(Constants.RECIPE_WIDGET_NAME, recipeName);
            PendingIntent pendingIntent = PendingIntent.getActivities(context, 0, new Intent[]{intent}, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.widget_recipe_name, pendingIntent);

            // Intent/Pending Intent for moving to next recipe
            Intent nextBtnIntent = new Intent(context, RecipeIngredientsProvider.class);
            nextBtnIntent.putExtra("direction", "forward");
            nextBtnIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            PendingIntent nextBtnPendingIntent = PendingIntent.getBroadcast(context, 1, nextBtnIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.widget_next_recipe_btn, nextBtnPendingIntent);

            // Intent/Pending Intent for moving to previous recipe
            Intent previousBtnIntent = new Intent(context, RecipeIngredientsProvider.class);
            previousBtnIntent.putExtra("direction", "previous");
            previousBtnIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            PendingIntent previousBtnPendingIntent = PendingIntent.getBroadcast(context, 2, previousBtnIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.widget_previous_recipe_btn, previousBtnPendingIntent);

            Intent serviceIntent = new Intent(context, WidgetService.class);
            views.setRemoteAdapter(R.id.widget_list_view, serviceIntent);

        return views;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String direction = null;

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, RecipeIngredientsProvider.class));

        String action = intent.getAction();
        Bundle extras = intent.getExtras();
            if (extras != null) {
                direction = extras.getString("direction", "forward");
            }

        if (Objects.equals(action, AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {

            if (Objects.equals(direction, "forward")) {

                int position = getSavedWidgetId(context);
                Log.d("testRP", "idFromgetSavedWidgetId: " + String.valueOf(position));
                int updatedPosition = position + 1;
                Log.d("testRP", "updatedId: " + String.valueOf(updatedPosition));
                if (updatedPosition > 3) {
                    updatedPosition = 0;
                }

                mRecipeId = updatedPosition;
                Log.d("ID:mRecipeIdF", String.valueOf(updatedPosition));

                updateSavedWidgetId(context, updatedPosition);

//                Toast.makeText(context, "forward: " + String.valueOf(updatedId), Toast.LENGTH_LONG).show();

                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list_view);
                for (int appWidgetId : appWidgetIds) {
                    appWidgetManager.partiallyUpdateAppWidget(appWidgetId, updateWidget(context));
                }
                Log.d("testRP: ", "IfOnReceive");

            } else if (Objects.equals(direction, "previous")){
                SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                int position = mPreferences.getInt(Constants.WIDGET_RECIPE_ID, 1);
                int updatedPosition = position - 1;
                if (updatedPosition < 0) {
                    updatedPosition = 3;
                }
                mRecipeId = updatedPosition;
                Log.d("ID:mRecipeIdB", String.valueOf(updatedPosition));

                updateSavedWidgetId(context, updatedPosition);

//                Toast.makeText(context, "previous: " + String.valueOf(updatedId), Toast.LENGTH_LONG).show();

                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list_view);
                for (int appWidgetId : appWidgetIds) {
                    appWidgetManager.partiallyUpdateAppWidget(appWidgetId, updateWidget(context));
                }
            }
        }
        else {

//            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
//            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, RecipeIngredientsProvider.class));

            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list_view);
            for (int appWidgetId : appWidgetIds) {
                appWidgetManager.partiallyUpdateAppWidget(appWidgetId, updateWidget(context));
            }

            Log.d("testRP: ", "ElseOnReceive");
            super.onReceive(context, intent);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        appWidgetManager.updateAppWidget(appWidgetIds, updateWidget(context));
        Log.d("testRP: ", "onUpdate");
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        Log.d("testRP: ", "onEnabled");
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    private int getSavedWidgetId(Context context) {
        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Log.d("testRP: ", "getSavedWidgetId");
        return mPreferences.getInt(Constants.WIDGET_RECIPE_ID, 1);
    }

    private void updateSavedWidgetId(Context context, int updatedId) {
        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(Constants.WIDGET_RECIPE_ID, updatedId);
        editor.apply();
        Log.d("testRP: ", "updateSavedWidgetId");
    }

    private class GetAllRecipes extends AsyncTask<Void, Void, ArrayList<Recipe>> {

        @Override
        protected ArrayList<Recipe> doInBackground(Void... voids) {
            RecipeDatabase mDatabase = Room.databaseBuilder(mContext, RecipeDatabase.class, "Recipe_db").build();
            mRecipeList = mDatabase.recipeDao().getAllRecipes();
            Log.d("testRP: ", "doInBackground");
            mDatabase.close();
            return (ArrayList<Recipe>) mRecipeList;
        }

        @Override
        protected void onPostExecute(ArrayList<Recipe> recipeArrayList) {
            mRecipeList = recipeArrayList;
        }
    }

}

