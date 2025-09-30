package ar.edu.utn.dds.k3003;

import ar.edu.utn.dds.k3003.app.Fachada;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "ar.edu.utn.dds.k3003.repository")
@EntityScan(basePackages = "ar.edu.utn.dds.k3003.model")
public class Application {
    public static void main(String[] args) throws IOException, TimeoutException {

        var app = SpringApplication
                .run(Application.class, args);

        Map<String, String> env = System.getenv();
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(env.get("MESSAGEBROKER_HOST"));
        factory.setUsername(env.get("MESSAGEBROKER_USERNAME"));
        factory.setPassword(env.get("MESSAGEBROKER_PASSWORD"));
        // En el plan más barato, el VHOST == USER
        factory.setVirtualHost(env.get("MESSAGEBROKER_USERNAME"));
        String exchangeName = env.get("MESSAGEBROKER_EXCHANGE_NAME");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        MqTopicWorker worker = new MqTopicWorker(channel, exchangeName, app.getBean(Fachada.class));
        worker.init();

    }
}
