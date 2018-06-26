package ru.shtrm.askmaster.packages;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.Gravity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ru.shtrm.askmaster.R;
import ru.shtrm.askmaster.mvp.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerActions.open;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.contrib.NavigationViewActions.navigateTo;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * The tests for {@link ru.shtrm.askmaster.mvp.questions.QuestionsFragment}.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class PackagesScreenTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule
            = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void navigateToPackagesScreen() {
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT))) // Left drawer should be closed.
                .perform(open()); // Open the drawer.

        // Start packages screen
        onView(withId(R.id.nav_view))
                .perform(navigateTo(R.id.nav_profile));
    }

    @Test
    public void test_PackagesScreenDisplayed() {
        // Check that the bottom navigation view was displayed.
        onView(withId(R.id.bottomNavigationView))
                .check(matches(isDisplayed()));

        // Check that the fab was displayed.
        onView(withId(R.id.fab))
                .check(matches(isDisplayed()));
        onView(withId(R.id.fab))
                .check(matches(isClickable()));
    }

}
