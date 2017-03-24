package com.eccang.amazon.service.crawl;

import com.eccang.base.SpringTestCase;
import com.eccang.spider.amazon.pojo.crawl.Department;
import com.eccang.spider.amazon.service.crawl.DepartmentService;
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
        department.batchNum = "EC2017022009204588";
        department.depLevel = 1;
        department.depName = "Clothing, Shoes & Jewelry";
        department.parentDepName = "Any Department";
        department.depTab = "";
        department.depUrl = "https://www.amazon.com/Best-Sellers/zgbs/fashion/ref=zg_bs_nav_0";
        departments.add(department);
        mService.addAll(departments);
    }
}