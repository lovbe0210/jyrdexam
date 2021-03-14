package com.jyrd.exam.exam.controller;


import com.jyrd.exam.base.entity.*;
import com.jyrd.exam.base.mapper.ExamUserQuestionMapper;
import com.jyrd.exam.base.vo.ParamVo;
import com.jyrd.exam.base.vo.ResponsePageVo;
import com.jyrd.exam.base.vo.ResponseVo;
import com.jyrd.exam.exam.service.ExamDepartmentService;
import com.jyrd.exam.exam.service.ExamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lovbe
 * @since 2021-03-19
 */
@Slf4j
@Controller
@RequestMapping("/exam")
@CrossOrigin
public class ExamController {

    @Autowired
    private ExamService examService;
    @Autowired
    private ExamDepartmentService examDeptService;

    @Autowired
    private ExamUserQuestionMapper examUserQuestionMapper;

    /**
     * 到达考试管理页面
     * @param model
     * @return
     */
    @RequestMapping("/toList")
    public String toList(Model model,HttpServletRequest request) {
        try {
            User user = (User) request.getSession().getAttribute("user");
            if(user == null){
                return "redirect:/";
            }
            model.addAttribute("userName",user.getLoginName());
            List<SysCatalog> catalogList = examService.getCatalogList();
            model.addAttribute("catalogList", catalogList);
            return "exam/examList";
        } catch (Exception e) {
            log.error("到达试卷列表页面错误：", e);
            return "home/paper/paperList";
        }
    }

    /**
     * 到达添加考试页面
     * @param model
     * @return
     */
    @RequestMapping("/toAdd")
    public String toAdd(Model model,HttpServletRequest request){
        try {
            User user = (User) request.getSession().getAttribute("user");
            if(user == null){
                return "redirect:/";
            }
            model.addAttribute("userName",user.getLoginName());
            // 加载考试分类列表
            List<SysCatalog> catalogList = examService.getCatalogList();
            model.addAttribute("catalogList",catalogList);
            // 此处放置一个空的exam，为了thymeleaf页面能够正常解析
            model.addAttribute("exam",new Exam());
            // 查询所有部门，然后再添加考试界面选择
            List<SysDepartment> deptList = examDeptService.getAllDept();
            model.addAttribute("deptList",deptList);
            return "exam/examEdit";
        } catch (Exception e) {
            log.error("到达考试添加列表页面错误：", e);
            return "home/paper/paperList";
        }
    }

    /**
     * 内嵌添加试卷页面
     *
     * @param model
     * @return PageResult
     */
    @RequestMapping("/toPaperAdd")
    public String toPaperAdd(Model model, HttpServletRequest request) {
        try {
            User user = (User) request.getSession().getAttribute("user");
            if(user == null){
                return "redirect:/";
            }
            model.addAttribute("userName",user.getLoginName());
            return "exam/paperAdd";
        } catch (Exception e) {
            log.error("到达添加试卷页面错误：", e);
            return "home/exam/paperAdd";
        }
    }

    /**
     * 后端获取考试列表
     * @param param
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public ResponsePageVo list(ParamVo param) {
        try {
            return examService.getListpage(param);
        } catch (Exception e) {
            log.error("试卷列表错误：", e);
            return new ResponsePageVo();
        }
    }

    /**
     * 添加或修改考试
     * @param exam
     * @param examClass
     * @return
     */
    @RequestMapping("/doAdd")
    @ResponseBody
    public ResponseVo doAdd(Exam exam, @RequestParam("examClass") List<Integer> examClass) {
        try {
            examService.addAndUpdate(exam, examClass);
            return new ResponseVo(true, "添加成功");
        } catch (Exception e) {
            log.error("完成添加考试错误：", e);
            return new ResponseVo(false, "添加失败：" + e.getMessage());
        }
    }

  /**
   * 完成发布
   *
   * @param id
   * @return PageResult
   */
  @RequestMapping("/doPublish")
  @ResponseBody
  public ResponseVo doPublish(Integer id) {
    try {
      examService.doPublish(id);
      return new ResponseVo(true, "发布成功");
    } catch (Exception e) {
      log.error("完成发布错误：", e);
      return new ResponseVo(false, "发布失败：" + e.getMessage());
    }
  }


  /**
   * 前端获取考试列表
   * @param param
   * @return
   */
  @RequestMapping("/getExamList")
  @ResponseBody
  public ResponsePageVo getExamList(ParamVo param) {
    try {
      param.setEight("-1");
      return examService.getListpage(param);
    } catch (Exception e) {
      log.error("试卷列表错误：", e);
      return new ResponsePageVo();
    }
  }

    /**
     * 首页获取我参与的考试
     * @param userId
     * @return
     */
    @RequestMapping("/getMyExamList")
    @ResponseBody
    public ResponseVo getMyExamList(Integer userId) {
        try {
            return examService.getMyExamList(userId);
        } catch (Exception e) {
            log.error("试卷列表错误：", e);
            return new ResponseVo(false,"获取我参与的考试列表错误");
        }
    }

  /**
   * 初始化考试用户信息以及完成组卷
   * @param examUser
   * @return
   */
  @RequestMapping("/initUserExam")
  @ResponseBody
  public ResponseVo initUserExam(@RequestBody ExamUser examUser) {
    try {
      return examService.initUserExam(examUser);
    } catch (Exception e) {
      log.error("用户组卷错误：", e);
      return new ResponseVo(false,e.getMessage(),null);
    }
  }

    /**
     * 获取考试用户信息
     *
     * @param examUser
     * @return
     */
    @RequestMapping("/getExamUserInfo")
    @ResponseBody
  public ResponseVo getExamUserInfo(@RequestBody ExamUser examUser){
        try {
            ExamUser examUserInfo = examService.getExamUser(examUser);
            if(examUserInfo == null){
                return new ResponseVo(false,"获取考试用户信息失败");
            }else {
                return new ResponseVo(true,"获取考试用户信息成功",examUserInfo);
            }
        } catch (Exception e) {
            log.error("获取考试用户信息错误：", e);
            return new ResponseVo(false,"获取考试用户信息失败");
        }
  }

    /**
     * 获取考试用户的试题集合
     * @param examUserQuestion
     * @return
     */
    @RequestMapping("/getExamQuestionList")
    @ResponseBody
  public ResponseVo getExamQuestionList(ExamUserQuestion examUserQuestion){
    try {
        // 延时2秒
        try { TimeUnit.MILLISECONDS.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
        return examService.getExamQuestionList(examUserQuestion);
    }catch (Exception e){
        log.error("获取考试试题列表错误：", e);
        return new ResponseVo(false,"获取考试试题列表错误");
    }   
  }

    /**
     * 保存当前试题答案
     * @param examUserQuestion
     * @return
     */
    @RequestMapping("/saveCurrentAnswer")
    @ResponseBody
    public ResponseVo saveCurrentAnswer(ExamUserQuestion examUserQuestion){
        try {
            return examService.saveCurrentAnswer(examUserQuestion);
        }catch (Exception e){
            log.error("保存当前试题答案错误：", e);
            return new ResponseVo(false,"保存当前试题答案错误");
        }
    }

    /**
     * 前端用户主动交卷
     * @param examUser
     * @return
     */
    @RequestMapping("/submitExam")
    @ResponseBody
    public ResponseVo submitExam(ExamUser examUser){
        try {
            return examService.submitExam(examUser);
        }catch (Exception e){
            log.error("交卷错误：", e);
            return new ResponseVo(false,"交卷错误");
        }
    }

    /**
     * 通过id查找ExamUser
     * @param examUser
     * @return
     */
    @RequestMapping("/getExamUserById")
    @ResponseBody
    public ResponseVo getExamUserById(ExamUser examUser){
        try {
            return examService.getExamUserById(examUser.getId());
        }catch (Exception e){
            log.error("交卷错误：", e);
            return new ResponseVo(false,"交卷错误");
        }
    }


}

