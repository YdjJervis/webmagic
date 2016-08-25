package us.codecraft.webmagic.netsense.tianyan.model;

import org.apache.commons.lang.StringUtils;
import org.jumpmind.symmetric.csv.CsvReader;
import us.codecraft.webmagic.netsense.Context;
import us.codecraft.webmagic.netsense.base.model.BaseModel;
import us.codecraft.webmagic.netsense.tianyan.dao.CompanyDao;
import us.codecraft.webmagic.netsense.tianyan.pojo.CompanyCvs;
import us.codecraft.webmagic.netsense.tianyan.pojo.CompanyInfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 公司模型
 */
public class CompanyModel implements BaseModel<CompanyCvs> {

    private CompanyDao mDao;

    public CompanyModel() {
        mDao = (CompanyDao) Context.getInstance().getBean("companyDao");
    }

    @Override
    public List<CompanyCvs> getList() {

        List<CompanyCvs> companyList = new ArrayList<CompanyCvs>();

        String lineStr;

        String projectPath = new File("").getAbsolutePath();
        System.out.println("project path = " + projectPath);

        File cvs = new File(projectPath + "/webmagic-samples/src/main/resources/tianyancha/company_sz.csv");

        try {
            BufferedReader reader = new BufferedReader(new FileReader(cvs));
            CsvReader creader = new CsvReader(reader, ',');

            while (creader.readRecord()) {
                lineStr = creader.getRawRecord();//读取一行数据

                CompanyCvs company = new CompanyCvs();

                String[] pros = lineStr.split(",");

                company.setName(pros[0]);
                company.setOrgCode(pros[1]);
                company.setCertificate(pros[2]);
                company.setIssueUnit(pros[3]);

                companyList.add(company);

            }

            System.out.println(companyList.toString());
            creader.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return companyList;
    }

    public List<String> getNameList() {
        List<String> nameList = new ArrayList<String>();

        for (CompanyInfo info : mDao.findAll()) {
            nameList.add(info.getName().trim());
        }

        return nameList;
    }

    public boolean isExsit(String name) {
        for (CompanyInfo companyInfo : mDao.findAll()) {
            if (StringUtils.isNotEmpty(companyInfo.getName()) && companyInfo.getName().contains(name)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        new CompanyModel().getList();
    }
}
