package com.jyrd.exam.admin.controller;

import com.jyrd.exam.admin.service.SysDepartmentService;
import com.jyrd.exam.base.entity.SysDepartment;
import com.jyrd.exam.base.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Slf4j
@Controller
@CrossOrigin
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private SysDepartmentService departmentService;

    /**
     * 获取部门信息
     * @return
     */
    @RequestMapping("/getdepartList")
    @ResponseBody
    public ResponseVo getdepartList(){
        try {
            List<SysDepartment> departmentList = departmentService.list();
            return new ResponseVo(true,"获取部门信息成功",departmentList);
        } catch (Exception e) {
            log.error("获取部门信息错误：", e);
            return new ResponseVo(false,"获取部门信息失败");
        }
    }

    /**
     * 获取部门信息
     * @return
     */
    @RequestMapping("/SysConfig")
    public String SysConfig(){
        try {
            return "redirect:/";
        } catch (Exception e) {
            log.error("进入系统配置页面出错：", e);
            return "redirect:/";
        }
    }
}
