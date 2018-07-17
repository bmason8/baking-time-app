package com.example.android.bakingtime.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.arch.persistence.room.Room;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.android.bakingtime.MainActivity;
import com.example.android.bakingtime.R;
import com.example.android.bakingtime.database.RecipeDatabase;
import com.example.android.bakingtime.model.Recipe;
import com.example.android.bakingtime.utilities.Constants;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeIngredientsProvider extends AppWidgetProvider {

    private RemoteViews updateWidget(Context context) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_ingredients_widget);

        int id = 2;

        // Get recipe name from database
        RecipeDatabase mDatabase = Room.databaseBuilder(context, RecipeDatabase.class, "Recipe_db").allowMainThreadQueries().build();
        Recipe recipe = mDatabase.recipeDao().getRecipeById(id);
        String recipeName = recipe.getName();

        if (id > 0) {

            views.setTextViewText(R.id.appwidget_recipe_name, recipeName);

            // This launches MainActivity when the recipe title is clicked
            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra(Constants.RECIPE_INTENT_SOURCE, Constants.INTENT_FROM_WIDGET_CLICK);
            intent.putExtra(Constants.RECIPE_WIDGET_ID, id);
            intent.putExtra(Constants.RECIPE_WIDGET_NAME, recipeName);
            PendingIntent pendingIntent = PendingIntent.getActivities(context, id, new Intent[]{intent}, 0);
            views.setOnClickPendingIntent(R.id.appwidget_recipe_name, pendingIntent);

            Intent serviceIntent = new Intent(context, WidgetService.class);
            views.setRemoteAdapter(R.id.widget_list_view, serviceIntent);
        }

        return views;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, RecipeIngredientsProvider.class));

        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list_view);
        for (int appWidgetId:appWidgetIds) {
            appWidgetManager.partiallyUpdateAppWidget(appWidgetId, updateWidget(context));
        }

        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
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

