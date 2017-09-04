package neon.desafio.banktransfer.api;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class CallbackRequest<T> implements Callback<T> {
    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful()) {
            success(response);

            return;
        }
        failureHttp(response);
    }

    @Override
    public void onFailure(Call<T> call, Throwable throwable) {
        failure(throwable);
    }

    protected abstract void success(Response<T> response);

    protected abstract void failureHttp(Response<T> response);

    protected abstract void failure(Throwable throwable);
}
