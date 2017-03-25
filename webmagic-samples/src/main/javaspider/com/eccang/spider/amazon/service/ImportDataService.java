package com.eccang.spider.amazon.service;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.eccang.spider.amazon.dao.ImportDataDao;
import com.eccang.spider.amazon.pojo.ImportData;

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
    private ImportDataDao mImportDataDao;

    public List<ImportData> findBySite(String siteCode,int limit) {
        return mImportDataDao.findBySite(siteCode, limit);
    }

    public List<ImportData> findAll(int limit) {
        return mImportDataDao.findAll(limit);
    }

    public List<ImportData> find(String siteCode, int limit) {
        if (StringUtils.isNotEmpty(siteCode)) {
            return mImportDataDao.findBySite(siteCode,limit);
        } else {
            return findAll(limit);
        }
    }
}