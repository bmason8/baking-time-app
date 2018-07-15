package com.example.android.bakingtime.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.android.bakingtime.MainActivity;
import com.example.android.bakingtime.R;
import com.example.android.bakingtime.database.RecipeDatabase;
import com.example.android.bakingtime.model.Recipe;

import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeIngredientsProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_ingredients_widget);

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        views.setOnClickPendingIntent(R.id.appwidget_recipe_title, pendingIntent);

        RecipeDatabase mDatabase = Room.databaseBuilder(context, RecipeDatabase.class, "Recipe_db").allowMainThreadQueries().build();
//        Recipe recipe = mDatabase.recipeDao().getRecipeById(1);
        List<Recipe> recipeList = mDatabase.recipeDao().getAllRecipes();
        Recipe recipe = recipeList.get(0);
        String recipeTitle = recipe.getName();

        CharSequence widgetText = recipeTitle;

        views.setTextViewText(R.id.appwidget_recipe_title, widgetText);




        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
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

