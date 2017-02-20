package com.eccang.amazon.service.crawl;

import com.eccang.base.SpringTestCase;
import com.eccang.spider.amazon.pojo.crawl.Department;
import com.eccang.spider.amazon.service.crawl.DepartmentService;
import com.eccang.spider.base.util.UrlUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2017/2/15 13:56
 */
public class DepartmentServiceTest extends SpringTestCase {

    @Autowired
    DepartmentService mService;

    @Test
    public void find() {
        List<Department> departments = mService.findAll();
        String departmentName;
        for (Department department : departments) {
            departmentName = department.depName;
            if (departmentName.contains("&amp;")) {
                System.out.println(department.depName);
                department.depName = departmentName.replace("&amp;", "&");
                System.out.println(department);
                mService.update(department);
            }
        }
    }

    @Test
    public void add() {
        List<Department> departments = new ArrayList<>();
        Department department = new Department();
        department.batchNum = "EC2017021710253813";
        department.depLevel = 2;
        department.depName = "Movies & TV";
        department.depTab = "Top 100 Paid";
        department.depUrl = "https://www.amazon.com/Best-Sellers-Appstore-Android-Movies-TV/zgbs/mobile-apps/9408765011/ref=zg_bs_nav_mas_1_mas";
        department.urlMD5 = UrlUtils.md5(department.depUrl);
        departments.add(department);
        mService.addAll(departments);
    }
}