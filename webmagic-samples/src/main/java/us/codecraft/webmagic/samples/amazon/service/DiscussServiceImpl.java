package us.codecraft.webmagic.samples.amazon.service;

import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.amazon.pojo.Discuss;

import java.util.List;

@Service
public class DiscussServiceImpl implements DiscussService {

//    @Autowired
//    private DiscussDao discussDao;

    @Override
    public List<Discuss> findAll() {
//        return discussDao.findAll();
        return null;
    }
}
