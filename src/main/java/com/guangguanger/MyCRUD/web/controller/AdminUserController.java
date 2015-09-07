package com.guangguanger.MyCRUD.web.controller;

import com.guangguanger.MyCRUD.core.util.CookieUtils;
import com.guangguanger.MyCRUD.web.dao.UserMapper;
import com.guangguanger.MyCRUD.web.model.User;
import com.guangguanger.MyCRUD.web.model.UserExample;
import com.guangguanger.MyCRUD.web.security.PermissionSign;
import com.guangguanger.MyCRUD.web.security.RoleSign;
import com.guangguanger.MyCRUD.web.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户控制器
 * 
 * @author Teng
 * @since 2015年8月26日 下午14:09:00
 **/
@Controller
@RequestMapping(value = "/admin")
public class AdminUserController {

    // (teng：访问数据库：（1）定义接口userService，byName自动注入userServiceimpl实现接口的类
    @Resource
    private UserService userService;

    @Resource
    private UserMapper userMapper;

    /**
     * 用户登录
     * 
     * @param user
     * @param result
     * @param model
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@Valid User user, BindingResult result, Model model, HttpServletRequest request,HttpServletResponse response) {
        try {
            Subject subject = SecurityUtils.getSubject();

            if (result.hasErrors()) {
                model.addAttribute("error", "参数错误！");
                return "adminlogin";
            }

            // 身份验证：
            // 登录成功后跳转到shiro配置（applicationContext-shiro.xml）的successUrl中
            // <property name="successUrl" value="/rest/index"/>
            subject.login(new UsernamePasswordToken(user.getUsername(), user.getPassword()));

            // 验证成功：在Session中保存用户信息
            // (teng：访问数据库：（2）访问实现接口类的方法selectByUsername
            final User authUserInfo = userService.selectByUsername(user.getUsername());
            request.getSession().setAttribute("userInfo", authUserInfo);

            // 记住我：如果“记住我”选中，将选项、用户名和密码分别存入cookie，用于再次登录时免于输入
            String isRember=request.getParameter("remember");     //“记住我”选项

            //
            if (isRember!=null && isRember.equals("1")==true)
            {
                Map<String,String> map=new HashMap<String,String>();
                map.put("rmbUser","true");
                map.put("userName",user.getUsername());
                map.put("passWord",user.getPassword());
                CookieUtils.addCookies(response,map,"/",60*60*24*7);
            }
            else
            {
                Map<String,String> map=new HashMap<String,String>();
                map.put("rmbUser","false");
                map.put("userName","");
                map.put("passWord","");
                CookieUtils.addCookies(response,map,"/",60*60*24*7);
            }

            // （teng 权限跳转：（1）根据用户不同权限，跳转到不同页面，首次尝试登录
            if (subject.hasRole(RoleSign.ADMIN))
            {
                return "redirect:/rest/page/adminindex";
            }
            else
            {
                subject.logout();
                model.addAttribute("error", "没有管理员权限！");
                return "adminlogin";
            }
        }
        catch (AuthenticationException e) {
            // 验证失败：删除cookies
            Map<String,String> map=new HashMap<String,String>();
            map.put("userName","");
            map.put("passWord","");
            CookieUtils.addCookies(response,map,"/",60*60*24*7);

            // 在登录页面显示：${error}
            model.addAttribute("error", "用户名或密码错误！");

            // 返回登录页
            return "adminlogin";

        }

    }

    /**
     * 后台检查前端字段的合法性，合法返回true，非法返回false
     */
    @RequestMapping(value = "/checkuser", method = RequestMethod.POST)
    @ResponseBody
    public Boolean checkUser(HttpServletRequest request,HttpServletResponse response) {
        String username = request.getParameter("username");

        UserExample example = new UserExample();
        example.createCriteria().andUsernameEqualTo(username);    // 设置where条件：username=...
        final List<User> list = userMapper.selectByExample(example);

        if (list.size() == 0) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 用户登出
     * 
     * @param session
     * @return
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpSession session) {
        session.removeAttribute("userInfo");

        Subject subject = SecurityUtils.getSubject();

        subject.logout();
        return "adminlogin";

    }

    /**
     *   （teng 不同登录页面：（1）Shiro下不同登录页面，浏览器中直接输入地址可访问
     *
     */
    @RequestMapping("/adminlogin")
    public String login() {

        return "adminlogin";
    }

    /**
     * 基于角色 标识的权限控制案例
     */
    @RequestMapping(value = "/admin")
    @ResponseBody
    @RequiresRoles(value = RoleSign.ADMIN)
    public String admin() {
        return "拥有admin角色,能访问";
    }

    /**
     * 基于权限标识的权限控制案例
     */
    @RequestMapping(value = "/create")
    @ResponseBody
    @RequiresPermissions(value = PermissionSign.USER_CREATE)
    public String create() {
        return "拥有user:create权限,能访问";
    }
}
