package ar.edu.utn.dds.k3003.client;

import ar.edu.utn.dds.k3003.facades.FachadaFuente;
import ar.edu.utn.dds.k3003.facades.FachadaSolicitudes;
import ar.edu.utn.dds.k3003.facades.dtos.EstadoSolicitudBorradoEnum;
import ar.edu.utn.dds.k3003.facades.dtos.PdIDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.HttpStatus;
import java.util.*;
import lombok.SneakyThrows;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;

public class ProcesadorPdiProxy {

  private final String endpoint;
  private final ProcesadorPdiRetrofitClient service;

  public ProcesadorPdiProxy(ObjectMapper objectMapper) {

    var env = System.getenv();
    this.endpoint = env.getOrDefault("URL_PROCESADOR", "https://tp-dds-2025-procesadorpdi-grupo2-1.onrender.com/api/");

    objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

    var retrofit =
        new Retrofit.Builder()
            .baseUrl(this.endpoint)
            .addConverterFactory(JacksonConverterFactory.create(objectMapper))
            .build();

    this.service = retrofit.create(ProcesadorPdiRetrofitClient.class);
  }

  public ProcesadorPdiProxy(String endpoint, ProcesadorPdiRetrofitClient service) {
    this.endpoint = endpoint;
    this.service = service;
  }

  public PdIDTO procesar(PdIDTO pdIDTO) throws java.io.IOException {
    var res = service.procesar(pdIDTO).execute();
    if (!res.isSuccessful()) {
        throw new RuntimeException("Error conectandose con procesadorPdi (" + new Integer(res.code()).toString() + ")");
    }

    return res.body();
  }
}