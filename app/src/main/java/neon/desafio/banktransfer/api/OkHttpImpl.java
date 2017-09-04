package neon.desafio.banktransfer.api;

import android.content.Context;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.io.File;
import java.util.concurrent.TimeUnit;

import neon.desafio.banktransfer.BuildConfig;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

@EBean(scope = EBean.Scope.Singleton)
public class OkHttpImpl {

    private static final long TIME_OUT = 60;
    private static final int CACHE_SIZE = 10 * 1024 * 1024;
    private static final String CACHE_NAME = "API_HTTP";
    private static OkHttpClient okHttpClient = null;

    @RootContext
    Context context;

    @AfterInject
    void initObject() {

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .cache(new Cache(new File(context.getCacheDir(), CACHE_NAME), CACHE_SIZE))
                .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
                .addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(BuildConfig.DEBUG
                        ? HttpLoggingInterceptor.Level.BODY
                        : HttpLoggingInterceptor.Level.NONE));

        okHttpClient = builder.build();
    }

    public OkHttpClient getInstance() {
        return okHttpClient;
    }

}
