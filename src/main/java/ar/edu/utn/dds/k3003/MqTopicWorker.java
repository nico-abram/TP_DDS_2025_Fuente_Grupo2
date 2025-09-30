package ar.edu.utn.dds.k3003;

import ar.edu.utn.dds.k3003.app.Fachada;
import ar.edu.utn.dds.k3003.config.JacksonConfig;
import ar.edu.utn.dds.k3003.facades.dtos.HechoDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MqTopicWorker extends DefaultConsumer {

    private String exchangeName;
    private EntityManagerFactory entityManagerFactory;
    private final Fachada fachada;
    private ObjectMapper objectMapper;

    protected MqTopicWorker(Channel channel, String exchangeName, Fachada fachada) {
        super(channel);
        this.exchangeName = exchangeName;
        this.entityManagerFactory = entityManagerFactory;
        this.fachada = fachada;

        var mapperBuilder = new Jackson2ObjectMapperBuilder();
        (new JacksonConfig()).jsonCustomizer().customize(mapperBuilder);
        this.objectMapper = mapperBuilder.build();
    }

    public void init() throws IOException {
        try {
            var channel = this.getChannel();
            channel.exchangeDeclare(exchangeName,  BuiltinExchangeType.FANOUT, true, false, false, Map.of(
                    "key", "value"
            ));
            String queueName = channel.queueDeclare().getQueue();
            channel.queueBind(queueName, exchangeName, "");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                try {
                    String message = new String(delivery.getBody(), "UTF-8");
                    System.out.println(" Recibido mensaje de subscripcion '" + message + "'");
                    HechoDTO body = objectMapper.readValue(message, HechoDTO.class);

                    HechoDTO creado = fachada.agregar(body);
                    System.out.println(" Creado hecho desde subscripcion: '" + objectMapper.writeValueAsString(creado) + "'");
                } catch (Exception e) {
                    System.out.println(" Error procesando hecho desde subscripcion: '" + e.getMessage() + "'");
                }
            };
            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
            });
        } catch (Exception e) {
            System.out.println(" Error suscribiendose a exchange: '" + e.getMessage() + "'");
        }

    }

}
