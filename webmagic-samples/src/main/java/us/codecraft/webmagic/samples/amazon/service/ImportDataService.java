package us.codecraft.webmagic.samples.amazon.service;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.amazon.dao.ImportDataDao;
import us.codecraft.webmagic.samples.amazon.pojo.ImportData;

import java.util.List;

/**
 * @author Hardy
 * @version V0.1
 * @Description:
 * @date 2016/12/5 20:17
 */
@Service
public class ImportDataService {
    @Autowired
    ImportDataDao mImportDataDao;

    public List<ImportData> findBySite(String siteCode) {
        return mImportDataDao.findBySite(siteCode);
    }

    public List<ImportData> findAll() {
        return mImportDataDao.findAll();
    }

    public List<ImportData> find(String siteCode) {
        if(StringUtils.isNotEmpty(siteCode)) {
            return mImportDataDao.findBySite(siteCode);
        } else {
            return mImportDataDao.findAll();
        }
    }
}