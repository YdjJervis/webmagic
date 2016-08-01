package us.codecraft.webmagic.frame;

import junit.framework.TestCase;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.sql.DataSource;

/**
 * Hibernate Spring集成框架测试
 */
public class HibernateSpringTest extends TestCase {

    private ApplicationContext mContext;

    @Override
    public void setUp() throws Exception {
        mContext = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
    }

    public void testDataSource() throws Exception {
        DataSource dataSource = (DataSource) mContext.getBean("dataSource");
        System.out.println(dataSource.getConnection());

    }
}
