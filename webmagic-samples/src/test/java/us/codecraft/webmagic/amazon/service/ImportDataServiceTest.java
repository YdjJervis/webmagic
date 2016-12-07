package us.codecraft.webmagic.amazon.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.base.SpringTestCase;
import us.codecraft.webmagic.samples.amazon.pojo.ImportData;
import us.codecraft.webmagic.samples.amazon.service.ImportDataService;

import java.util.List;

/**
 * @author Hardy
 * @version V0.1
 * @Description:
 * @date 2016/12/5 20:18
 */
public class ImportDataServiceTest extends SpringTestCase {

    @Autowired
    ImportDataService mImportDataService;

    @Test
    public void findBySite() {
       String siteCode = "US";
        List<ImportData> importDataList = mImportDataService.findBySite(siteCode);
        System.out.println(importDataList);
    }

    @Test
    public void findAll() {
        List<ImportData> importDataList = mImportDataService.findAll();
        System.out.println(importDataList);
    }

}