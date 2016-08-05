package us.codecraft.webmagic.netsense.model;

import junit.framework.TestCase;
import us.codecraft.webmagic.netsense.Context;
import us.codecraft.webmagic.netsense.tianyan.dao.CompanyDao;
import us.codecraft.webmagic.netsense.tianyan.model.CompanyModel;
import us.codecraft.webmagic.netsense.tianyan.pojo.CompanyCvs;

import java.util.ArrayList;
import java.util.List;

/**
 * 公司
 */
public class CompanyModelTest extends TestCase {

    private CompanyDao companyDao;

    @Override
    protected void setUp() throws Exception {
        companyDao = (CompanyDao) Context.getInstance().getBean("companyDao");
    }

    public void testGetList() {

        CompanyModel model = new CompanyModel();
        List<String> nameList = model.getNameList();
        System.out.println(nameList.size());

        List<CompanyCvs> companyCvsList = model.getList();

        List<String> srcList = new ArrayList<String>();
        for (CompanyCvs companyCvs : companyCvsList) {
            srcList.add(companyCvs.getName());
        }
        System.out.println(srcList.size());

        srcList.removeAll(nameList);
        System.out.println(srcList.size());

    }

    public void testExist(){
        System.out.println(companyDao.isExist("http://www.tianyancha.com/company/1070074"));
    }
}
