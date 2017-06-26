package xyz.belvi.recipie.presenters.network.requests;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import xyz.belvi.recipie.models.cache.RecipePreference;
import xyz.belvi.recipie.models.pojos.Recipe;
import xyz.belvi.recipie.presenters.app.RecipeApplication;

/**
 * Created by zone2 on 6/19/17.
 */

public class RecipeRequest extends Request<ArrayList<Recipe>> {

    private final Response.Listener<ArrayList<Recipe>> listener;
    public static final String URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";


    public RecipeRequest(String url, Response.Listener<ArrayList<Recipe>> listener, Response.ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        this.listener = listener;
    }


    @Override
    protected Response<ArrayList<Recipe>> parseNetworkResponse(NetworkResponse response) {
        try {

            String data = new String(
                    response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            new RecipePreference().cacheRecipe(RecipeApplication.getInstance().getApplicationContext(), data);

            try {
                return Response.success(generateRecipeFromJson(data),
                        HttpHeaderParser.parseCacheHeaders(response));
            } catch (JSONException e) {
                return Response.error(new ParseError(e));
            }


        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(ArrayList<Recipe> response) {
        listener.onResponse(response);
    }

    public static ArrayList<Recipe> generateRecipeFromJson(String data) throws JSONException {
        Gson gson = new Gson();
        ArrayList<Recipe> recipes = new ArrayList<>();
        JSONArray dataArray = new JSONArray(data);
        for (int responseIndex = 0; responseIndex < dataArray.length(); responseIndex++) {
            final Recipe recipe;

            recipe = gson.fromJson(dataArray.getString(responseIndex), Recipe.class);
            recipes.add(recipe);

        }
        return recipes;
    }
}
