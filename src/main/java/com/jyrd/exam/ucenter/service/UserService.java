package com.jyrd.exam.ucenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jyrd.exam.base.entity.ExamUser;
import com.jyrd.exam.base.entity.User;
import com.jyrd.exam.base.vo.MyExamVo;
import com.jyrd.exam.base.vo.ResponseVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lovbe
 * @since 2021-03-19
 */
public interface UserService extends IService<User> {

    User getUser(String loginName);

    boolean isRegist(User user);

    Integer login(User user);

    List<MyExamVo> getMyExam(ExamUser examUser);

    boolean isBindPhone(String phoneNum);

    ResponseVo sendLoginSMS(String phoneNum);

    User mobileLogin(User user);
}
