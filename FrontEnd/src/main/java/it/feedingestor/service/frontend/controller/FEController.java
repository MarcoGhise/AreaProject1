package it.feedingestor.service.frontend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.feedingestor.service.frontend.entity.WordSearch;
import it.feedingestor.service.frontend.news.FetchNews;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import it.library.commondata.DataInformation;

@Controller
public class FEController {

    private final FetchNews fetchNews;

    public FEController(FetchNews fetchNews) {
        this.fetchNews = fetchNews;
    }

    @GetMapping("/search")
    public String searchForm(Model model) {
        model.addAttribute("search", new WordSearch());
        return "search";
    }

    @PostMapping("/search")
    public String searchSubmit(@ModelAttribute WordSearch wordSearch, Model model) {
        String information = fetchNews.getNews(wordSearch);

        model.addAttribute("result", information);
        return "result";
    }

    @GetMapping("/insert")
    public String greetingForm(Model model) {
        model.addAttribute("insert", new DataInformation());
        return "insert";
    }

    @PostMapping("/insert")
    public String greetingSubmit(@ModelAttribute DataInformation information, Model model)  {
        String result = fetchNews.postNews(information);

        model.addAttribute("result", result);
        return "added";
    }

}