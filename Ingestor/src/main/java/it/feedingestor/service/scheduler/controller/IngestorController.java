package it.feedingestor.service.scheduler.controller;

import it.feedingestor.service.scheduler.message.Publisher;
import it.library.commondata.DataInformation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IngestorController {

    private final Publisher publisher;

    public IngestorController(Publisher publisher) {
        this.publisher = publisher;
    }

    @PostMapping("/publish")
    public String publishMessage(@RequestBody DataInformation information){
        publisher.publishMessage(information);

        return "ok";
    }
}
