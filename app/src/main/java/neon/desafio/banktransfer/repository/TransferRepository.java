package neon.desafio.banktransfer.repository;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.greenrobot.eventbus.EventBus;

import java.util.List;

import neon.desafio.banktransfer.api.CallbackRequest;
import neon.desafio.banktransfer.api.RequestError;
import neon.desafio.banktransfer.api.RetrofitImpl;
import neon.desafio.banktransfer.controller.SessionController;
import neon.desafio.banktransfer.event.TransferHistorySuccess;
import neon.desafio.banktransfer.event.TransferMoneySuccessEvent;
import neon.desafio.banktransfer.model.SendMoneyRequestVO;
import neon.desafio.banktransfer.model.TransferVO;
import retrofit2.Response;

@EBean
public class TransferRepository {

    @Bean
    RetrofitImpl webService;
    @Bean
    SessionController sessionController;
    @Bean
    RequestError requestError;

    public void sentMoney(String clientId, double value){
        webService.getInstance().sendMoney(new SendMoneyRequestVO(clientId, sessionController.getSessionToken(), value))
                .enqueue(new CallbackRequest<Boolean>() {
                    @Override
                    protected void success(Response<Boolean> response) {
                        EventBus.getDefault().post(new TransferMoneySuccessEvent(response.body().booleanValue()));
                    }

                    @Override
                    protected void failureHttp(Response<Boolean> response) {
                        requestError.onLoadHttpFailureEvent(response);
                    }

                    @Override
                    protected void failure(Throwable throwable) {
                        requestError.onLoadInputOutputFailureEvent(throwable);
                    }
                });
    }

    public void getTransfersHistory() {
        webService.getInstance().getTransfers(sessionController.getSessionToken())
                .enqueue(new CallbackRequest<List<TransferVO>>() {
            @Override
            protected void success(Response<List<TransferVO>> response) {
                EventBus.getDefault().post(new TransferHistorySuccess(response.body()));
            }

            @Override
            protected void failureHttp(Response<List<TransferVO>> response) {
                requestError.onLoadHttpFailureEvent(response);
            }

            @Override
            protected void failure(Throwable throwable) {
                requestError.onLoadInputOutputFailureEvent(throwable);
            }
        });
    }

}
