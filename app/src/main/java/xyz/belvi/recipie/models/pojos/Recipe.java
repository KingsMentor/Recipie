package xyz.belvi.recipie.models.pojos;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by zone2 on 6/18/17.
 */

public class Recipe implements Parcelable {
    ArrayList<Ingredient> ingredients;
    ArrayList<RecipeStep> steps;
    int id, servings;
    String name, image;

    public Recipe() {
    }

    protected Recipe(Parcel in) {
        ingredients = in.createTypedArrayList(Ingredient.CREATOR);
        steps = in.createTypedArrayList(RecipeStep.CREATOR);
        id = in.readInt();
        servings = in.readInt();
        name = in.readString();
        image = in.readString();
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public ArrayList<Ingredient> getIngredients() {
        return this.ingredients;
    }

    public ArrayList<RecipeStep> getSteps() {
        return this.steps;
    }

    public int getId() {
        return this.id;
    }

    public int getServings() {
        return this.servings;
    }

    public String getName() {
        return this.name;
    }

    public String getImage() {
        return this.image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(ingredients);
        dest.writeTypedList(steps);
        dest.writeInt(id);
        dest.writeInt(servings);
        dest.writeString(name);
        dest.writeString(image);
    }
}
