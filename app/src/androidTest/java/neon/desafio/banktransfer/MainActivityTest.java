package neon.desafio.banktransfer;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.gson.GsonBuilder;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.InputStream;

import neon.desafio.banktransfer.activity.HistoryActivity_;
import neon.desafio.banktransfer.activity.MainActivity_;
import neon.desafio.banktransfer.activity.SendMoneyActivity;
import neon.desafio.banktransfer.activity.SendMoneyActivity_;
import neon.desafio.banktransfer.api.RetrofitImpl;
import neon.desafio.banktransfer.api.RetrofitInterface;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.SocketPolicy;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity_> mActivityRule = new ActivityTestRule<>(MainActivity_.class, false, false);

    private MockWebServer server;
    private Context context;

    @Before
    public void before() throws Exception {
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        server = new MockWebServer();
        server.start();
        setupServerUrl();
    }

    @Test
    public void afterScreenLoad_FieldsAreVisible() throws InterruptedException {
        enqueueTokenRequestSuccess();
        Thread.sleep(250);

        onView(withId(R.id.main_profile_name)).check(matches(isDisplayed()));
        onView(withId(R.id.main_profile_mail)).check(matches(isDisplayed()));
        onView(withText("Pedro Pereira")).check(matches(isDisplayed()));
    }

    @Test
    public void afterScreenLoad_RequestErrorShowsMessage() throws InterruptedException {
        enqueueTokenRequestError();
        Thread.sleep(250);

        onView(withText(R.string.generic_token_error)).check(matches(isDisplayed()));
    }

    @Test
    public void afterScreenLoad_RequestNetworkErrorShowsMessage() throws InterruptedException {
        enqueueTokenRequestNetworkError();
        Thread.sleep(1000);

        onView(withText(R.string.connection_error)).check(matches(isDisplayed()));
    }

    @Test
    public void clickOnSendMoneyOpensScreen() throws InterruptedException {
        enqueueTokenRequestSuccess();
        Thread.sleep(250);

        Intents.init();

        Matcher<Intent> matcher = hasComponent(SendMoneyActivity_.class.getName());

        onView(withId(R.id.main_send_money_button)).perform(click());

        intended(matcher);

        Intents.release();
    }

    @Test
    public void clickOnHistoryOpensScreen() throws InterruptedException {
        enqueueTokenRequestSuccess();
        Thread.sleep(250);

        Intents.init();

        Matcher<Intent> matcher = hasComponent(HistoryActivity_.class.getName());

        onView(withId(R.id.main_history_button)).perform(click());

        intended(matcher);

        Intents.release();
    }

    private void enqueueTokenRequestSuccess() {
        server.enqueue(new MockResponse().setResponseCode(200).setBody("8267f821-212e-4222-a683-d1248198070f"));
        mActivityRule.launchActivity(new Intent());
    }

    private void enqueueTokenRequestError() {
        server.enqueue(new MockResponse().setResponseCode(400));
        mActivityRule.launchActivity(new Intent());
    }

    private void enqueueTokenRequestNetworkError() {
        server.enqueue(new MockResponse().setSocketPolicy(SocketPolicy.DISCONNECT_AT_END));
        mActivityRule.launchActivity(new Intent());
    }

    private void setupServerUrl() {
        String url = server.url("/").toString();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        final RetrofitInterface api = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                        .create()))
                .client(client)
                .build()
                .create(RetrofitInterface.class);

        RetrofitImpl.setRetrofitInterface(api);
    }
}
