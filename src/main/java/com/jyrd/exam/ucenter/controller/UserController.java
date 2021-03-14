package com.jyrd.exam.ucenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jyrd.exam.base.entity.ExamUser;
import com.jyrd.exam.base.entity.User;
import com.jyrd.exam.base.vo.MyExamVo;
import com.jyrd.exam.base.vo.PasswordInfo;
import com.jyrd.exam.base.vo.ResponseVo;
import com.jyrd.exam.ucenter.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

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
@CrossOrigin
@RequestMapping("/ucenter")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 获取用户考试成绩
     * @param examUser
     * @return
     */
    @RequestMapping("/getMyExam")
    @ResponseBody
    public ResponseVo getMyExam(ExamUser examUser){
        try {
            List<MyExamVo> examList = userService.getMyExam(examUser);
            return new ResponseVo(true,"获取用户考试信息成功",examList);
        } catch (Exception e) {
            log.error("获取用户考试信息错误：", e);
            return new ResponseVo(false,"获取用户考试信息失败");
        }
    }

    /**
     * 获取用户信息
     * @param user
     * @return
     */
    @RequestMapping("/getUserById")
    @ResponseBody
    public ResponseVo getUserById(User user){
        try {
            User userInfo = userService.getById(user.getId());
            userInfo.setPassword("");
            return new ResponseVo(true,"获取用户信息成功",userInfo);
        } catch (Exception e) {
            log.error("获取用户信息错误：", e);
            return new ResponseVo(false,"获取用户信息失败");
        }
    }


    /**
     * 注册
     * @param user
     * @return
     */
    @RequestMapping("/registUser")
    @ResponseBody
    public ResponseVo registUser(@RequestBody User user){
        try {
            //  TODO 后端也应该校验
            boolean flag = userService.isRegist(user);
            // true表示后台查询结果为null，该信息可注册，false表示不为null，表示已经注册
            if(flag){
                userService.save(user);
                return new ResponseVo(true,"用户注册成功");
            }else {
                return new ResponseVo(false,"当前用户信息已经被注册");
            }
        } catch (Exception e) {
            log.error("用户注册失败：", e);
            return new ResponseVo(false,"用户注册失败");
        }
    }

    /**
     * 用户名密码登录
     * @param user
     * @return
     */
    @RequestMapping("/login")
    @ResponseBody
    public ResponseVo login(@RequestBody User user, HttpServletRequest request,HttpServletResponse response){
        try {
            Integer userId = userService.login(user);
            if(userId == null){
                // 登录失败
                return new ResponseVo(false,"用户名或密码错误");
            }
            HttpSession session = request.getSession();
            session.setAttribute("userId",userId);
            session.setMaxInactiveInterval(60*60*24*7);

            Cookie jsessionid = new Cookie("JSESSIONID", session.getId());
            jsessionid.setPath("/");
            jsessionid.setMaxAge(60 * 60 * 24 * 7);
            jsessionid.setDomain("localhost");
            response.addCookie(jsessionid);

            // 返回登录状态
            return new ResponseVo(true,"登录成功");
        } catch (Exception e) {
            log.error("用户登录失败：", e);
            return new ResponseVo(false,"用户登录失败");
        }
    }

    /**
     * 手机验证码登录
     * @param user
     * @return
     */
    @RequestMapping("/mobileLogin")
    @ResponseBody
    public ResponseVo mobileLogin(@RequestBody User user,HttpServletRequest request,HttpServletResponse response){
        try {
            User selectOne = userService.mobileLogin(user);
            if(selectOne == null){
                // 登录失败
                return new ResponseVo(false,"验证码错误");
            }

            HttpSession session = request.getSession();
            session.setAttribute("userId",selectOne.getId());
            session.setMaxInactiveInterval(60*60*24*7);

            Cookie jsessionid = new Cookie("JSESSIONID", session.getId());
            jsessionid.setPath("/");
            jsessionid.setMaxAge(60 * 60 * 24 * 7);
            jsessionid.setDomain("localhost");
            response.addCookie(jsessionid);

            // 返回登录状态
            return new ResponseVo(true,"登录成功");
        } catch (Exception e) {
            log.error("用户登录失败：", e);
            return new ResponseVo(false,"用户登录失败");
        }
    }


    /**
     * 判断用户是否登录
     * @return
     */
    @RequestMapping("/isLogin")
    @ResponseBody
    public ResponseVo isLogin(HttpServletRequest request){
        try {
            HttpSession session = request.getSession();
            Integer userId = (Integer) session.getAttribute("userId");
            if(userId == null){
                // 登录失效，重新登录
                return new ResponseVo(false,"登录已失效，请重新登录");
            }else {
                return new ResponseVo(true,"已登录",userId);
            }
        } catch (Exception e) {
            log.error("查询状态失败：", e);
            return new ResponseVo(false,"查询状态失败");
        }
    }

    /**
     * 退出登录
     * @return
     */
    @RequestMapping("/loginOut")
    @ResponseBody
    public ResponseVo loginOut(HttpServletRequest request){
        try {
            HttpSession session = request.getSession();
            session.invalidate();
            return new ResponseVo(true,"退出登录");
        } catch (Exception e) {
            log.error("查询状态失败：", e);
            return new ResponseVo(false,"查询状态失败");
        }
    }

    /**
     * 修改密码
     * @return
     */
    @RequestMapping("/updatePwd")
    @ResponseBody
    public ResponseVo updatePwd(@RequestBody PasswordInfo passwordInfo){
        try {
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("ID",passwordInfo.getUserId());
            queryWrapper.eq("password",passwordInfo.getBeforePwd());
            User user = userService.getOne(queryWrapper);
            if(user == null){
                return new ResponseVo(false,"原密码有误，请重新输入");
            }
            user.setPassword(passwordInfo.getConfirmNowPwd());
            userService.updateById(user);
            return new ResponseVo(true,"密码修改成功");
        } catch (Exception e) {
            log.error("查询状态失败：", e);
            return new ResponseVo(false,"查询状态失败");
        }
    }

    /**
     * 修改个人信息
     * @return
     */
    @RequestMapping("/updateUserInfo")
    @ResponseBody
    public ResponseVo updateUserInfo(@RequestBody User user){
        try {
            user.setPassword(null);
            user.setCreateTime(null);
            user.setLastLoginTime(null);
            user.setLoginName(null);
            user.setUpdateTime(null);
            userService.updateById(user);

            return new ResponseVo(true,"用户个人信息修改成功");
        } catch (Exception e) {
            log.error("个人信息修改失败：", e);
            return new ResponseVo(false,"个人信息修改失败");
        }
    }

    /**
     * 查询是否绑定手机号码
     * @return
     */
    @RequestMapping("/isBindPhone")
    @ResponseBody
    public ResponseVo isBindPhone(String phoneNum){
        try {
            boolean flag = userService.isBindPhone(phoneNum);
            if(flag){
                return new ResponseVo(true,"已绑定手机号码");
            }else {
                return new ResponseVo(false,"当前手机号码暂未绑定账户，请先登录之后再进行绑定手机号码");
            }
        } catch (Exception e) {
            log.error("个人信息修改失败：", e);
            return new ResponseVo(false,"个人信息修改失败");
        }
    }


    /**
     * 发送验证码
     * @return
     */
    @RequestMapping("/sendCode")
    @ResponseBody
    public ResponseVo sendCode(String phoneNum){
        try {
            ResponseVo responseVo = userService.sendLoginSMS(phoneNum);
            return responseVo;
        } catch (Exception e) {
            log.error("短信服务器故障：", e);
            return new ResponseVo(false,"短信发送失败，请联系管理员查看原因");
        }
    }

}
