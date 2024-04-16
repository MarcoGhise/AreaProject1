package it.feedingestor.service.scheduler.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)

public class BbcNews {

    @JsonProperty("articles")
    private List<Object> articles;

    @JsonProperty("articles")
    public List<Object> getArticles() {
        return articles;
    }

    @JsonProperty("articles")
    public void setArticles(List<Object> articles) {
        this.articles = articles;
    }
}
