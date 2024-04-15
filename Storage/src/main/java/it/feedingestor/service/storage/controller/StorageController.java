package it.feedingestor.service.storage.controller;

import it.feedingestor.service.storage.mongo.InformationRepository;
import it.library.commondata.DataInformation;
import it.library.commondata.MongoInformation;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
public class StorageController {

    private final InformationRepository repository;

    public StorageController(InformationRepository repository, MongoOperations mongoOps) {
        this.repository = repository;
    }

    @GetMapping("/information/{word}")
    @Cacheable(cacheNames="informationCache", key="#word")
    public List<MongoInformation> getInformation(@PathVariable String word)
    {
        TextCriteria criteria = TextCriteria.forDefaultLanguage().matchingAny(word);
        return repository.findAllBy(criteria);
    }
}
