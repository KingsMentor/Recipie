package xyz.belvi.recipie.presenters.app;

import android.app.Application;

import xyz.belvi.recipie.presenters.handlers.RecipeDataHandler;
import xyz.belvi.recipie.presenters.network.volley.VolleyHelper;

/**
 * Created by zone2 on 6/19/17.
 */

public class RecipeApplication extends Application {

    private static RecipeApplication recipeApplication;
    private RecipeDataHandler recipeDataHandler;
    private VolleyHelper volleyHelper;

    public static synchronized RecipeApplication getInstance() {
        return recipeApplication;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        if (recipeApplication == null) {
            recipeApplication = this;
        }
        initVolley();
        initRecipeData();

    }


    public RecipeDataHandler getRecipeDataHandler() {
        return this.recipeDataHandler;
    }


    private void initRecipeData() {
        recipeDataHandler = new RecipeDataHandler();
        recipeDataHandler.loadData();
    }

    private void initVolley() {
        volleyHelper = new VolleyHelper();
        volleyHelper.initVolley(this);
    }

    public VolleyHelper getVolleyHelper() {
        return volleyHelper;
    }


}

