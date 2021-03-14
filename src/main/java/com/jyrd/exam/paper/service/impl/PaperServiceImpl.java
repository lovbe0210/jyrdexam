package com.jyrd.exam.paper.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jyrd.exam.base.entity.*;
import com.jyrd.exam.base.mapper.*;
import com.jyrd.exam.base.vo.PaperQuestionVo;
import com.jyrd.exam.base.vo.ParamVo;
import com.jyrd.exam.base.vo.QuestionTypeVo;
import com.jyrd.exam.base.vo.ResponsePageVo;
import com.jyrd.exam.paper.service.PaperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PaperServiceImpl extends ServiceImpl<PaperMapper, Paper> implements PaperService {
    @Autowired
    private PaperTypeMapper paperTypeMapper;
    @Autowired
    private PaperMapper paperMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private PaperConfigMapper paperConfigMapper;
    @Autowired
    private SysCatalogMapper catalogMapper;

    @Override
    public List<Map<String, Object>> getPaperTypeTreeList() {
        QueryWrapper<PaperCatalog> paperTypeQueryWrapper = new QueryWrapper<>();
        List<PaperCatalog> paperCatalogList = paperTypeMapper.selectList(paperTypeQueryWrapper);
        List<Map<String, Object>> paperTypeTreeList = new ArrayList<Map<String,Object>>();

        for(PaperCatalog paperCatalog : paperCatalogList){
            Map<String, Object> map = new HashMap<>();
            map.put("ID", paperCatalog.getId());
            map.put("NAME", paperCatalog.getName());
            map.put("PARENT_ID", paperCatalog.getParentId());
            //map.put("DISABLED", true);
            //map.put("EXPANDED", true);
            paperTypeTreeList.add(map);
        }
        return paperTypeTreeList;
    }

    @Override
    public ResponsePageVo getListpage(ParamVo paramVo) {
        QueryWrapper<Paper> paperQueryWrapper = new QueryWrapper<>();
        String six = paramVo.getSix();  // 分类
        String two = paramVo.getTwo();  // 名称
        String four = paramVo.getFour();// 状态

        if(!StringUtils.isEmpty(six)){
            paperQueryWrapper.like("catalog_id",six);
        }
        if(!StringUtils.isEmpty(two)){
            paperQueryWrapper.like("NAME",two);
        }
        if (!StringUtils.isEmpty(four)){
            paperQueryWrapper.like("STATE",four);
        }

        // 查询总记录数
        Integer count = paperMapper.selectCount(paperQueryWrapper);

        // 查询列表
        paperQueryWrapper.orderByAsc("catalog_id");
        Page<Paper> page = new Page<>(paramVo.getCurrentPage(), paramVo.getPageSize());
        page.setTotal(count);
        Page<Paper> paperPage = paperMapper.selectPage(page, paperQueryWrapper);
        List<Map<String, Object>> mapList = new ArrayList<>();
        List<Paper> records = paperPage.getRecords();
        for (Paper record : records) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("ID",record.getId());
            map.put("NAME",record.getName());
            map.put("TOTLE_SCORE",record.getTotleScore());
            SysCatalog sysCatalog = catalogMapper.selectById(record.getCatalogId());
            map.put("PAPER_TYPE_NAME",sysCatalog.getCatalogName()+"试卷");

            switch (record.getState()){
                case 0:
                    map.put("STATE_NAME","尚未配置无法发布");
                    break;
                case 1:
                    map.put("STATE_NAME","已配置");
                    break;
            }

            mapList.add(map);
        }

        // 封装查询列表和总记录数，并返回
        return new ResponsePageVo(mapList, count);
    }

    @Override
    public PaperQuestionVo getPaperQuestion(Integer catalogId) {
        PaperQuestionVo paperQuestionVo = new PaperQuestionVo();
        paperQuestionVo.setPaperTypeId(catalogId);
        QueryWrapper<Question> questionQueryWrapper = new QueryWrapper<>();

        questionQueryWrapper.eq("question_type_id",2);
        questionQueryWrapper.eq("catalog_id",catalogId);
        Integer radioCount = questionMapper.selectCount(questionQueryWrapper);
        paperQuestionVo.setRadioCount(radioCount);

        questionQueryWrapper.clear();
        questionQueryWrapper.eq("question_type_id",3);
        questionQueryWrapper.eq("catalog_id",catalogId);
        Integer checkboxCount = questionMapper.selectCount(questionQueryWrapper);
        paperQuestionVo.setCheckboxCount(checkboxCount);

        questionQueryWrapper.clear();
        questionQueryWrapper.eq("question_type_id",4);
        questionQueryWrapper.eq("catalog_id",catalogId);
        Integer judgeCount = questionMapper.selectCount(questionQueryWrapper);
        paperQuestionVo.setJudgeCount(judgeCount);

        questionQueryWrapper.clear();
        questionQueryWrapper.eq("question_type_id",5);
        questionQueryWrapper.eq("catalog_id",catalogId);
        Integer scannerCount = questionMapper.selectCount(questionQueryWrapper);
        paperQuestionVo.setScannerCount(scannerCount);

        questionQueryWrapper.clear();
        questionQueryWrapper.eq("question_type_id",6);
        questionQueryWrapper.eq("catalog_id",catalogId);
        Integer qAnswerCount = questionMapper.selectCount(questionQueryWrapper);
        paperQuestionVo.setQAnswerCount(qAnswerCount);

        return paperQuestionVo;
    }

    @Override
    public boolean addQuestionType(QuestionTypeVo questionTypeVo) {
        QueryWrapper<PaperConfig> paperConfigQueryWrapper = new QueryWrapper<>();
        paperConfigQueryWrapper.eq("PAPER_ID",questionTypeVo.getPaperId());
        PaperConfig paperConfig = paperConfigMapper.selectOne(paperConfigQueryWrapper);
        int questionType = questionTypeVo.getQuestionType();
        if(paperConfig == null){
            // 第一次添加
            paperConfig = new PaperConfig();
            paperConfig.setPaperId(questionTypeVo.getPaperId());
            switch (questionType){
                case 2:
                    paperConfig.setRadioCount(questionTypeVo.getQuestionCount());
                    paperConfig.setRadioScore(questionTypeVo.getQuestionScore());
                    break;
                case 3:
                    paperConfig.setCheckedCount(questionTypeVo.getQuestionCount());
                    paperConfig.setCheckedScore(questionTypeVo.getQuestionScore());
                    break;
                case 4:
                    paperConfig.setJudgeCount(questionTypeVo.getQuestionCount());
                    paperConfig.setJudgeScore(questionTypeVo.getQuestionScore());
                    break;
                case 5:
                    paperConfig.setScannerCount(questionTypeVo.getQuestionCount());
                    paperConfig.setScannerScore(questionTypeVo.getQuestionScore());
                    break;
                case 6:
                    paperConfig.setQAnswerCount(questionTypeVo.getQuestionCount());
                    paperConfig.setQAnswerScore(questionTypeVo.getQuestionScore());
                    break;
            }
            // 更新总分数
            paperConfig.submitTotalScore();
            paperConfigMapper.insert(paperConfig);
        }else {
            // 非首次添加
            // 1. 判断所添加的实体类型是否已经添加
            // 2. 如果已经添加则返回false，提示已经添加，可以删除后再添加
            // 3. 如果没有添加则在添加
            switch (questionType){
                case 2:
                    if(paperConfig.getRadioCount() > 0){
                        return false;
                    }else {
                        paperConfig.setRadioCount(questionTypeVo.getQuestionCount());
                        paperConfig.setRadioScore(questionTypeVo.getQuestionScore());
                    }
                    break;
                case 3:
                    if(paperConfig.getCheckedCount() > 0){
                        return false;
                    }else {
                        paperConfig.setCheckedCount(questionTypeVo.getQuestionCount());
                        paperConfig.setCheckedScore(questionTypeVo.getQuestionScore());
                    }
                    break;
                case 4:
                    if(paperConfig.getJudgeCount() > 0){
                        return false;
                    }else {
                        paperConfig.setJudgeCount(questionTypeVo.getQuestionCount());
                        paperConfig.setJudgeScore(questionTypeVo.getQuestionScore());
                    }
                    break;
                case 5:
                    if(paperConfig.getScannerCount() > 0){
                        return false;
                    }else {
                        paperConfig.setScannerCount(questionTypeVo.getQuestionCount());
                        paperConfig.setScannerScore(questionTypeVo.getQuestionScore());
                    }
                    break;
                case 6:
                    if(paperConfig.getQAnswerCount() > 0){
                        return false;
                    }else {
                        paperConfig.setQAnswerCount(questionTypeVo.getQuestionCount());
                        paperConfig.setQAnswerScore(questionTypeVo.getQuestionScore());
                    }
                    break;
            }
            // 更新总分数
            paperConfig.submitTotalScore();
            paperConfigMapper.updateById(paperConfig);
        }

        // 更新试卷状态为已配置可发布
        Paper paper = new Paper();
        paper.setState(1);
        paper.setId(questionTypeVo.getPaperId());

        // 更新总分数
        paper.setTotleScore(BigDecimal.valueOf(paperConfig.getTotleScore()));
        paperMapper.updateById(paper);

        return true;
    }

    @Override
    public PaperConfig getPaperConfig(int paperId) {
        QueryWrapper<PaperConfig> paperConfigQueryWrapper = new QueryWrapper<>();
        paperConfigQueryWrapper.eq("PAPER_ID",paperId);
        return paperConfigMapper.selectOne(paperConfigQueryWrapper);
    }

    @Override
    public List<SysCatalog> getCatalogList() {
        return catalogMapper.selectList(null);
    }
}
