package us.codecraft.webmagic.samples.ttmeiju.pipeline;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.samples.base.Context;
import us.codecraft.webmagic.samples.ttmeiju.pojo.Movie;
import us.codecraft.webmagic.samples.ttmeiju.pojo.Summary;
import us.codecraft.webmagic.samples.ttmeiju.service.IMovieService;
import us.codecraft.webmagic.samples.ttmeiju.service.ISummaryService;

import java.util.List;

public class MoviePipleline implements Pipeline {

    public static final String SUMMARY_LIST = "summary_list";
    public static final String MOVIE_LIST = "movie_list";
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    @Qualifier("summaryService")
    private ISummaryService summaryService;

    @Autowired
    @Qualifier("movieService")
    private IMovieService movieService;

    @Override
    public void process(ResultItems resultItems, Task task) {

        summaryService = (ISummaryService) Context.getInstance().getBean("summaryService");
        movieService = (IMovieService) Context.getInstance().getBean("movieService");

        List<Summary> summaryList = resultItems.get(SUMMARY_LIST);
        List<Movie> movieList = resultItems.get(MOVIE_LIST);

        if(CollectionUtils.isNotEmpty(summaryList)){
            summaryService.save(summaryList);
        }

        if(CollectionUtils.isNotEmpty(movieList)){
            movieService.save(movieList);
        }

    }
}
