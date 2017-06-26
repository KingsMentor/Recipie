package xyz.belvi.recipie.views.widget;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import org.json.JSONException;

import java.util.ArrayList;

import xyz.belvi.recipie.R;
import xyz.belvi.recipie.models.cache.RecipePreference;
import xyz.belvi.recipie.models.pojos.Ingredient;
import xyz.belvi.recipie.models.pojos.Recipe;
import xyz.belvi.recipie.models.pojos.RecipeStep;
import xyz.belvi.recipie.presenters.app.RecipeApplication;
import xyz.belvi.recipie.presenters.interfaces.IntentKeys;
import xyz.belvi.recipie.presenters.network.requests.RecipeRequest;

class GridRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    Context mContext;
    int widgetId;
    ArrayList<Recipe> recipes = new ArrayList<>();


    private boolean isRecipeStepPref() {
        widgetId = new RecipePreference().getWidgetToUpdateId(mContext);
        return new RecipePreference().getWidgetPref(mContext, widgetId) == IntentKeys.RECIPE_STEPS;
    }

    public GridRemoteViewsFactory(Context applicationContext) {
        mContext = applicationContext;
    }

    @Override
    public void onCreate() {
    }

    //called on start and when notifyAppWidgetViewDataChanged is called
    @Override
    public void onDataSetChanged() {
        String recipeJson = new RecipePreference().getCacheRecipe(RecipeApplication.getInstance().getApplicationContext());
        try {
            recipes = RecipeRequest.generateRecipeFromJson(recipeJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onDestroy() {
    }

    @Override
    public int getCount() {
        return recipes.size();
    }

    /**
     * This method acts like the onBindViewHolder method in an Adapter
     *
     * @param position The current position of the item in the GridView to be displayed
     * @return The RemoteViews object to display for the provided postion
     */
    @Override
    public RemoteViews getViewAt(int position) {


        Recipe recipe = recipes.get(position);
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.recipe_widget_items);

        views.setTextViewText(R.id.recipe_name, recipe.getName());
        views.setTextViewText(R.id.recipe_widget_item_detail, generateDetails(recipe));

        Bundle extras = new Bundle();
        extras.putLong(IntentKeys.SelectedPositionKey, position);
        extras.putParcelable(IntentKeys.RecipeIntentKey, recipe);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        views.setOnClickFillInIntent(R.id.widget_item, fillInIntent);
        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return new RemoteViews(mContext.getPackageName(), R.layout.widget_loading);
    }

    @Override
    public int getViewTypeCount() {
        return 1; // Treat all items in the GridView the same
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }


    private String generateDetails(Recipe recipe) {
        StringBuilder details = new StringBuilder();
        if (isRecipeStepPref()) {
            for (RecipeStep step : recipe.getSteps()) {
                details.append("\u25CF");
                details.append("\t");
                details.append(step.getShortDescription());
                details.append("\n");
            }
        } else {
            for (Ingredient ingredient : recipe.getIngredients()) {
                details.append("\u25CF");
                details.append("\t");
                details.append(ingredient.getIngredient());
                details.append(" ( ");
                details.append(ingredient.getQuantity() + " ");
                details.append(ingredient.getMeasure());
                details.append(" )");
                details.append("\n");
            }
        }
        return details.toString();

    }
}