package uk.colessoft.android.hilllist.activities;


import androidx.test.InstrumentationRegistry;
import androidx.test.espresso.ViewInteraction;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;

import uk.colessoft.android.hilllist.R;
import uk.colessoft.android.hilllist.TestComponentRule;

import static androidx.test.InstrumentationRegistry.getTargetContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MunrosListTest extends HillTest{


    public ActivityTestRule<Main> mActivityTestRule = new ActivityTestRule<Main>(Main.class){
        @Override
        protected void beforeActivityLaunched() {
            grantExternalStoragePermission();
            //getTargetContext().deleteDatabase(HillsDatabaseHelper.DATABASE_NAME);
            super.beforeActivityLaunched();

        }

    };

    public final TestComponentRule component =
            new TestComponentRule(InstrumentationRegistry.getTargetContext());


    @Rule
    public TestRule chain = RuleChain.outerRule(component).around(mActivityTestRule);

    @Test
    public void munrosListTest() {
        ViewInteraction cardView = onView(
                allOf(withId(R.id.menu_scotland), isDisplayed()));
        cardView.perform(click());

        ViewInteraction cardView2 = onView(
                allOf(withId(R.id.munros), isDisplayed()));
        cardView2.perform(click());

        ViewInteraction linearLayout = onView(
                allOf(withId(R.id.ln_h),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.myListView),
                                        0),
                                0),
                        isDisplayed()));
        linearLayout.check(matches(isDisplayed()));

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
