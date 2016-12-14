package us.codecraft.webmagic.amazon.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.base.SpringTestCase;
import us.codecraft.webmagic.samples.amazon.pojo.RelationCustomerAsin;
import us.codecraft.webmagic.samples.amazon.service.RelationCustomerAsinService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hardy
 * @version V0.1
 * @Description:
 * @date 2016/12/5 14:37
 */
public class RelationCustomerAsinServiceTest extends SpringTestCase {
    @Autowired
    RelationCustomerAsinService mRelationCustomerAsinService;

    /**
     * 添加一条关系数据
     */
    @Test
    public void add() {
        RelationCustomerAsin relationCustomerAsin = new RelationCustomerAsin();
        relationCustomerAsin.setCustomerCode("EC_001");
        relationCustomerAsin.setSiteCode("US");
        relationCustomerAsin.setAsin("B01M6WRC9R");
        relationCustomerAsin.setRootAsin("B01LY3AMY8");
        relationCustomerAsin.setStatus(1);
        mRelationCustomerAsinService.add(relationCustomerAsin);
        System.out.println(relationCustomerAsin);
    }

    /**
     * 批量添加
     */
    @Test
    public void addAll() {
        List<RelationCustomerAsin> relationCustomerAsinList = new ArrayList<RelationCustomerAsin>();
        RelationCustomerAsin relation;
        for (int i = 0; i < 2; i++) {
            relation = new RelationCustomerAsin();
            relation.setCustomerCode("EC_001" + i);
            relation.setSiteCode("US");
            relation.setAsin("B01M6WRC9R");
            relation.setRootAsin("B01LY3AMY8");
            relation.setStatus(1);
            relationCustomerAsinList.add(relation);
        }
        mRelationCustomerAsinService.addAll(relationCustomerAsinList);
        System.out.println(relationCustomerAsinList);
    }

    /**
     * 通过id删除
     */
    @Test
    public void deleteById() {
        mRelationCustomerAsinService.deleteById(8);
    }

    /**
     * 通过id更新
     */
    @Test
    public void updateById() {
        RelationCustomerAsin relationCustomerAsin = new RelationCustomerAsin();
        relationCustomerAsin.setId(7);
        relationCustomerAsin.setCustomerCode("EC_007");
        relationCustomerAsin.setSiteCode("CN");
        relationCustomerAsin.setAsin("B01M6WRC9R");
        relationCustomerAsin.setRootAsin("B01LY3AMY8");
        relationCustomerAsin.setStatus(0);
        mRelationCustomerAsinService.updateById(relationCustomerAsin);
    }

    /**
     * 查询对应客户下的asin
     */
    @Test
    public void findByCustomer() {
        String customerCode = "EC_007";
        List<RelationCustomerAsin> relationCustomerAsinList = mRelationCustomerAsinService.findByCustomer(customerCode);
        System.out.println(relationCustomerAsinList);
    }

    /**
     * 查询对应客户和asin是否存在
     */
    @Test
    public void findByCustomerAndAsin() {
        String customerCode = "EC_001";
        String asin = "B01M6WRC9R";
        RelationCustomerAsin relationCustomerAsin = mRelationCustomerAsinService.findByCustomerAndAsin(customerCode, asin);
        System.out.println(relationCustomerAsin);
    }

    @Test
    public void isExisted() {
        String customerCode = "EC_001";
        String asin = "B01M6WRC9R";
        System.out.println(mRelationCustomerAsinService.isExisted(customerCode, asin));
    }
}