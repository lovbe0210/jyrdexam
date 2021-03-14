package com.jyrd.exam.exam.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jyrd.exam.base.common.quartzTask.ExamTimeOutJob;
import com.jyrd.exam.base.common.quartzTask.SubmitExamJob;
import com.jyrd.exam.base.entity.*;
import com.jyrd.exam.base.mapper.*;
import com.jyrd.exam.paper.service.PaperService;
import com.jyrd.exam.base.common.utils.DateUtil;
import com.jyrd.exam.base.vo.ExamUserQuestionVo;
import com.jyrd.exam.base.vo.ParamVo;
import com.jyrd.exam.base.vo.ResponsePageVo;
import com.jyrd.exam.base.vo.ResponseVo;
import com.jyrd.exam.base.common.quartzTask.ExamScheduler;
import com.jyrd.exam.base.common.utils.CronUtils;
import com.jyrd.exam.exam.service.ExamService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class ExamServiceImpl extends ServiceImpl<ExamMapper, Exam> implements ExamService {
    @Autowired
    private ExamMapper examMapper;
    @Autowired
    private SysCatalogMapper catalogMapper;
    @Autowired
    private ExamDepartmentMapper examDepartmentMapper;
    @Autowired
    private ExamUserMapper examUserMapper;
    @Autowired
    private ExamUserQuestionMapper examUserQuestionMapper;
    @Autowired
    private PaperMapper paperMapper;
    @Autowired
    private PaperService paperService;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private SysDepartmentMapper sysDepartmentMapper;
    @Autowired
    private QuartzTaskMapper quartzTaskMapper;

    @Autowired
    private RedisErrorQuestionService redisErrorQuestionService;

    @Override
    public ResponsePageVo getListpage(ParamVo param) {
        QueryWrapper<Exam> examQueryWrapper = new QueryWrapper<>();

        String name = param.getTwo();
        String catalog = param.getSix();
        String stat = param.getSeven();
        String frontStat = param.getEight();
        if(!StringUtils.isEmpty(name)){
            examQueryWrapper.like("NAME",name);
        }
        if(!StringUtils.isEmpty(catalog)){
            examQueryWrapper.eq("catalog_id",catalog);
        }
        if(!StringUtils.isEmpty(stat)){
            examQueryWrapper.eq("STATE",stat);
        }
        if(!StringUtils.isEmpty(frontStat) && StringUtils.isEmpty(stat)){
          examQueryWrapper.ne("STATE",0);
        }

        // 查询总数
        Integer count = examMapper.selectCount(examQueryWrapper);
        ResponsePageVo responsePageVo = new ResponsePageVo();
        responsePageVo.setTotal(count);

        examQueryWrapper.orderByAsc("STATE");
        Page<Exam> page = new Page<>(param.getCurrentPage(), param.getPageSize());
        page.setTotal(count);

        Page<Exam> examPage = examMapper.selectPage(page, examQueryWrapper);
        List<Exam> records = examPage.getRecords();
        List<Map<String, Object>> mapList = new ArrayList<>();
        for (Exam record : records) {
          HashMap<String, Object> map = new HashMap<>();
          map.put("ID",record.getId());
          map.put("NAME",record.getName());
          map.put("Exam_Duration",record.getExamDuration());
          map.put("Exam_Cunt",record.getMaxExamCount());
          Paper paper = paperMapper.selectById(record.getPaperId());
          String paperName = paper.getName();
          String totalScore = paper.getTotleScore().toString();
          map.put("PAPER_NAME",paperName);
          map.put("PAPER_TOTLE_SCORE",totalScore);
          map.put("PASS_SCORE",record.getPassScore().toString());
          SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
          String startTime = sdf.format(record.getStartTime());
          String endTime = sdf.format(record.getEndTime());
          map.put("START_TIME_STR",startTime);
          map.put("END_TIME_STR",endTime);
          switch(record.getState()){
            case 0:
              map.put("STATE_NAME","未发布");
              break;
            case 1:
              map.put("STATE_NAME","进行中");
              break;
            case 2:
              map.put("STATE_NAME","已结束");
              break;
          }
          mapList.add(map);
        }
        responsePageVo.setRows(mapList);
        return responsePageVo;
    }

    @Override
    public List<SysCatalog> getCatalogList() {
        return catalogMapper.selectList(null);
    }

    @Override
    public void addAndUpdate(Exam exam, List<Integer> examClass) {
        if(exam.getId() == null){
          // 补充状态
          exam.setState(0);
            examMapper.insert(exam);
            for (Integer examDeptId : examClass) {
                ExamDepartment examDepartment = new ExamDepartment();
                examDepartment.setDeptId(examDeptId);
                examDepartment.setExamId(exam.getId());
                SysDepartment sysDepartment = sysDepartmentMapper.selectById(examDeptId);
                examDepartment.setDeptName(sysDepartment.getDeptName());
                examDepartmentMapper.insert(examDepartment);
            }
        }else {
            examMapper.updateById(exam);
            for (Integer examDeptId : examClass) {
                // 先删除再重新添加
                QueryWrapper<ExamDepartment> examDeptQueryWrapper = new QueryWrapper<>();
                examDeptQueryWrapper.eq("exam_id",exam.getId());
                examDepartmentMapper.delete(examDeptQueryWrapper);

                ExamDepartment examDepartment = new ExamDepartment();
                examDepartment.setDeptId(examDeptId);
                examDepartment.setExamId(exam.getId());
                ExamDepartment department = examDepartmentMapper.selectById(examDeptId);
                examDepartment.setDeptName(department.getDeptName());
                examDepartmentMapper.insert(examDepartment);
            }
        }
    }

    /**
     * 发布考试
     * @param id
     * @throws ParseException
     * @throws InterruptedException
     * @throws SchedulerException
     */
    @Override
    public void doPublish(Integer id) throws ParseException, InterruptedException, SchedulerException, ClassNotFoundException {
    Exam exam = examMapper.selectById(id);
    if(exam!= null && exam.getState() == 0){
      exam.setState(1);
      examMapper.updateById(exam);

      // TODO 设置定时任务，修改考试状态
      Map<String,Object> params = new HashMap<>();
      params.put("examService",this);
      String quartzName = "考试ID:"+exam.getId()+"--修改考试状态为已过期";
      params.put("quartzName",quartzName);
      params.put("examId",exam.getId());
      Date endTime = exam.getEndTime();
      String cron = CronUtils.getCron(endTime);
      params.put("cron",cron);
      params.put("quartTaskClass", ExamTimeOutJob.class.getName());
      setQuartTask(params);
    }else {
      //考试已发布或者已结束
      throw new RuntimeException("考试已发布或已结束,无法再次发布");
    }
  }


    /**
     * 内部方法，考试自动过期
     * @param scheduler
     * @param params
     * @throws SchedulerException
     */
    public void timeouted(Scheduler scheduler, Map<String,Object> params) throws SchedulerException {
        String jobDetailName = (String) params.get("jobDetailName");
        String cronTriggerName = (String) params.get("cronTriggerName");
        ExamScheduler.removeJob(scheduler, jobDetailName,cronTriggerName);
        String time = LocalDateTime.now().toString();
        log.info(time +" 定时任务结束 **********");

        // 改变数据库中的定时任务状态
        QueryWrapper<QuartzTask> quartzQueryWrapper = new QueryWrapper<>();
        String quartzName = (String) params.get("quartzName");
        quartzQueryWrapper.eq("NAME",quartzName);
        QuartzTask quartzTask = quartzTaskMapper.selectOne(quartzQueryWrapper);
        if(quartzTask != null){
          quartzTask.setState(2);
          quartzTaskMapper.updateById(quartzTask);

          // 修改考试状态
          Integer examId = (Integer) params.get("examId");
          Exam exam = examMapper.selectById(examId);
          if(exam != null && exam.getState() != 2){
            exam.setState(2);
            examMapper.updateById(exam);
          }
        }
    }

    @Override
    public ResponseVo initUserExam(ExamUser examUser) {
      ResponseVo responeVo = new ResponseVo();
        // * 先查询是否已经有考试记录，如果没有在进行初始化，如果已经有考试用户，则判断考试是否
        //   已经开始，如果考试已经开始，则直接进入考试，如果没有则判断考试次数是否已经到达最大值
        //   如果没有则重新初始化试题信息，然后考试次数加1，如果已经达到最大值，则返回错误信息
      Exam exam = examMapper.selectById(examUser.getExamId());

      QueryWrapper<ExamUser> examUserQueryWrapper = new QueryWrapper<>();
      examUserQueryWrapper.eq("EXAM_ID",examUser.getExamId());
      examUserQueryWrapper.eq("USER_ID",examUser.getUserId());
      ExamUser selectOne = examUserMapper.selectOne(examUserQueryWrapper);
      if(selectOne == null){
          // 第一次开始，保存ExamUser和ExamUserQuestion
          // 1. 生成用户试卷：考试状态为：未考试，已考次数为0,最高分0.0
          examUser.setState(1);
          examUser.setExamedCount(0);
          examUser.setMaxScore(0.0);
          // 2. 在查询试卷策略:考试时长
          examUser.setExamDuration(Integer.parseInt(exam.getExamDuration()) * 60);
          // 3. 保存考试用户
          examUserMapper.insert(examUser);
          // 4. 保存考试用户试题  随机抽题保存用户试卷 *****
          allQuestionConfig(examUser, exam);

          // 5. 组卷成功
          responeVo.setSucc(true);
          responeVo.setMsg("用户考试试卷初始化成功");
      }else {
          // 已经开始考试，判断考试是否已经开始
          Integer state = selectOne.getState();
          if(state == 2 || state == 1){
              // 无需操作直接进入考试
              responeVo.setSucc(true);
              responeVo.setMsg("正在考试中，重新回到当前考试");
          }else{
              // 考试结束的状态，判断考试次数是否已经达到最大
              Integer examedCount = selectOne.getExamedCount();
              Integer maxExamCount = exam.getMaxExamCount();
              if(examedCount >= maxExamCount){
                  // 已达到最大考试次数
                  responeVo.setSucc(false);
                  responeVo.setMsg("已达到最大考试次数");
              }else{
                  // 重置ExamUser状态，考试次数加1，然后重新组卷，保存ExamUserQuestion
                  selectOne.setState(1);
                  selectOne.setExamedCount(selectOne.getExamedCount()+1);
                  examUserMapper.updateById(selectOne);
                  allQuestionConfig(selectOne,exam);
                  responeVo.setSucc(true);
                  responeVo.setMsg("用户考试试卷初始化成功");
              }
          }
      }
      // 4. 返回组卷状态
      return responeVo;
  }

    /**
     * 开始答题，获取考试用户信息
     * @param examUser
     * @return
     * @throws InterruptedException
     * @throws SchedulerException
     */
    @Override
    public ExamUser getExamUser(ExamUser examUser) throws InterruptedException, SchedulerException, ClassNotFoundException {
        /**
         * 判断状态是否==1才进行修改
         * 1. 先获取用户考试信息，然后改变考试状态为考试中
         * 2. 设置考试开始时间为当前时间
         * 3. 回填信息到数据库
         * 4. 设置定时器，指定时间检查用户试卷是否已经提交，如果没有则自动提交试卷
         */
        Exam exam = examMapper.selectById(examUser.getExamId());

        QueryWrapper<ExamUser> examUserQueryWrapper = new QueryWrapper<>();
        examUserQueryWrapper.eq("EXAM_ID",examUser.getExamId());
        examUserQueryWrapper.eq("USER_ID",examUser.getUserId());
        ExamUser selectOne = examUserMapper.selectOne(examUserQueryWrapper);
        if(selectOne != null && (selectOne.getState() == 1 || selectOne.getState() == 2)
                && selectOne.getExamedCount() <= exam.getMaxExamCount()){
            if(selectOne.getState() == 1){
                selectOne.setState(2);
                selectOne.setBeginTime(new Date());
                examUserMapper.updateById(selectOne);
                // 开启定时任务，强制交卷
                Map<String,Object> params = new HashMap<>();
                params.put("examService",this);
                String quartzName = "考试用户ExamUserId:"+selectOne.getId()+"第"+selectOne.getExamedCount()+"次考试"+"--强制自动交卷";
                params.put("quartzName",quartzName);
                params.put("examUserId",selectOne.getId());
                params.put("quartTaskClass",SubmitExamJob.class.getName());
                Date date = DateUtil.addSeconds(selectOne.getBeginTime(),selectOne.getExamDuration());
                String cron = CronUtils.getCron(date);
                params.put("cron",cron);
                String uuid = UUID.randomUUID().toString().replace("-", "");
                params.put("jobDetailName","submitExam"+uuid);
                params.put("cronTriggerName","submitExam"+uuid);
                setQuartTask(params);
            }
            return selectOne;
        }
        return null;
    }

    /**
     * 内部方法，自动交卷
     * @param scheduler
     * @param params
     */
    @Override
    public void forceSubmitExam(Scheduler scheduler, Map<String, Object> params) throws SchedulerException {
        String jobDetailName = (String) params.get("jobDetailName");
        String cronTriggerName = (String) params.get("cronTriggerName");
        ExamScheduler.removeJob(scheduler, jobDetailName,cronTriggerName);
        String time = LocalDateTime.now().toString();
        log.info(time +" 定时任务结束 "+(String) params.get("quartzName")+"**********");

        // 延迟10秒时间，考虑用户浏览器时间显示延迟、网络延迟等因素
        try { TimeUnit.SECONDS.sleep(10);} catch (InterruptedException e) {e.printStackTrace();}

        // 改变数据库中的定时任务状态
        QueryWrapper<QuartzTask> quartzQueryWrapper = new QueryWrapper<>();
        String quartzName = (String) params.get("quartzName");
        quartzQueryWrapper.eq("NAME",quartzName);
        QuartzTask quartzTask = quartzTaskMapper.selectOne(quartzQueryWrapper);
        if(quartzTask != null){
            quartzTask.setState(2);
            quartzTaskMapper.updateById(quartzTask);

            // 先判断用户是否自助交卷，如果没有在进行强制交卷
            Integer examUserId = (Integer) params.get("examUserId");
            ExamUser examUser = examUserMapper.selectById(examUserId);
            if(examUser != null && examUser.getState() == 2){
                examUser.setState(4);
                examUserMapper.updateById(examUser);
                // 交卷核算最终成绩
                calculateTotalScore(examUser);
                // 删除试题
                deleteExamUserQuestionS(examUser.getId());
            }

        }
    }

    /**
     * 保存当前试题答案
     * @param examUserQuestion
     * @return
     */
    @Override
    public ResponseVo saveCurrentAnswer(ExamUserQuestion examUserQuestion) {
        examUserQuestionMapper.updateById(examUserQuestion);
        return new ResponseVo(true,"保存成功");
    }

    /**
     * 用户主动交卷
     * @param examUser
     * @return
     */
    @Override
    public ResponseVo submitExam(ExamUser examUser) {
        ExamUser selectById = examUserMapper.selectById(examUser.getId());
        if(selectById != null && selectById.getState() == 2){
            // 改变状态
            selectById.setState(3);
            // 核算成绩
            calculateTotalScore(selectById);
            // 删除用户当前生成的试题
            deleteExamUserQuestionS(selectById.getId());
            // 取消定时任务
            String quartzName = "考试用户ExamUserId:"+selectById.getId()+"第"+selectById.getExamedCount()+"次考试--强制自动交卷";
            QueryWrapper<QuartzTask> quartzTaskQW = new QueryWrapper<>();
            quartzTaskQW.eq("NAME",quartzName);
            QuartzTask quartzTask = quartzTaskMapper.selectOne(quartzTaskQW);

            String jobDetailName = quartzTask.getJobDetailName();
            String cronTriggerName = quartzTask.getCronTriggerName();
            ExamScheduler.removeJob(ExamScheduler.scheduler, jobDetailName,cronTriggerName);
            // 改变数据库定时任务的状态
            quartzTask.setState(3);
            quartzTaskMapper.updateById(quartzTask);
        }
        return new ResponseVo(true,"交卷成功",selectById.getId());
    }

    /**
     * 根据Id获取ExamUser
     * @param id
     * @return
     */
    @Override
    public ResponseVo getExamUserById(Integer id) {
        ExamUser examUser = examUserMapper.selectById(id);
        return new ResponseVo(true,"获取ExamUser信息成功",examUser);
    }

    @Override
    public ResponseVo getMyExamList(Integer userId) {
        QueryWrapper<ExamUser> examUserQueryWrapper = new QueryWrapper<>();
        examUserQueryWrapper.eq("USER_ID",userId);
        List<ExamUser> examUserList = examUserMapper.selectList(examUserQueryWrapper);
        HashMap<String, Object> map ;
        List<Map<String,Object>> myExamList = new ArrayList<>(examUserList.size());
        for (ExamUser examUser : examUserList) {
            map = new HashMap<>();
            Exam exam = examMapper.selectById(examUser.getExamId());
            if(exam.getState() == 1) {
                map.put("examId",exam.getId());
                map.put("name", exam.getName());
                map.put("count", exam.getMaxExamCount() - examUser.getExamedCount());
                map.put("duration", exam.getExamDuration() + "分钟");
                map.put("score", examUser.getMaxScore());
                map.put("buttonText", "继续考试");
                myExamList.add(map);
            }
        }
        return new ResponseVo(true,"获取我参与的考试列表成功",myExamList);
    }

    /**
     * 内部方法，删除当前交卷用户所有考试试题
     * @param examUserId
     */
    private void deleteExamUserQuestionS(Integer examUserId) {
        QueryWrapper<ExamUserQuestion> examUserQuestionQW = new QueryWrapper<>();
        examUserQuestionQW.eq("EXAM_USER_ID",examUserId);
        examUserQuestionMapper.delete(examUserQuestionQW);
    }

    /**
     * 内部方法：核算最终成绩
     * @param examUser
     */
    private void calculateTotalScore(ExamUser examUser) {
        /**
         * 1. 根据examUserId查询所有的ExamUserQuestion
         * 2. 逐个计算每项分数 // TODO 后期可用多线程来完成
         * 3. 最后累加到ExamUser的最高分
         */
        double totalScore = 0;
        QueryWrapper<ExamUserQuestion> examUserQuestionQR = new QueryWrapper<>();
        examUserQuestionQR.eq("EXAM_USER_ID",examUser.getId());
        examUserQuestionQR.orderByAsc("ID");
        List<ExamUserQuestion> examUserQuestionList = examUserQuestionMapper.selectList(examUserQuestionQR);
        for (ExamUserQuestion examUserQuestion : examUserQuestionList) {
            Integer questionId = examUserQuestion.getQuestionId();
            Question question = questionMapper.selectById(questionId);
            if(question.getAnswer().equals(examUserQuestion.getAnswer())){
                totalScore += examUserQuestion.getSingleScore();
            }else{
                // 错题，计入redis中进行缓存

                // 先判断当前试题是否已经答错过
                redisErrorQuestionService.saveErrorQ(examUserQuestion, questionId, question);
            }
        }
        examUser.setMaxScore(totalScore);
        examUserMapper.updateById(examUser);
    }



    /**
     * 内部方法，设置考试已过期定时任务
     * @param params
     * @throws SchedulerException
     * @throws InterruptedException
     */
    private void setQuartTask(Map<String, Object> params) throws SchedulerException, InterruptedException, ClassNotFoundException {
        ExamScheduler.startQuartzTask(params);
        // 这里还应该将定时任务添加到数据库，管理员可以直接查看定时任务进行的情况
        QuartzTask quartzTask = new QuartzTask();
        quartzTask.setName((String) params.get("quartzName"));
        quartzTask.setJobClass((String) params.get("quartTaskClass"));
        String cron = (String) params.get("cron");
        quartzTask.setJobDetailName((String) params.get("jobDetailName"));
        quartzTask.setCronTriggerName((String) params.get("cronTriggerName"));
        quartzTask.setCron(cron);
        quartzTask.setState(1);  //1启动2停止
        quartzTaskMapper.insert(quartzTask);
    }

    @Override
    public ResponseVo getExamQuestionList(ExamUserQuestion examUserQuestion) {
        /**
         * 这里需要两表联查，ExamUserQuestion和Question
         * 查询结果主要显示每个试题的title和answer
         */
        /*QueryWrapper<ExamUserQuestion> examUserQuestionqw = new QueryWrapper<>();
        examUserQuestionqw.eq("EXAM_USER_ID",examUserQuestion.getExamUserId());
        List<ExamUserQuestion> examUserQuestions = examUserQuestionMapper.selectList(examUserQuestionqw);
        List<ExamUserQuestionVo> questionVoList = new ArrayList<>();
        for (ExamUserQuestion userQuestion : examUserQuestions) {
            ExamUserQuestionVo examUserQuestionVo = new ExamUserQuestionVo();
            BeanUtils.copyProperties(userQuestion,examUserQuestionVo);
            Question question = questionMapper.selectById(userQuestion.getQuestionId());
            examUserQuestionVo.setTitle(question.getTitle());
            examUserQuestionVo.setOptionA(question.getOptionA());
            examUserQuestionVo.setOptionB(question.getOptionB());
            examUserQuestionVo.setOptionC(question.getOptionC());
            examUserQuestionVo.setOptionD(question.getOptionD());
            examUserQuestionVo.setOptionE(question.getOptionE());
            examUserQuestionVo.setOptionF(question.getOptionF());
            examUserQuestionVo.setOptionG(question.getOptionG());
            examUserQuestionVo.setAnalysis(question.getAnalysis());
            questionVoList.add(examUserQuestionVo);
        }*/
        List<ExamUserQuestionVo> examUserQuestionList = examUserQuestionMapper.getExamQuestionList(examUserQuestion.getExamUserId());
        return new ResponseVo(true,"success",examUserQuestionList);
    }

    /**
     * 内部方法，所有题型组卷
     * @param examUser
     * @param exam
     */
    private void allQuestionConfig(ExamUser examUser, Exam exam) {
        // 1)根据考试查询试卷id，获取试卷策略
        PaperConfig paperConfig = paperService.getPaperConfig(exam.getPaperId());
        // 2)然后再题库进行抽题，保存到考试用户试题exam_user_question表中

        // 单选组卷
        int radioCount = paperConfig.getRadioCount();
        if(radioCount>0){
            examUserQuestionConfig(examUser, exam, radioCount, paperConfig.getRadioScore(), 2);
        }
        // 多选组卷
        int checkedCount = paperConfig.getCheckedCount();
        if(checkedCount>0){
            examUserQuestionConfig(examUser, exam, checkedCount, paperConfig.getCheckedScore(), 3);
        }
        // 判断组卷
        int judgeCount = paperConfig.getJudgeCount();
        if(judgeCount>0){
            examUserQuestionConfig(examUser, exam, judgeCount, paperConfig.getJudgeScore(), 4);
        }
        // 填空组卷
        int scannerCount = paperConfig.getScannerCount();
        if(scannerCount>0){
            examUserQuestionConfig(examUser, exam, scannerCount, paperConfig.getScannerScore(), 5);
        }
        // 问答组卷
        int qAnswerCount = paperConfig.getQAnswerCount();
        if(qAnswerCount>0){
            examUserQuestionConfig(examUser, exam, qAnswerCount, paperConfig.getQAnswerScore(), 6);
        }
}

    /**
     * 内部方法，单个题型组卷
     * @param examUser
     * @param exam
     * @param singleCount  单项数量
     * @param singleScore 单项分数
     */
    private void examUserQuestionConfig(ExamUser examUser, Exam exam, int singleCount, double singleScore, int questionTypeId) {
        /**
         * a.获取当前题型(type、catalog)的所有id存放进list集合中
         * b.获取试卷策略这种的题型数量
         * c.然后对list集合进行多次乱序组合
         * d.最后获取试卷策略数量的的试题id集合
         * f.插入用户考试试题
         */
        QueryWrapper<Question> questionQueryWrapper = new QueryWrapper<>();
        questionQueryWrapper.eq("question_type_id",questionTypeId);
        questionQueryWrapper.eq("catalog_id",exam.getCatalogId());
        List<Question> questionList = questionMapper.selectList(questionQueryWrapper);
        Collections.shuffle(questionList);
        Collections.shuffle(questionList);
        List<Question> questions = questionList.subList(0, singleCount);
        ExamUserQuestion examUserQuestion = null;
        for (Question question : questions) {
            examUserQuestion = new ExamUserQuestion();
            // 考试用户ID
            examUserQuestion.setExamUserId(examUser.getId());
            // 考试ID
            examUserQuestion.setExamId(examUser.getExamId());
            // 用户ID
            examUserQuestion.setUserId(examUser.getUserId());
            // 试题ID
            examUserQuestion.setQuestionId(question.getId());
            // 试题类型
            examUserQuestion.setQuestionType(question.getQuestionTypeId());
            // 单项分数
            examUserQuestion.setSingleScore(singleScore);

            // 保存考试用户试题
            examUserQuestionMapper.insert(examUserQuestion);
        }
    }
}
