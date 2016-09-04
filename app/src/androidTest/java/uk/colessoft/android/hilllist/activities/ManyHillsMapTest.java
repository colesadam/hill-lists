package uk.colessoft.android.hilllist.activities;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.FlakyTest;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import uk.colessoft.android.hilllist.R;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ManyHillsMapTest extends HillTest {


    @Rule
    public ActivityTestRule<Main> mActivityTestRule = new ActivityTestRule<Main>(Main.class){
        @Override
        protected void beforeActivityLaunched() {
            grantExternalStoragePermission();
            super.beforeActivityLaunched();

        }

    };

    @Test
    @FlakyTest(tolerance=3)
    public void manyHillsMapTest() {
        ViewInteraction linearLayout = onView(
                allOf(withId(R.id.menu_scotland),
                        withParent(withId(R.id.menu_1)),
                        isDisplayed()));
        linearLayout.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.munros), withText("Munros"), isDisplayed()));
        textView.perform(click());

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        ViewInteraction textView2 = onView(
                allOf(withId(android.R.id.title), withText("Show hills on Map"), isDisplayed()));
        textView2.perform(click());

        ViewInteraction frameLayout = onView(
                allOf(withId(R.id.hills_map_fragment),
                        childAtPosition(
                                allOf(withId(R.id.many_map_rel),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                0),
                        isDisplayed()));
        frameLayout.check(matches(isDisplayed()));

        ViewInteraction toggleButton = onView(
                allOf(withId(R.id.satellite_button),
                        childAtPosition(
                                allOf(withId(R.id.many_map_rel),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                1),
                        isDisplayed()));
        toggleButton.check(matches(isDisplayed()));

    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
