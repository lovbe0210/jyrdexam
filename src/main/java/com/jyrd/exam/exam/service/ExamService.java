package com.jyrd.exam.exam.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jyrd.exam.base.entity.Exam;
import com.jyrd.exam.base.entity.ExamUser;
import com.jyrd.exam.base.entity.ExamUserQuestion;
import com.jyrd.exam.base.entity.SysCatalog;
import com.jyrd.exam.base.vo.ParamVo;
import com.jyrd.exam.base.vo.ResponsePageVo;
import com.jyrd.exam.base.vo.ResponseVo;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lovbe
 * @since 2021-03-19
 */
public interface ExamService extends IService<Exam> {

    ExamUser getExamUser(ExamUser examUser) throws InterruptedException, SchedulerException, ClassNotFoundException;

    ResponsePageVo getListpage(ParamVo param);

    List<SysCatalog> getCatalogList();

    void addAndUpdate(Exam exam, List<Integer> examClass);

    void doPublish(Integer id) throws ParseException, InterruptedException, SchedulerException, ClassNotFoundException;

    void timeouted(Scheduler scheduler, Map<String,Object> params) throws SchedulerException;

    ResponseVo initUserExam(ExamUser examUser);

    ResponseVo getExamQuestionList(ExamUserQuestion examUserQuestion);

    void forceSubmitExam(Scheduler scheduler, Map<String, Object> params) throws SchedulerException;

    ResponseVo saveCurrentAnswer(ExamUserQuestion examUserQuestion);

    ResponseVo submitExam(ExamUser examUser);

    ResponseVo getExamUserById(Integer id);

    ResponseVo getMyExamList(Integer userId);
}
