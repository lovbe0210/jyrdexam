package com.jyrd.exam.question.controller;

import com.jyrd.exam.base.entity.Question;
import com.jyrd.exam.base.entity.SysCatalog;
import com.jyrd.exam.base.entity.User;
import com.jyrd.exam.question.service.QuestionService;
import com.jyrd.exam.base.vo.ParamVo;
import com.jyrd.exam.base.vo.ResponsePageVo;
import com.jyrd.exam.base.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lovbe
 * @since 2021-03-19
 */
@Controller
@Slf4j
public class QuestionController {

    @Autowired
    private QuestionService questionService;


    @RequestMapping("/question/toList")
    public String toList(HttpServletRequest request) {
        try {
            User user = (User) request.getSession().getAttribute("user");
            if(user == null){
                return "redirect:/";
            }
            request.setAttribute("userName",user.getLoginName());
            List<SysCatalog> catalogList = questionService.getCatalogList();
            request.setAttribute("catalogList", catalogList);
            return "question/questionList";
        } catch (Exception e) {
            log.error("到达试题列表页面错误：", e);
            return "home/question/questionList";
        }
    }

    /**
     * 加载试题list
     * @param pageIn
     */
    @RequestMapping("/question/list")
    @ResponseBody
    public ResponsePageVo list(ParamVo pageIn) {
        try {
            return questionService.getListpage(pageIn);
        } catch (Exception e) {
            log.error("试题列表错误：", e);
            return new ResponsePageVo();
        }
    }


    /**
     * 获取问题分类列表
     * @return
     */
    @RequestMapping("/question/questionTypeTreeList")
    @ResponseBody
    public List<Map<String, Object>> questionTypeTreeList() {
        try {
            List<Map<String, Object>> questionTypeTreeList = questionService.getQuestionTypeTreeList();
            return questionTypeTreeList;
        } catch (Exception e) {
            log.error("获取试题分类树错误：", e);
            return new ArrayList<Map<String,Object>>();
        }
    }

    /**
     * 跳转到添加试题页面
     * @param request
     * @param questionTypeId
     * @return
     */
    @RequestMapping("/question/toAdd")
    public String toAdd(HttpServletRequest request, Integer questionTypeId) {
        try {
            User user = (User) request.getSession().getAttribute("user");
            if(user == null){
                return "redirect:/";
            }
            request.setAttribute("userName",user.getLoginName());
            List<SysCatalog> catalogList = questionService.getCatalogList();
            request.setAttribute("catalogList", catalogList);
            return "question/questionEdit";
        } catch (Exception e) {
            log.error("到达添加试题页面错误：", e);
            return "home/question/questionEdit";
        }
    }


    /**
     * 添加试题
     * @param question
     * @param answer
     * @return
     */
    @RequestMapping("/question/doAdd")
    @ResponseBody
    public ResponseVo doAdd(Question question, String[] answer) {
        try {
            questionService.addAndUpdate(question, answer);
            return new ResponseVo(true, "添加成功");
        } catch (Exception e) {
            log.error("完成添加试题错误：", e);
            return new ResponseVo(false, "添加失败：" + e.getMessage());
        }
    }
}
