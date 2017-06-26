package xyz.belvi.recipie.views.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;

import org.json.JSONException;

import java.util.Random;

import xyz.belvi.recipie.R;
import xyz.belvi.recipie.models.cache.RecipePreference;
import xyz.belvi.recipie.presenters.app.RecipeApplication;
import xyz.belvi.recipie.presenters.interfaces.IntentKeys;
import xyz.belvi.recipie.presenters.network.requests.RecipeRequest;
import xyz.belvi.recipie.views.activities.MainActivity;
import xyz.belvi.recipie.views.activities.RecipeDetailsActivity;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link RecipeWidgetConfigureActivity RecipeWidgetConfigureActivity}
 */
public class RecipeWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = getGardenGridRemoteView(context, appWidgetId);
        views.setTextViewText(R.id.widget_title, String.format(context.getString(R.string.widget_recipe_title), getTitle(context, appWidgetId)));

        // broadcast to update widget id in RemoveViewFactory
        new RecipePreference().setWidgetToUpdateId(context, appWidgetId);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.gv_widget);
    }

    private static String getTitle(Context context, int widgetId) {
        return new RecipePreference().getWidgetPref(context, widgetId) == IntentKeys.RECIPE_STEPS ? "Steps" : "Ingredients";
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
//            RecipeWidgetConfigureActivity.deleteTitlePref(context, appWidgetId);
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

    static RemoteViews getGardenGridRemoteView(Context context, int widgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);

        Intent intent = new Intent(context, GridWidgetService.class);
        int dummyuniqueInt = new Random().nextInt(543254);
        views.setRemoteAdapter(R.id.gv_widget, intent);

        Intent appIntent = new Intent(context, RecipeDetailsActivity.class);
        PendingIntent appPendingIntent = PendingIntent.getActivity(context, dummyuniqueInt, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.gv_widget, appPendingIntent);

        //set pending intent for settings
        Bundle bundle = new Bundle();
        bundle.putInt(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
        Intent settingsIntent = new Intent(context, RecipeWidgetConfigureActivity.class);
        settingsIntent.putExtras(bundle);

        PendingIntent settingsPendingIntent = PendingIntent.getActivity(context, dummyuniqueInt, settingsIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.widget_setting, settingsPendingIntent);

        //set pending intent for empy view
        Intent emptyViewIntent = new Intent(context, MainActivity.class);
        settingsIntent.putExtras(bundle);

        PendingIntent emptyViewPendingIntent = PendingIntent.getActivity(context, dummyuniqueInt, emptyViewIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.empty_view, emptyViewPendingIntent);

        // Handle empty gardens
        views.setEmptyView(R.id.gv_widget, R.id.loading_view);
        toggleVisibility(views, isEmpty());


        return views;
    }

    static void toggleVisibility(RemoteViews views, boolean isEmpty) {
        views.setViewVisibility(R.id.gv_widget, isEmpty ? View.GONE : View.VISIBLE);
        views.setViewVisibility(R.id.empty_view, isEmpty ? View.VISIBLE : View.GONE);
        views.setViewVisibility(R.id.loading_view, isEmpty ? View.GONE : View.VISIBLE);

    }

    static boolean isEmpty() {
        try {
            String recipeJson = new RecipePreference().getCacheRecipe(RecipeApplication.getInstance().getApplicationContext());
            return RecipeRequest.generateRecipeFromJson(recipeJson).size() == 0;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }
}

