package com.eccang.amazon.grammar;

import com.eccang.spider.amazon.pojo.crawl.Review;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jervis
 * @version V0.1
 * @Description:
 * @date 2016/11/2 11:42
 */
public class MapTest extends TestCase {

    @Test
    public void testPut() {
        Map<String,Review> map = new HashMap<>();

        Review review = new Review();
        review.reviewId = "001";

        map.put("001",review);

        Review review2 = new Review();
        review.reviewId = "002";
        map.put("001",review2);

        System.out.println(map.size());

    }

}