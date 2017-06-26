package xyz.belvi.recipie.views.holders;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import xyz.belvi.recipie.R;

/**
 * Created by zone2 on 6/18/17.
 */

public class RecipeStepsHolder extends RecyclerView.ViewHolder {
    private AppCompatTextView recipeStepInfo;
    private LinearLayoutCompat infoBg;
    private Context context;

    public RecipeStepsHolder(View itemView) {
        super(itemView);
        context = itemView.getContext();
        recipeStepInfo = (AppCompatTextView) itemView.findViewById(R.id.step_info);
        infoBg = (LinearLayoutCompat) itemView.findViewById(R.id.info_bg);
    }

    public AppCompatTextView getRecipeStepInfo() {
        return this.recipeStepInfo;
    }

    public LinearLayoutCompat getInfoBg() {
        return this.infoBg;
    }

    public Context getContext() {
        return this.context;
    }
}
