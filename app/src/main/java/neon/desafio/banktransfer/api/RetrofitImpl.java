package neon.desafio.banktransfer.api;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import neon.desafio.banktransfer.BuildConfig;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@EBean(scope = EBean.Scope.Singleton)
public class RetrofitImpl {

    @Bean
    OkHttpImpl okHttp;

    private static RetrofitInterface apiInterface ;

    public static void setRetrofitInterface(RetrofitInterface restClientApiMock) {
        apiInterface = restClientApiMock;
    }

    @AfterInject
    void afterInject(){
        if(apiInterface != null)
            return;

        apiInterface = new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttp.getInstance())
                .build().create(RetrofitInterface.class);
    }

    public RetrofitInterface getInstance() {
        return apiInterface;
    }

}
