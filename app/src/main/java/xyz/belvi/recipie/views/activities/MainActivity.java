package xyz.belvi.recipie.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;

import xyz.belvi.recipie.R;
import xyz.belvi.recipie.espresso.IdlingResourceSample.IdlingResource.SimpleIdlingResource;
import xyz.belvi.recipie.espresso.IdlingResourceSample.MessageDelayer;
import xyz.belvi.recipie.models.pojos.Recipe;
import xyz.belvi.recipie.presenters.app.RecipeApplication;
import xyz.belvi.recipie.presenters.interfaces.IntentKeys;
import xyz.belvi.recipie.presenters.interfaces.RecipeController;
import xyz.belvi.recipie.views.adapters.RecipeAdapter;

public class MainActivity extends AppCompatActivity implements RecipeController,
        MessageDelayer.DelayerCallback {


    private RecyclerView recipeRecyclerView;
    private SimpleIdlingResource mIdlingResource;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        initRecipeRecyclerView();
        RecipeApplication.getInstance().getRecipeDataHandler().init(this);
        initRetry();

    }

    private void initRetry() {
        findViewById(R.id.retry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecipeApplication.getInstance().getRecipeDataHandler().loadData();
            }
        });
    }

    private void initRecipeRecyclerView() {
        recipeRecyclerView = (RecyclerView) findViewById(R.id.recipe_list);
        recipeRecyclerView.setHasFixedSize(true);
        if (getResources().getBoolean(R.bool.is_tab) || getResources().getBoolean(R.bool.landscape)) {
            recipeRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), calculateNoOfColumns()));
        } else {
            recipeRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        }
    }

    public int calculateNoOfColumns() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 200);
        return noOfColumns;
    }

    @Override
    public void recipeAdapter(RecipeAdapter adapter) {
        findViewById(R.id.loading_layout).setVisibility(View.GONE);
        findViewById(R.id.empty_view).setVisibility(View.GONE);
        recipeRecyclerView.setAdapter(adapter);
        MessageDelayer.processMessage(this, mIdlingResource);

    }

    @Override
    public void recipeSelected(Recipe recipe) {
        startActivity(new Intent(this, RecipeDetailsActivity.class)
                .putExtra(IntentKeys.RecipeIntentKey, recipe)
        );
    }

    @Override
    public void failedToLoad(String errorMessage) {
        findViewById(R.id.loading_layout).setVisibility(View.GONE);
        findViewById(R.id.recipe_list).setVisibility(View.GONE);
        findViewById(R.id.empty_view).setVisibility(View.VISIBLE);
        MessageDelayer.processMessage(this, mIdlingResource);
    }

    @Override
    public void loadingRecipes() {
        findViewById(R.id.loading_layout).setVisibility(View.VISIBLE);
    }

    @VisibleForTesting
    public boolean isSuccessful() {
        return recipeRecyclerView.getAdapter() != null;
    }

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    @Override
    public void onDone() {

    }


}
