package com.jyrd.exam.question.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jyrd.exam.base.entity.Question;
import com.jyrd.exam.base.entity.QuestionType;
import com.jyrd.exam.base.entity.SysCatalog;
import com.jyrd.exam.base.mapper.QuestionMapper;
import com.jyrd.exam.base.mapper.QuestionTypeMapper;
import com.jyrd.exam.base.mapper.SysCatalogMapper;
import com.jyrd.exam.question.service.QuestionService;
import com.jyrd.exam.base.vo.ParamVo;
import com.jyrd.exam.base.vo.ResponsePageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lovbe
 * @since 2021-01-19
 */
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements QuestionService {

    @Autowired
    private QuestionTypeMapper questionTypeMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private SysCatalogMapper CatalogMapper;

    @Override
    public List<Map<String, Object>> getQuestionTypeTreeList() {
        QueryWrapper<QuestionType> typeQueryWrapper = new QueryWrapper<>();
        List<QuestionType> questionTypeList = questionTypeMapper.selectList(typeQueryWrapper);
        List<Map<String, Object>> questionTypeTreeList = new ArrayList<Map<String,Object>>();

        for(QuestionType questionType : questionTypeList){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("ID", questionType.getId());
            map.put("NAME", questionType.getName());
            map.put("PARENT_ID", questionType.getParentId());
            //map.put("DISABLED", true);
            //map.put("EXPANDED", true);
            questionTypeTreeList.add(map);
        }
        return questionTypeTreeList;
    }

    @Override
    public QuestionType getQuestionType(Integer questionTypeId) {
        return questionTypeMapper.selectById(questionTypeId);
    }

    @Override
    public void addAndUpdate(Question question, String[] answer) {
        //添加试题
        if(question.getQuestionTypeId() == 5){//如果是填空，特殊处理一下
            StringBuilder answers = new StringBuilder();
            for(String an : answer){
                if(answers.length() > 0){
                    answers.append("&");
                }
                answers.append(an);
            }
            question.setAnswer(answers.toString());
        }
        question.setUpdateTime(new Date());
        questionMapper.insert(question);
    }

    @Override
    public ResponsePageVo getListpage(ParamVo paramVo) {
        QueryWrapper<Question> questionQueryWrapper = new QueryWrapper<>();
        String three = paramVo.getThree();  // 题干
        String five = paramVo.getFive();    // 类型  单选多选
        String six = paramVo.getSix();      // 分类  安规运规

        if(!StringUtils.isEmpty(three)){
            questionQueryWrapper.like("TITLE",three);
        }

        if(!StringUtils.isEmpty(five)){
            questionQueryWrapper.eq("question_type_id",five);
        }

        if(!StringUtils.isEmpty(six)){
            questionQueryWrapper.eq("catalog_id",six);
        }

        // 查询总记录数
        Integer count = questionMapper.selectCount(questionQueryWrapper);

        // 查询列表
        questionQueryWrapper.orderByAsc("question_type_id");
        Page<Question> page = new Page<>(paramVo.getCurrentPage(), paramVo.getPageSize());
        page.setTotal(count);
        Page<Question> questionPage = questionMapper.selectPage(page, questionQueryWrapper);
        List<Map<String, Object>> mapList = new ArrayList<>();
        List<Question> records = questionPage.getRecords();
        for (Question record : records) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("ID",record.getId());
            map.put("TITLE",record.getTitle());
            map.put("DIFFICULTY_NAME",record.getDifficulty());

            switch (record.getCatalogId()){
                case 1:
                    map.put("CATALOG_NAME","安规");
                    break;
                case 2:
                    map.put("CATALOG_NAME","运规");
                    break;
                case 3:
                    map.put("CATALOG_NAME","三种人");
                    break;
            }

            switch (record.getQuestionTypeId()){
                case 2:
                    map.put("QUESTION_TYPE_NAME","单选");
                    break;
                case 3:
                    map.put("QUESTION_TYPE_NAME","多选");
                    break;
                case 4:
                    map.put("QUESTION_TYPE_NAME","判断");
                    break;
                case 5:
                    map.put("QUESTION_TYPE_NAME","填空");
                    break;
                case 6:
                    map.put("QUESTION_TYPE_NAME","简答");
                    break;
            }

            mapList.add(map);
        }

        // 封装查询列表和总记录数，并返回
        return new ResponsePageVo(mapList, count);
    }

    @Override
    public List<QuestionType> getQuestionTypeList() {
        List<QuestionType> questionTypes = questionTypeMapper.selectList(null);
        return questionTypes;
    }

    @Override
    public List<SysCatalog> getCatalogList() {
        List<SysCatalog> catalogList = CatalogMapper.selectList(null);
        return catalogList;
    }

}
