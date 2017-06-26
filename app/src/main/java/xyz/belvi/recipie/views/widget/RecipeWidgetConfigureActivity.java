package xyz.belvi.recipie.views.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatRadioButton;
import android.view.View;

import xyz.belvi.recipie.R;
import xyz.belvi.recipie.models.cache.RecipePreference;
import xyz.belvi.recipie.presenters.interfaces.IntentKeys;

/**
 * The configuration screen for the {@link RecipeWidget RecipeWidget} AppWidget.
 */
public class RecipeWidgetConfigureActivity extends Activity {


    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;


    public RecipeWidgetConfigureActivity() {
        super();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        setContentView(R.layout.recipe_widget_configure);
        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//
//        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        initSelect(R.id.widget_ingredient_pref, R.id.widget_steps_pref);

    }

    private void initSelect(int... ids) {
        boolean prefIsIngredient = new RecipePreference().getWidgetPref(this, mAppWidgetId) == IntentKeys.RECIPE_INGREDIENTS;
        boolean prefIsStep = new RecipePreference().getWidgetPref(this, mAppWidgetId) == IntentKeys.RECIPE_STEPS;
        ((AppCompatRadioButton) findViewById(R.id.widget_ingredient_pref)).setChecked(prefIsIngredient);
        ((AppCompatRadioButton) findViewById(R.id.widget_steps_pref)).setChecked(prefIsStep);

        for (int id : ids) {
            findViewById(id).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (((AppCompatRadioButton) v).isChecked()) {
                        if (v.getId() == R.id.widget_ingredient_pref)
                            new RecipePreference().setWidgetPref(v.getContext(), IntentKeys.RECIPE_INGREDIENTS, mAppWidgetId);
                        else
                            new RecipePreference().setWidgetPref(v.getContext(), IntentKeys.RECIPE_STEPS, mAppWidgetId);
                        setResult(v.getContext());
                    }

                }
            });
        }
    }

    private void setResult(Context context) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        RecipeWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);

        // Make sure we pass back the original appWidgetId
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }
}

