package it.feedingestor.service.storage.mongo;

import it.library.commondata.MongoInformation;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface InformationRepository extends MongoRepository<MongoInformation, String> {

    List<MongoInformation> findAllBy(TextCriteria criteria);
}
