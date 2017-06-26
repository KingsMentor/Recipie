package xyz.belvi.recipie;

import android.content.Intent;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.intent.matcher.IntentMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import xyz.belvi.recipie.presenters.interfaces.IntentKeys;
import xyz.belvi.recipie.views.activities.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.allOf;

/**
 * Created by zone2 on 6/23/17.
 */

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule
            = new ActivityTestRule<MainActivity>(MainActivity.class);

    private IdlingResource mIdlingResource;

    @Before
    public void registerIdlingresource() {

        mIdlingResource = mainActivityActivityTestRule.getActivity().getIdlingResource();
        Espresso.registerIdlingResources(mIdlingResource);
    }


    @Test
    public void performTest() {
        if (mainActivityActivityTestRule.getActivity().isSuccessful()) {
            onView(withId(R.id.recipe_list)).check(matches(isDisplayed()));
            Intents.init();
            onView(withId(R.id.recipe_list))
                    .perform(actionOnItemAtPosition(0, click()));

            Matcher<Intent> expectedIntent = allOf(
                    IntentMatchers.hasExtraWithKey(IntentKeys.RecipeIntentKey));
            intended(expectedIntent);
            Intents.release();
            onView(withId(R.id.ingredients_details)).check(matches(isDisplayed()));
            pressBack();
        } else {
            onView(withId(R.id.empty_view)).check(matches(isDisplayed()));
            onView(withId(R.id.empty_view)).perform(click());
        }

    }


    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            Espresso.unregisterIdlingResources(mIdlingResource);
        }
    }
}
