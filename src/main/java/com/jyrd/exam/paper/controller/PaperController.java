package com.jyrd.exam.paper.controller;

import com.jyrd.exam.base.entity.Paper;
import com.jyrd.exam.base.entity.PaperConfig;
import com.jyrd.exam.base.entity.SysCatalog;
import com.jyrd.exam.base.entity.User;
import com.jyrd.exam.base.vo.*;
import com.jyrd.exam.paper.service.PaperService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lovbe
 * @since 2021-03-19
 */
@Controller
@RequestMapping("/paper")
@Slf4j
public class PaperController {

    @Autowired
    private PaperService paperService;

    /**
     * 达到试卷列表页面
     * @param model
     * @param nav
     * @return
     */
    @RequestMapping("/toList")
    public String toList(Model model, HttpServletRequest request) {
        try {
            User user = (User) request.getSession().getAttribute("user");
            if(user == null){
                return "redirect:/";
            }
            model.addAttribute("userName",user.getLoginName());
            List<SysCatalog> catalogList = paperService.getCatalogList();
            model.addAttribute("catalogList", catalogList);
            return "paper/paperList";
        } catch (Exception e) {
            log.error("到达试卷列表页面错误：", e);
            return "home/paper/paperList";
        }
    }

    /**
     * 达到试卷添加页面
     * @return
     */
    @RequestMapping("/toAdd")
    public String toAdd(Model model, HttpServletRequest request) {
        try {
            User user = (User) request.getSession().getAttribute("user");
            if(user == null){
                return "redirect:/";
            }
            model.addAttribute("userName",user.getLoginName());
            List<SysCatalog> catalogList = paperService.getCatalogList();
            model.addAttribute("catalogList", catalogList);
            return "paper/paperEdit";
        } catch (Exception e) {
            log.error("到达添加试卷页面错误：", e);
            return "home/paper/paperEdit";
        }
    }

    /**
     * 添加试卷
     * @param paper
     * @return
     */
    @RequestMapping("/doAdd")
    @ResponseBody
    public ResponseVo doAdd(Paper paper) {
        try {
            paper.setState(0);
            paperService.save(paper);
            return new ResponseVo(true, "添加成功");
        } catch (Exception e) {
            log.error("完成添加试卷错误：", e);
            return new ResponseVo(false, "添加失败：" + e.getMessage());
        }
    }

    /**
     * 获取试卷列表
     * @param param
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public ResponsePageVo list(ParamVo param) {
        try {
            return paperService.getListpage(param);
        } catch (Exception e) {
            log.error("试卷列表错误：", e);
            return new ResponsePageVo();
        }
    }

    /**
     * 配置试卷
     * @param model
     * @param id
     * @return
     */
    @RequestMapping("/toConfig")
    public String toConfig(Model model, Integer id,HttpServletRequest request) {
        try {
            User user = (User) request.getSession().getAttribute("user");
            if(user == null){
                return "redirect:/";
            }
            model.addAttribute("userName",user.getLoginName());
            Paper paper = paperService.getById(id);
            model.addAttribute("paper", paper);
            PaperQuestionVo paperQuestionVo = paperService.getPaperQuestion(paper.getCatalogId());
            model.addAttribute("paperQuestionVo",paperQuestionVo);

            PaperConfig paperConfig = paperService.getPaperConfig(id);
            if(paperConfig != null){
                model.addAttribute("paperConfig",paperConfig);
            }else {
                model.addAttribute("paperConfig",new PaperConfig());
            }
            return "paper/paperConfig";
        } catch (Exception e) {
            log.error("到达配置试卷页面错误：", e);
            return "home/paper/paperCfg";
        }
    }

    /**
     * 添加试卷配置的试题类型
     * @param questionTypeVo
     * @return
     */
    @RequestMapping("/addQuestionType")
    @ResponseBody
    public ResponseVo addQuestionType(QuestionTypeVo questionTypeVo){
        Paper paper = paperService.getById(questionTypeVo.getPaperId());
        PaperQuestionVo paperQuestionVo = paperService.getPaperQuestion(paper.getCatalogId());
        int questionType = questionTypeVo.getQuestionType();
        int total = 0;
        switch (questionType){
            case 2:
                total = paperQuestionVo.getRadioCount();
                break;
            case 3:
                total = paperQuestionVo.getCheckboxCount();
                break;
            case 4:
                total = paperQuestionVo.getJudgeCount();
                break;
            case 5:
                total = paperQuestionVo.getScannerCount();
                break;
            case 6:
                total = paperQuestionVo.getQAnswerCount();
                break;
        }
        if(questionTypeVo.getQuestionCount() > total){
            return new ResponseVo(false,"当前题型最多可选数量为"+total+"个");
        }


        boolean flag = paperService.addQuestionType(questionTypeVo);
        if(flag){
            return new ResponseVo(true,"添加成功");
        }else {
            return new ResponseVo(false,"当前类型已经添加，请勿重复添加");
        }
    }

}
