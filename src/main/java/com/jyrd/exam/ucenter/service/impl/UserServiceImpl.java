package com.jyrd.exam.ucenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jyrd.exam.base.common.utils.DateUtil;
import com.jyrd.exam.base.common.utils.RandomUtils;
import com.jyrd.exam.base.common.utils.SMSUtils;
import com.jyrd.exam.base.entity.Exam;
import com.jyrd.exam.base.entity.ExamUser;
import com.jyrd.exam.base.entity.User;
import com.jyrd.exam.base.mapper.ExamMapper;
import com.jyrd.exam.base.mapper.ExamUserMapper;
import com.jyrd.exam.base.mapper.UserMapper;
import com.jyrd.exam.base.vo.MyExamVo;
import com.jyrd.exam.base.vo.ResponseVo;
import com.jyrd.exam.base.vo.SmsPropertiesVo;
import com.jyrd.exam.ucenter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService {
    public static final String REDIS_CODE = "ucenter:verfyCode:";

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ExamUserMapper examUserMapper;

    @Autowired
    private ExamMapper examMapper;

    @Autowired
    private SmsPropertiesVo propertiesVo;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public User getUser(String loginName) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("login_name",loginName);
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public boolean isRegist(User user) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("login_name",user.getLoginName());
        User select1 = userMapper.selectOne(userQueryWrapper);

        userQueryWrapper.clear();
        userQueryWrapper.eq("real_name",user.getRealName());
        User select2 = userMapper.selectOne(userQueryWrapper);

        userQueryWrapper.clear();
        userQueryWrapper.eq("work_num",user.getWorkNum());
        User select3 = userMapper.selectOne(userQueryWrapper);
        return select1 == null && select2 == null && select3 == null;
    }

    /**
     * 登录
     * @param user
     * @return
     */
    @Override
    public Integer login(User user) {
        // 直接不用校验了。。。。。。
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("login_name",user.getLoginName());
        queryWrapper.eq("password",user.getPassword());
        User selectOne = userMapper.selectOne(queryWrapper);
        if(selectOne != null){
            return selectOne.getId();
        }
        return null;
    }

    @Override
    public List<MyExamVo> getMyExam(ExamUser examUser) {
        QueryWrapper<ExamUser> examUserQueryWrapper = new QueryWrapper<>();
        examUserQueryWrapper.eq("USER_ID",examUser.getUserId());
        List<ExamUser> examUserList = examUserMapper.selectList(examUserQueryWrapper);
        List<MyExamVo> myExamList = new ArrayList<>();
        for (ExamUser item : examUserList) {
            MyExamVo myExamVo = new MyExamVo();
            myExamVo.setId(item.getExamId());
            Exam exam = examMapper.selectById(item.getExamId());
            myExamVo.setExamName(exam.getName());
            myExamVo.setMaxScore(item.getMaxScore());
            myExamVo.setTotalDuration(DateUtil.getMulTime(item.getBeginTime(),item.getUpdateTime()));
            myExamList.add(myExamVo);
        }
        return myExamList;
    }

    @Override
    public boolean isBindPhone(String phoneNum) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("phone_num",phoneNum);
        User user = userMapper.selectOne(userQueryWrapper);
        return !(user == null);
    }

    @Override
    public ResponseVo sendLoginSMS(String phoneNum) {
        propertiesVo.setMobileNum(phoneNum);
        String fourBitRandom = RandomUtils.getFourBitRandom();
        propertiesVo.setVerfyCode(fourBitRandom);

        Map<String, String> map = SMSUtils.sendSmS(propertiesVo);
        /*Map<String,String> map = new HashMap<>();
        map.put("code","0000");
        map.put("msg","发送成功");*/

        if("0000".equals(map.get("code"))){
            // 短信发送成功，保存验证码到redis
            redisTemplate.opsForValue().set(REDIS_CODE+phoneNum,propertiesVo.getVerfyCode(),propertiesVo.getTimeOut(), TimeUnit.MINUTES);
            return new ResponseVo(true,"短信发送成功");
        }else {
            log.error("短信发送失败，失败信息："+map.get("code")+map.get("msg"));
            return new ResponseVo(false,"短信发送失败，请稍后重试");
        }
    }

    @Override
    public User mobileLogin(User user) {
        String code = (String) redisTemplate.opsForValue().get(REDIS_CODE + user.getLoginName());
        boolean login = code != null && code.equals(user.getPassword());
        if(login){
            // 如果登录成功，删除redis中的验证码
            redisTemplate.delete(REDIS_CODE + user.getPhoneNum());
            // 查询user信息
            QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
            userQueryWrapper.eq("phone_num",user.getLoginName());
            User selectOne = userMapper.selectOne(userQueryWrapper);
            return selectOne;
        }
        return null;
    }
}
