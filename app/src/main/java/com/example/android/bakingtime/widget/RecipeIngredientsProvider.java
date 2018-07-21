package com.example.android.bakingtime.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.arch.persistence.room.Room;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.android.bakingtime.MainActivity;
import com.example.android.bakingtime.R;
import com.example.android.bakingtime.database.RecipeDatabase;
import com.example.android.bakingtime.model.Recipe;
import com.example.android.bakingtime.utilities.Constants;

import java.util.Objects;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeIngredientsProvider extends AppWidgetProvider {

    private RemoteViews updateWidget(Context context) {

//        Toast.makeText(context, "updateWidget ran", Toast.LENGTH_LONG).show();

        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        int id = mPreferences.getInt(Constants.WIDGET_RECIPE_ID, 1);


        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_ingredients_widget);


        // Get recipe name from database
        RecipeDatabase mDatabase = Room.databaseBuilder(context, RecipeDatabase.class, "Recipe_db").allowMainThreadQueries().build();
        Recipe recipe = mDatabase.recipeDao().getRecipeById(id);
        String recipeName = recipe.getName();
        mDatabase.close();

            views.setTextViewText(R.id.widget_recipe_name, recipeName);

            // This launches MainActivity when the recipe title is clicked
            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra(Constants.RECIPE_INTENT_SOURCE, Constants.INTENT_FROM_WIDGET_CLICK);
            intent.putExtra(Constants.RECIPE_WIDGET_ID, id);
            intent.putExtra(Constants.RECIPE_WIDGET_NAME, recipeName);
            PendingIntent pendingIntent = PendingIntent.getActivities(context, 0, new Intent[]{intent}, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.widget_recipe_name, pendingIntent);

            // Intent/Pending Intent for moving to next recipe
            Intent nextBtnIntent = new Intent(context, RecipeIngredientsProvider.class);
            nextBtnIntent.putExtra("direction", "forward");
            nextBtnIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            PendingIntent nextBtnPendingIntent = PendingIntent.getBroadcast(context, 0, nextBtnIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.widget_next_recipe_btn, nextBtnPendingIntent);

            // Intent/Pending Intent for moving to previous recipe
            Intent previousBtnIntent = new Intent(context, RecipeIngredientsProvider.class);
            previousBtnIntent.putExtra("direction", "previous");
            previousBtnIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            PendingIntent previousBtnPendingIntent = PendingIntent.getBroadcast(context, 0, previousBtnIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.widget_previous_recipe_btn, previousBtnPendingIntent);

            Intent serviceIntent = new Intent(context, WidgetService.class);
            views.setRemoteAdapter(R.id.widget_list_view, serviceIntent);

        return views;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        Bundle extras = intent.getExtras();
        String direction = null;
        if (extras != null) {
            direction = extras.getString("direction", "previous");
        }

        if (Objects.equals(action, AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {

            if (Objects.equals(direction, "forward")) {

                SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                int id = mPreferences.getInt(Constants.WIDGET_RECIPE_ID, 1);
                int updatedId = id + 1;
                if (updatedId > 4) {
                    updatedId = 1;
                }

                mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putInt(Constants.WIDGET_RECIPE_ID, updatedId);
                editor.apply();

                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, RecipeIngredientsProvider.class));

                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list_view);
                for (int appWidgetId : appWidgetIds) {
                    appWidgetManager.partiallyUpdateAppWidget(appWidgetId, updateWidget(context));
                }
            } else if (Objects.equals(direction, "previous")){
                SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                int id = mPreferences.getInt(Constants.WIDGET_RECIPE_ID, 1);
                int updatedId = id - 1;
                if (updatedId < 1) {
                    updatedId = 4;
                }

                mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putInt(Constants.WIDGET_RECIPE_ID, updatedId);
                editor.apply();

                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, RecipeIngredientsProvider.class));

                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list_view);
                for (int appWidgetId : appWidgetIds) {
                    appWidgetManager.partiallyUpdateAppWidget(appWidgetId, updateWidget(context));
                }
            }
        }
        else {

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, RecipeIngredientsProvider.class));

            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list_view);
            for (int appWidgetId : appWidgetIds) {
                appWidgetManager.partiallyUpdateAppWidget(appWidgetId, updateWidget(context));
            }

            super.onReceive(context, intent);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        Toast.makeText(context, "onUpdate ran", Toast.LENGTH_LONG).show();
        appWidgetManager.updateAppWidget(appWidgetIds, updateWidget(context));
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

