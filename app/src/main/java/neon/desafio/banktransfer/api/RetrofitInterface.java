package neon.desafio.banktransfer.api;

import java.util.List;

import neon.desafio.banktransfer.model.SendMoneyRequestVO;
import neon.desafio.banktransfer.model.TransferVO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitInterface {
    @GET("/GenerateToken")
    Call<String> generateToken(@Query("nome") String userName,
                               @Query("email") String userMail);

    @POST("/SendMoney")
    Call<Boolean> sendMoney(@Body SendMoneyRequestVO requestVO);

    @GET("/GetTransfers")
    Call<List<TransferVO>> getTransfers (@Query("token") String token);
}
