package com.wzyy.springbootinit.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.Hutool;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.extra.mail.MailUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wzyy.springbootinit.common.ErrorCode;
import com.wzyy.springbootinit.constant.CommonConstant;
import com.wzyy.springbootinit.constant.UserConstant;
import com.wzyy.springbootinit.exception.BusinessException;
import com.wzyy.springbootinit.exception.ThrowUtils;
import com.wzyy.springbootinit.mapper.LoginMessageMapper;
import com.wzyy.springbootinit.mapper.UserMapper;
import com.wzyy.springbootinit.model.dto.user.UserQueryRequest;
import com.wzyy.springbootinit.model.entity.LoginMessage;
import com.wzyy.springbootinit.model.entity.User;
import com.wzyy.springbootinit.model.enums.EmailTypeEnum;
import com.wzyy.springbootinit.model.enums.UserRoleEnum;
import com.wzyy.springbootinit.model.vo.LoginUserVO;
import com.wzyy.springbootinit.model.vo.UserVO;
import com.wzyy.springbootinit.service.UserService;
import com.wzyy.springbootinit.utils.CommonUtil;
import com.wzyy.springbootinit.utils.EmailUtils;
import com.wzyy.springbootinit.utils.MinioUtil;
import com.wzyy.springbootinit.utils.SqlUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.jws.soap.SOAPBinding;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Email;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

/**
 * 用户服务实现
 *
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService{

    @Resource
    LoginMessageMapper loginMessageMapper;

    /**
     * 盐值，混淆密码
     */
    private static final String SALT = "wzyyy";
    private static final String CodeNumePath="codenum:";

    @Autowired
    EmailUtils emailUtils;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @SneakyThrows
    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword, String userEmail, String captcha) {
        // 1. 校验
        ThrowUtils.throwIf(StringUtils.isAnyBlank(userAccount, userPassword, checkPassword),ErrorCode.PARAMS_ERROR, "参数为空");
        //如果有邮箱验证邮箱验证码对不对
        if(userEmail!=null&&!userEmail.isEmpty()){
            ThrowUtils.throwIf(captcha.isEmpty(),ErrorCode.SYSTEM_ERROR,"参数为空");
            ThrowUtils.throwIf(!userEmail.contains("@"),ErrorCode.PARAMS_ERROR,"用户邮箱需要包含@");
            String  trueCaptcha = stringRedisTemplate.opsForValue().get(CodeNumePath + userEmail);
            if(trueCaptcha==null){
                throw new BusinessException(ErrorCode.SYSTEM_ERROR,"验证码错误！");
            }
            ThrowUtils.throwIf(!captcha.equals(trueCaptcha),ErrorCode.PARAMS_ERROR,"邮箱验证码错误！");
        }
        ThrowUtils.throwIf(userAccount.contains("@"),ErrorCode.PARAMS_ERROR,"用户账户禁止包含@");
        ThrowUtils.throwIf(userAccount.length() < 4,ErrorCode.PARAMS_ERROR,"用户账号过短");
        ThrowUtils.throwIf(userPassword.length() < 8 || checkPassword.length() < 8,ErrorCode.PARAMS_ERROR, "用户密码过短");
        // 密码和校验密码相同
        ThrowUtils.throwIf(!userPassword.equals(checkPassword),ErrorCode.PARAMS_ERROR, "两次输入的密码不一致");
        synchronized (userAccount.intern()) {
            // 账户不能重复
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_account", userAccount);
            long count = this.baseMapper.selectCount(queryWrapper);
            if (count > 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号重复");
            }
            // 2. 加密
            String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
            // 3. 插入数据
            User user = new User();
            user.setUserAccount(userAccount);
            user.setUserPassword(encryptPassword);
            user.setUserName("SPH_"+ RandomUtil.randomString(15));
            user.setUserAvatar(MinioUtil.getPreSignedObjectUrl("default","defaultAvatar.png"));
            boolean saveResult = this.save(user);
            ThrowUtils.throwIf(!saveResult,ErrorCode.SYSTEM_ERROR, "注册失败，数据库错误");
            return user.getId();
        }
    }

    @Override
    public LoginUserVO userLoginAccount(String userAccount, String userPassword, HttpServletRequest request) {
        // 1. 校验
        ThrowUtils.throwIf(StringUtils.isAnyBlank(userAccount, userPassword),new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空"));
        ThrowUtils.throwIf(userAccount.length() < 4,new BusinessException(ErrorCode.PARAMS_ERROR, "账号错误"));
        ThrowUtils.throwIf(userPassword.length() < 8,new BusinessException(ErrorCode.PARAMS_ERROR, "密码错误"));
        // 2. 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        // 查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account", userAccount).or().eq("user_Email", userAccount);
        queryWrapper.eq("user_password", encryptPassword);
        User user = this.baseMapper.selectOne(queryWrapper);
        // 用户不存在
        ThrowUtils.throwIf(user == null,new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在或密码错误"));
        String ip = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        log.info("ip is {}",ip);
        log.info("userAgent is {}",userAgent);
        LoginMessage loginMessage = new LoginMessage();
        loginMessage.setUserId(user.getId());
        loginMessage.setLoginTime(new Date());
        loginMessage.setLoginDeviceName("手机名称");
        loginMessage.setLoginDeviceSystem("系统");
        loginMessage.setLoginWay("账号密码");
        loginMessageMapper.insert(loginMessage);
        // 3. 记录用户的登录态
        StpUtil.login(user.getId());
        return this.getLoginUserVO(user);
    }

    @Override
    public Boolean getCODE(String email, String encode) throws MessagingException {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_email",email);
        User user = this.baseMapper.selectOne(queryWrapper);
        ThrowUtils.throwIf(user==null&&encode.equals(EmailTypeEnum.findEmail.getEncode()),ErrorCode.NOT_FOUND_ERROR);
        ThrowUtils.throwIf(user!=null&&encode.equals(EmailTypeEnum.Register.getEncode()),ErrorCode.NOT_FOUND_ERROR);
        String message = getCodeNum(email,encode);
        String send = MailUtil.send(email, "欢迎使用该系统!", message, true);
        if(send!=null){
            return true;
        }
        Boolean b = emailUtils.contextLoads(email, Objects.requireNonNull(EmailTypeEnum.getEnumByValue(encode)).getValue());
        return b;
    }


    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    @Override
    public User getLoginUser(HttpServletRequest request) {
        long userId = StpUtil.getLoginIdAsLong();
        return this.getById(userId);
    }

    /**
     * 获取当前登录用户（允许未登录）
     *
     * @param request
     * @return
     */
    @Override
    public User getLoginUserPermitNull(HttpServletRequest request) {
        // 先判断是否已登录
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            return null;
        }
        // 从数据库查询（追求性能的话可以注释，直接走缓存）
        long userId = currentUser.getId();
        return this.getById(userId);
    }

    /**
     * 是否为管理员
     *
     * @param request
     * @return
     */
    @Override
    public boolean isAdmin(HttpServletRequest request) {
        // 仅管理员可查询
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        User user = (User) userObj;
        return isAdmin(user);
    }

    @Override
    public boolean isAdmin(User user) {
        return user != null && UserRoleEnum.ADMIN.getValue().equals(user.getUserRole());
    }

    /**
     * 用户注销
     *
     */
    @Override
    public boolean userLogout() {
        StpUtil.logout();
        return true;
    }

    @Override
    public LoginUserVO getLoginUserVO(User user) {
        if (user == null) {
            return null;
        }
        LoginUserVO loginUserVO = new LoginUserVO();
        BeanUtils.copyProperties(user, loginUserVO);
        return loginUserVO;
    }

    @Override
    public UserVO getUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    @Override
    public List<UserVO> getUserVO(List<User> userList) {
        if (CollectionUtils.isEmpty(userList)) {
            return new ArrayList<>();
        }
        return userList.stream().map(this::getUserVO).collect(Collectors.toList());
    }

    @Override
    public QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest) {
        if (userQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        Long id = userQueryRequest.getId();
        String userName = userQueryRequest.getUserName();
        String userProfile = userQueryRequest.getUserProfile();
        String userRole = userQueryRequest.getUserRole();
        String sortField = userQueryRequest.getSortField();
        String sortOrder = userQueryRequest.getSortOrder();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(id != null, "id", id);
        queryWrapper.eq(StringUtils.isNotBlank(userRole), "user_role", userRole);
        queryWrapper.like(StringUtils.isNotBlank(userProfile), "user_profile", userProfile);
        queryWrapper.like(StringUtils.isNotBlank(userName), "user_name", userName);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    /**
     * 获取发送验证码的内容
     * @param email
     * @param Type
     * @return
     */
    public String getCodeNum(String email,String Type) {
        CommonUtil commonUtil = new CommonUtil();
        String message = commonUtil.getProfileValue(Objects.requireNonNull(EmailTypeEnum.getEnumByValue(Type)).getValue());
        String codeNum = RandomUtil.randomString(6);
        System.out.println(codeNum);
        message = message.replace("#{codeNum}",codeNum);
        stringRedisTemplate.opsForValue().set(CodeNumePath+email,codeNum,60, TimeUnit.SECONDS);
        return message;
    }
}
