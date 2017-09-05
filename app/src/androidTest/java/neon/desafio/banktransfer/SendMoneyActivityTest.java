package neon.desafio.banktransfer;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.text.Editable;
import android.text.Selection;
import android.widget.EditText;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import neon.desafio.banktransfer.activity.SendMoneyActivity_;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class SendMoneyActivityTest {

    @Rule
    public ActivityTestRule<SendMoneyActivity_> mActivityRule = new ActivityTestRule<>(SendMoneyActivity_.class, false, true);

    @Test
    public void afterScreenLoad_CheckRecyclerIsVisible() throws InterruptedException {
        Thread.sleep(250);
        onView(withId(R.id.send_money_recycler_view)).check(matches(isDisplayed()));
    }

    @Test
    public void clickOnLineOpensAlert() throws InterruptedException {
        Thread.sleep(250);

        onView(withId(R.id.send_money_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(withText(R.string.alert_label)).check(matches(isDisplayed()));
    }
}
