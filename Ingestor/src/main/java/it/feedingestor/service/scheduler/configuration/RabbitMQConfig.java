package it.feedingestor.service.scheduler.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    /*
            * Exchange configurations
	 */
    @Bean
    DirectExchange directExchange() {
        return new DirectExchange("notify-exchange");
    }
    /*
     * Direct exchange binding
     */
    @Bean
    Binding notifyBinding(Queue notifyQueue, DirectExchange exchange) {
        return BindingBuilder.bind(notifyQueue).to(exchange).withQueueName();
    }

    @Bean
    Queue notifyQueue() {
        return new Queue("notify", false);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}
