package xyz.belvi.recipie.models.cache;

import android.appwidget.AppWidgetManager;
import android.content.Context;

import xyz.belvi.recipie.presenters.interfaces.IntentKeys;

/**
 * Created by zone2 on 6/19/17.
 */

public class RecipePreference {

    private final String CACHE_RECIPE_KEY = "RecipePreference.CACHE_RECIPE_KEY";
    private final String WIDGET_PREF_KEY = "RecipePreference.WIDGET_PREF_KEY";
    private final String WIDGET_ID_PREF_KEY = "RecipePreference.WIDGET_ID_PREF_KEY";

    public void cacheRecipe(Context context, String recipeJson) {
        context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE).edit().putString(CACHE_RECIPE_KEY, recipeJson).commit();

    }

    public String getCacheRecipe(Context context) {
        return context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE).getString(CACHE_RECIPE_KEY, "[]");
    }

    public void setWidgetPref(Context context, int type, int widgetId) {
        context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE).edit().putInt(WIDGET_PREF_KEY + "." + widgetId, type).commit();

    }

    public int getWidgetPref(Context context, int widgetId) {
        return context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE).getInt(WIDGET_PREF_KEY + "." + widgetId, IntentKeys.RECIPE_INGREDIENTS);
    }

    public void setWidgetToUpdateId(Context context, int widgetId) {
        context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE).edit().putInt(WIDGET_ID_PREF_KEY, widgetId).commit();

    }

    public int getWidgetToUpdateId(Context context) {
        return context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE).getInt(WIDGET_ID_PREF_KEY, AppWidgetManager.INVALID_APPWIDGET_ID);
    }

}
