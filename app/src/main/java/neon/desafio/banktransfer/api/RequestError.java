package neon.desafio.banktransfer.api;

import org.androidannotations.annotations.EBean;
import org.greenrobot.eventbus.EventBus;

import neon.desafio.banktransfer.event.ErrorEvent;
import retrofit2.Response;

@EBean(scope = EBean.Scope.Singleton)
public class RequestError {

    public void onLoadHttpFailureEvent(Response response) {
        int statusCode = response.code();

        switch (statusCode) {
            case Kind.HTTP_400:
                postError(new ErrorEvent(ErrorEvent.Type.BAD_REQUEST));
                break;
            case Kind.HTTP_401:
                postError(new ErrorEvent(ErrorEvent.Type.UNAUTHORIZED));
                break;
            case Kind.HTTP_404:
                postError(new ErrorEvent(ErrorEvent.Type.NOT_FOUND));
                break;
            case Kind.HTTP_500:
                postError(new ErrorEvent(ErrorEvent.Type.SERVER_ERROR));
                break;
            default:
                postError(new ErrorEvent(ErrorEvent.Type.GENERIC_ERROR));
                break;
        }
    }

    public void onLoadInputOutputFailureEvent(Throwable throwable) {
        postError(new ErrorEvent(ErrorEvent.Type.NETWORK_ERROR));
    }

    public void postError(ErrorEvent error) {
        EventBus.getDefault().post(error);
    }

    interface Kind {
        int HTTP_400 = 400;

        int HTTP_401 = 401;

        int HTTP_404 = 404;

        int HTTP_500 = 500;
    }
}
