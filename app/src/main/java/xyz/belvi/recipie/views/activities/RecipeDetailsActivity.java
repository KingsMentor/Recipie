package xyz.belvi.recipie.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import xyz.belvi.recipie.R;
import xyz.belvi.recipie.models.pojos.Recipe;
import xyz.belvi.recipie.presenters.interfaces.IntentKeys;
import xyz.belvi.recipie.views.fragments.RecipeStepDetailsFragment;
import xyz.belvi.recipie.views.fragments.RecipeStepInfoFragment;

/**
 * Created by zone2 on 6/19/17.
 */

public class RecipeDetailsActivity extends AppCompatActivity {


    private Recipe getRecipe(Intent intent) {
        return intent.getParcelableExtra(IntentKeys.RecipeIntentKey);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_details);
        Recipe recipe = getRecipe(getIntent());
        setActionBar(recipe);
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            if (getResources().getBoolean(R.bool.is_tab)) {
                fragmentManager.beginTransaction().replace(R.id.fl_recipe_step_info, new RecipeStepInfoFragment().newInstance(recipe)).commitAllowingStateLoss();
                fragmentManager.beginTransaction().replace(R.id.fl_recipe_step_details, new RecipeStepDetailsFragment().newInstance(recipe, 0)).commitAllowingStateLoss();

            } else {
                fragmentManager.beginTransaction()
                        .replace(R.id.fl_recipe_step, new RecipeStepInfoFragment().newInstance(recipe)).commitAllowingStateLoss();

            }
        }

    }

    private void setActionBar(Recipe recipe) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(recipe.getName());
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
