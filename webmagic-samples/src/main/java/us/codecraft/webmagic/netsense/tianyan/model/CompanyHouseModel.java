package us.codecraft.webmagic.netsense.tianyan.model;

import org.apache.commons.lang.StringUtils;
import us.codecraft.webmagic.netsense.Context;
import us.codecraft.webmagic.netsense.tianyan.dao.BuildingDao;
import us.codecraft.webmagic.netsense.tianyan.dao.CompanyNameDao;
import us.codecraft.webmagic.netsense.tianyan.dao.LandInfoDao;
import us.codecraft.webmagic.netsense.tianyan.pojo.Building;
import us.codecraft.webmagic.netsense.tianyan.pojo.Company;
import us.codecraft.webmagic.netsense.tianyan.pojo.LandInfo;

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
    public static final String FILE_PATH = USER_DIR + "/webmagic-samples/src/main/java/us/codecraft/webmagic/netsense/tianyan/res/2016房地产500强名单.txt";

    private static CompanyNameDao mDao;
    private LandInfoDao mLandInfoDao;
    private BuildingDao mBuildingDao;

    public CompanyHouseModel() {
        mDao = (CompanyNameDao) Context.getInstance().getBean("companyNameDao");
        mLandInfoDao = (LandInfoDao) Context.getInstance().getBean("landInfoDao");
        mBuildingDao = (BuildingDao) Context.getInstance().getBean("buildingDao");
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
                if (matcher.find()) {
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

    public List<Company> getList() {
        return mDao.findAll();
    }

    public List<LandInfo> getLandInfoList() {
        return mLandInfoDao.findAll();
    }

    public List<Building> getBuildingList() {
        return mBuildingDao.findAll();
    }

    public static void main(String[] args) {
        //把新房表里面的公司信息提取出来同步到公司表集合
        /*List<Building> buildingList = new CompanyHouseModel().getBuildingList();

//        List<Company> companyList = new ArrayList<Company>();
        for (Building building : buildingList) {
            sleep(50);

            //特殊字符串有：空，暂无资料，
            System.out.println(building);
            List<String> strs = new ArrayList<String>();

            String tmp = building.getDeveloper();
            if (StringUtils.isNotEmpty(tmp) && !tmp.contains("暂无")) {
                strs.add(tmp);
            }

            tmp = building.getMgrcompany();
            if (StringUtils.isNotEmpty(tmp) && !tmp.contains("暂无")) {
                strs.add(tmp);
            }

            for (String str : strs) {
                Company company = new Company();
                company.setSort("Building");
                company.setName(str);
//                companyList.add(company);
                try {
                    mDao.add(company);
                } catch (Exception e) {
                    System.out.println(e.toString());
                }
            }
        }*/
//        mDao.add(companyList);

        //把土地信息表里面的公司信息提取出来同步到公司表集合
        List<LandInfo> landInfoList = new CompanyHouseModel().getLandInfoList();

//        companyList = new ArrayList<Company>();
        for (LandInfo building : landInfoList) {

            sleep(50);
            //特殊字符串有：空，暂无资料，
            System.out.println(building);
            List<String> strs = new ArrayList<String>();

            String tmp = building.getBuyuser();
            if (StringUtils.isNotEmpty(tmp) && !tmp.contains("暂无") && !tmp.contains("***")) {
                strs.add(tmp);
            }

            tmp = building.getOwner();
            if (StringUtils.isNotEmpty(tmp) && tmp.length() > 3) {
                strs.add(tmp);
            }

            tmp = building.getTRANSFEROR();
            if (StringUtils.isNotEmpty(tmp) && !tmp.contains("暂无")) {
                strs.add(tmp);
            }

            for (String str : strs) {
                Company company = new Company();
                company.setSort("LandInfo");
                company.setName(str);
//                companyList.add(company);
                try {
                    mDao.add(company);
                } catch (Exception e) {
                    System.out.println(e.toString());
                }
            }
        }
//        mDao.add(companyList);
    }

    private static void sleep(long time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
