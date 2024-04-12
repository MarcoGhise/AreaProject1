package it.feedingestor.service.storage.listener;

import it.feedingestor.service.storage.mongo.InformationRepository;
import it.library.commondata.DataInformation;
import it.library.commondata.MongoInformation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
public class NewsListener {

    private final CacheManager cacheManager;

    private final MessageConverter jsonMessageConverter;

    private final InformationRepository repository;

    public NewsListener(CacheManager cacheManager, MessageConverter jsonMessageConverter, InformationRepository repository) {
        this.cacheManager = cacheManager;
        this.jsonMessageConverter = jsonMessageConverter;
        this.repository = repository;
    }

    @RabbitListener(queues = "notify", concurrency = "2")
    public void listenNotification(Message message)
    {
        log.info("Raw Message {}", message);
        /*
         * Received message
         */
        Object receivedObject = jsonMessageConverter.fromMessage(message);

        if (receivedObject instanceof DataInformation infoMessage) {

            log.info("received message {}", infoMessage);
            /*
            Store into Mongo
             */
            MongoInformation mongoInformation = new MongoInformation();

            mongoInformation.setType(infoMessage.getType());
            mongoInformation.setPayload(infoMessage.getPayload());

            repository.save(mongoInformation);
            //Evict cache
            Objects.requireNonNull(cacheManager.getCache("informationCache")).clear();
        }
        else
            log.warn("Skipped message {}", message);

    }
}
