package com.fosung.ksh.organization.controller;

import com.fosung.ksh.organization.task.OrgLifeMapReport;
import com.fosung.ksh.organization.task.OrgLifeReportCreateTask;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author toquery
 * @version 1
 */
@RestController
@RequestMapping(value = "/test")
public class TestController {


    @Resource
    private OrgLifeMapReport orgLifeMapReport;

    @Resource
    private OrgLifeReportCreateTask orgLifeReportCreateTask;

    @GetMapping("/1")
    public String step1(){
        orgLifeReportCreateTask.createOrgLifeReport();
        return "oko";
    }
    @GetMapping("/2")
    public String step2(){
        orgLifeMapReport.orgLifeMapReport();
        return "oko";
    }
}
