package com.wzyy.springbootinit.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzyy.springbootinit.common.BaseResponse;
import com.wzyy.springbootinit.common.DeleteRequest;
import com.wzyy.springbootinit.common.ErrorCode;
import com.wzyy.springbootinit.common.ResultUtils;
import com.wzyy.springbootinit.model.dto.loginMessage.LoginMessageQueryRequest;
import com.wzyy.springbootinit.model.dto.user.*;
import com.wzyy.springbootinit.model.entity.User;
import com.wzyy.springbootinit.annotation.AuthCheck;
import com.wzyy.springbootinit.constant.UserConstant;
import com.wzyy.springbootinit.exception.BusinessException;
import com.wzyy.springbootinit.exception.ThrowUtils;
import com.wzyy.springbootinit.model.vo.LoginMessageV0;
import com.wzyy.springbootinit.model.vo.LoginUserVO;
import com.wzyy.springbootinit.model.vo.UserVO;
import com.wzyy.springbootinit.service.LoginMessageService;
import com.wzyy.springbootinit.service.UserService;
import java.util.List;
import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

/**
 * 用户接口
 *
 */
@RestController
@RequestMapping("/user")
@Slf4j
@Api(tags = "用户相关操作")
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private LoginMessageService loginMessageService;


    /**
     * 用户注册
     *
     * @param userRegisterRequest
     * @return
     */
    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String userEmail = userRegisterRequest.getUserEmail();
        String captcha = userRegisterRequest.getCaptcha();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return null;
        }
        long result = userService.userRegister(userAccount, userPassword, checkPassword,userEmail,captcha);
        return ResultUtils.success(result);
    }

    /**
     * 用户账号密码登录
     *
     * @param userLoginRequest
     * @param request
     * @return
     */
    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public BaseResponse<LoginUserVO> userLoginBYAccount(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        ThrowUtils.throwIf(StringUtils.isAnyBlank(userAccount, userPassword),new BusinessException(ErrorCode.PARAMS_ERROR));
        LoginUserVO loginUserVO = userService.userLoginAccount(userAccount, userPassword, request);
        return ResultUtils.success(loginUserVO);
    }

    /**
     * 使用邮箱找回密码
     * @param email 邮箱
     * @param encode 发送邮件的类型
     * @return
     */
    @Operation(summary = "获取验证码")
    @GetMapping("getEmailCode")
    public BaseResponse<Boolean> userLoginByEmailGetCode(String email,@RequestParam(defaultValue = "0") String encode) throws MessagingException {
        ThrowUtils.throwIf(email==null||email.isEmpty(),ErrorCode.PARAMS_ERROR);
        Boolean code = userService.getCODE(email,encode);
        return ResultUtils.success(code);
    }

    @Operation(summary = "通过邮箱修改密码")
    @PostMapping("changePassBYEmail")
    public BaseResponse<Boolean> changPassByEmail(@RequestBody UserChangePassRequest userChangePassRequest,HttpServletRequest request){
        ThrowUtils.throwIf(userChangePassRequest==null,ErrorCode.PARAMS_ERROR);
        return ResultUtils.success(true);
    }


    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    @Operation(summary = "用户退出登录")
    @PostMapping("/logout")
    public BaseResponse<Boolean> userLogout(HttpServletRequest request) {
        boolean result = userService.userLogout();
        return ResultUtils.success(result);
    }

    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    @Operation(summary = "获取当前用户")
    @GetMapping("/get/login")
    public BaseResponse<LoginUserVO> getLoginUser(HttpServletRequest request) {
        User user = userService.getLoginUser(request);
        return ResultUtils.success(userService.getLoginUserVO(user));
    }

    // endregion

    // region 增删改查

    /**
     * 创建用户
     *
     * @param userAddRequest
     * @param request
     * @return
     */
    @Operation(summary = "新增用户")
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addUser(@RequestBody UserAddRequest userAddRequest, HttpServletRequest request) {
        if (userAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = new User();
        BeanUtils.copyProperties(userAddRequest, user);
        boolean result = userService.save(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(user.getId());
    }

    /**
     * 删除用户
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @Operation(summary = "删除用户")
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteUser(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b = userService.removeById(deleteRequest.getId());
        return ResultUtils.success(b);
    }

    /**
     * 更新用户
     *
     * @param userUpdateRequest
     * @param request
     * @return
     */
    @Operation(summary = "更新用户")
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateUser(@RequestBody UserUpdateRequest userUpdateRequest,
            HttpServletRequest request) {
        if (userUpdateRequest == null || userUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = new User();
        BeanUtils.copyProperties(userUpdateRequest, user);
        boolean result = userService.updateById(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取用户（仅管理员）
     *
     * @param id
     * @param request
     * @return
     */
    @Operation(summary = "获取用户")
    @GetMapping("/get")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<User> getUserById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getById(id);
        ThrowUtils.throwIf(user == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(user);
    }

    /**
     * 根据 id 获取包装类
     *
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<UserVO> getUserVOById(long id, HttpServletRequest request) {
        BaseResponse<User> response = getUserById(id, request);
        User user = response.getData();
        return ResultUtils.success(userService.getUserVO(user));
    }

    /**
     * 分页获取用户列表（仅管理员）
     *
     * @param userQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<User>> listUserByPage(@RequestBody UserQueryRequest userQueryRequest,
            HttpServletRequest request) {
        long current = userQueryRequest.getCurrent();
        long size = userQueryRequest.getPageSize();
        Page<User> userPage = userService.page(new Page<>(current, size),
                userService.getQueryWrapper(userQueryRequest));
        return ResultUtils.success(userPage);
    }

    /**
     * 分页获取用户封装列表
     *
     * @param userQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<UserVO>> listUserVOByPage(@RequestBody UserQueryRequest userQueryRequest,
            HttpServletRequest request) {
        if (userQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long current = userQueryRequest.getCurrent();
        long size = userQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<User> userPage = userService.page(new Page<>(current, size),
                userService.getQueryWrapper(userQueryRequest));
        Page<UserVO> userVOPage = new Page<>(current, size, userPage.getTotal());
        List<UserVO> userVO = userService.getUserVO(userPage.getRecords());
        userVOPage.setRecords(userVO);
        return ResultUtils.success(userVOPage);
    }

    // endregion

    /**
     * 更新个人信息
     *
     * @param userUpdateMyRequest
     * @param request
     * @return
     */
    @PostMapping("/update/my")
    public BaseResponse<Boolean> updateMyUser(@RequestBody UserUpdateMyRequest userUpdateMyRequest,
            HttpServletRequest request) {
        if (userUpdateMyRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        User user = new User();
        BeanUtils.copyProperties(userUpdateMyRequest, user);
        user.setId(loginUser.getId());
        boolean result = userService.updateById(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }
    @PostMapping("getLoginMesaage")
    public BaseResponse<List<LoginMessageV0>> getLoginMessage(@RequestBody LoginMessageQueryRequest loginMessageQueryRequest, HttpServletRequest request){
        loginMessageQueryRequest.setUserId(userService.getLoginUser(request).getId());
        List<LoginMessageV0> loginMessageList = loginMessageService.getLoginMessageList(loginMessageQueryRequest);
        return ResultUtils.success(loginMessageList);
    }
}
