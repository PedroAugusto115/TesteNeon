package neon.desafio.banktransfer.repository;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import neon.desafio.banktransfer.api.CallbackRequest;
import neon.desafio.banktransfer.api.RequestError;
import neon.desafio.banktransfer.api.RetrofitImpl;
import neon.desafio.banktransfer.controller.SessionController;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@EBean
public class TokenRepository {

    @Bean
    RetrofitImpl webService;
    @Bean
    SessionController sessionController;
    @Bean
    RequestError requestError;

    public void generateToken(String userName, String userEmail) {

        webService.getInstance().generateToken(userName, userEmail).enqueue(new CallbackRequest<String>() {
            @Override
            protected void success(Response<String> response) {
                sessionController.setSessionToken(response.body());
            }

            @Override
            protected void failureHttp(Response<String> response) {
                requestError.onLoadHttpFailureEvent(response);
            }

            @Override
            protected void failure(Throwable throwable) {
                requestError.onLoadInputOutputFailureEvent(throwable);
            }
        });
    }
}
