package it.feedingestor.service.scheduler.job;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.feedingestor.service.scheduler.entity.BbcNews;
import it.feedingestor.service.scheduler.message.Publisher;
import it.feedingestor.service.scheduler.service.NewsFetcher;
import it.library.commondata.DataInformation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ImportNews {

    final Publisher publisher;

    final NewsFetcher newsFetcher;

    public ImportNews(Publisher publisher, NewsFetcher newsFetcher) {
        this.publisher = publisher;
        this.newsFetcher = newsFetcher;
    }

    //@Scheduled(cron = "${scheduler.cron}")
    @Scheduled(fixedRate = 10000000)
    public void importFromBbc() throws Exception {
        /*
        Read data from Bbc website
         */
        BbcNews bbcNews =  newsFetcher.getNewsFromBbcHeadline();
        /*
        Send message for every
         */
        bbcNews.getArticles().forEach(info -> publisher.publishMessage(this.mapFromBbcNews(info)));

    }

    private DataInformation mapFromBbcNews(Object o){
        DataInformation dataInformation = new DataInformation();
        dataInformation.setType("BbcNews");
        dataInformation.setPayload(o);
        return dataInformation;
    }
}
