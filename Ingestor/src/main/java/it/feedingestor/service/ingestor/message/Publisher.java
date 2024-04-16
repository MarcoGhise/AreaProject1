package it.feedingestor.service.ingestor.message;

import it.library.commondata.DataInformation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class Publisher {
    // RabbitMQ Client
    final RabbitTemplate rabbitTemplate;

    public Publisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public String publishMessage(DataInformation information) {
        /*
         * Send message
         */
        rabbitTemplate.convertAndSend("notify-exchange", "notify", information);

        log.info("Sent message {}", information);
        return "OK";
    }

}
