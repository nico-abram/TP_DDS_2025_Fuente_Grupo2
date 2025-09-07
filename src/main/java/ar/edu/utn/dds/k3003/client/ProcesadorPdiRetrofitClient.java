package ar.edu.utn.dds.k3003.client;

import ar.edu.utn.dds.k3003.facades.dtos.PdIDTO;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Body;

public interface ProcesadorPdiRetrofitClient {

  @POST("pdis")
  Call<PdIDTO> procesar(@Body PdIDTO pdi);
}