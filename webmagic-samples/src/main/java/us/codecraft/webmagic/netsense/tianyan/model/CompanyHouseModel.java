package us.codecraft.webmagic.netsense.tianyan.model;

import us.codecraft.webmagic.netsense.Context;
import us.codecraft.webmagic.netsense.tianyan.dao.CompanyNameDao;
import us.codecraft.webmagic.netsense.tianyan.pojo.Company;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 房地产公司
 */
public class CompanyHouseModel {

    public static final String USER_DIR = System.getProperties().getProperty("user.dir");
    public static final String FILE_PATH = USER_DIR + "/webmagic-samples/src/main/java/us/codecraft/webmagic/netsense/tianyan/res/mastest_company.txt";

    private CompanyNameDao mDao;

    public CompanyHouseModel(){
        mDao = (CompanyNameDao) Context.getInstance().getBean("companyNameDao");
    }


    public List<Company> getListFromFile() {

        List<Company> companyList = new ArrayList<Company>();

        File file = new File(FILE_PATH);
        FileReader reader = null;
        BufferedReader br = null;

        Pattern compile = Pattern.compile("([0-9]+)(.*)");

        try {
            reader = new FileReader(file);
            br = new BufferedReader(reader);

            String line = br.readLine();
            while (line != null) {
                Matcher matcher = compile.matcher(line);
                if(matcher.find()){
                    String level = matcher.group(1);
                    String name = matcher.group(2);
                    Company company = new Company();
                    company.setLevel(Integer.valueOf(level));
                    company.setName(name);
                    company.setSort("房地产");
                    companyList.add(company);
                }
                line = br.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return companyList;
    }

    public List<Company> getList(){
        return mDao.findAll();
    }

}
