package com.guangguanger.MyCRUD.web.controller;

import javax.annotation.Resource;

import com.guangguanger.MyCRUD.core.feature.orm.mybatis.Page;
import com.guangguanger.MyCRUD.web.dao.UserMapper;
import com.guangguanger.MyCRUD.web.model.User;
import com.guangguanger.MyCRUD.web.model.UserExample;
import com.guangguanger.MyCRUD.web.security.RoleSign;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 视图控制器,返回jsp视图给前端
 * 
 * @author Teng
 * @since 2015年8月12日 22:06
 **/
@Controller
@RequestMapping("/page")
public class PageController {

    @Resource
    UserMapper userMapper;

    /**
     * 管理员页面只有具有管理权限的用户才能访问
     */
    @RequestMapping("/adminindex")
    @RequiresRoles(value = RoleSign.ADMIN)
    public String adminindex() {
        return "adminindex";
    }

    /**
     * 登录页
     */
    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    /**
     * dashboard页
     */
    @RequestMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }

    /**
     * adduser页
     */
    @RequestMapping("/adduser")
    public String adduserrequestmapping() {
        return "adduser";
    }

    /**
     * edituser页
     */
    @RequestMapping("/edituser")
    public String edituserrequestmapping() {
        return "edituser";
    }

    /**
     * manageuser页面
     */
    @RequestMapping("/manageuser")
    public String manageuserrequestmapping() {
        return "manageuser";
    }

    // (3) 控制器决定页面转向至datatable.jsp页面
    @RequestMapping(value = "/adduserpost", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> addUserPost(@RequestBody User user) {
        userMapper.insertSelective(user);
        Map<String, Object> map = new HashMap<String, Object>(1);
        map.put("success", "true");

        return map;
    }

    @RequestMapping(value = "/edituserpost", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> editUserPost(@RequestBody User user) {
        userMapper.updateByPrimaryKey(user);
        Map<String, Object> map = new HashMap<String, Object>(1);
        map.put("success", "true");

        return map;
    }

    @RequestMapping(value = "/deleteuserpost", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> deleteUserPost(@RequestBody User user) {
        Long id=user.getId();

        userMapper.deleteByPrimaryKey(id);
        Map<String, Object> map = new HashMap<String, Object>(1);
        map.put("success", "true");

        return map;
    }

//    后台生成数据，供前端ajax读取，前端见adduser.js
//    @RequestMapping(value = "/userinfo_post_data", method = RequestMethod.POST)
//    @ResponseBody
//    //public Map<String, Object> addUser_post_data(@RequestBody List<User> users) {
//    public Map<String, Object> addUser_post_data(@RequestBody User user) {
//        List<User> list = new ArrayList<User>();
//
//        User um = new User();
//        um.setId(1L);
//        um.setUsername("sss");
//        list.add(um);
//
//        user.setId(2L);
//        list.add(user);
//
//        Map<String, Object> map = new HashMap<String, Object>(list.size());
//        map.put("total",list.size());
//        map.put("data",list);
//        map.put("success", "true");
//
//        return map;
//    }

    @RequestMapping(value = "/datatableajax", method = RequestMethod.POST)
    @ResponseBody
    public String datatableAjax(@RequestParam String aoData) {
        JSONArray jsonarray = JSONArray.fromObject(aoData);       // 前端datatables参数

        String sEcho = null;
        int iDisplayStart = 0;                                    // 起始索引
        int iDisplayLength = 0;                                   // 每页显示的行数

        for (int i = 0; i < jsonarray.size(); i++) {
            JSONObject obj = (JSONObject) jsonarray.get(i);

//            // standard jquery datatable
//            if (obj.get("name").equals("sEcho"))
//                sEcho = obj.get("value").toString();
//
//            if (obj.get("name").equals("iDisplayStart"))
//                iDisplayStart = obj.getInt("value");
//
//            if (obj.get("name").equals("iDisplayLength"))
//                iDisplayLength = obj.getInt("value");
//
//            if (obj.get("name").equals("sEcho"))
//                sEcho = obj.get("value").toString();

            //m4.1.0 datatable
            if (obj.get("name").equals("draw"))
                sEcho = obj.get("value").toString();

            if (obj.get("name").equals("start"))
                iDisplayStart = obj.getInt("value");

            if (obj.get("name").equals("length"))
                iDisplayLength = obj.getInt("value");
        }

        UserExample example1 = new UserExample();
        int linenumber=userMapper.countByExample(example1);       // 实际的行数:数据表总共的行数

        //Page<User> page = new Page<>(2, 3); // 2:第2页，每页3个
        int pagenumber=iDisplayStart/iDisplayLength+1;
        Page<User> page = new Page<>(pagenumber, iDisplayLength); // pagenumber:第pagenumber页，每页iDisplayLength个
        UserExample example = new UserExample();
        example.createCriteria().andIdGreaterThan(0L);
        final List<User> users = userMapper.selectByExampleAndPage(page, example);

        //
        List<String[]> lst = new ArrayList<String[]>();
        for (User user : users) {
            String[] d=new String[6];
            d[0]=user.getId().toString();
            d[1]=(user.getUsername()==null)?"":user.getUsername();
            d[2]=(user.getPassword()==null)?"":user.getPassword();
            d[3]=(user.getCreateTime()==null)?"":user.getCreateTime().toString();
            d[4]=d[0];
            d[5]=d[0];

            lst.add(d);
        }

//        // 生成20条测试数据
//        List<String[]> lst = new ArrayList<String[]>();
//        for (int i = 0; i < 50; i++) {
//            String[] d = {Integer.toString(i), "col2_data" + i,"col3_data" + i,"co4_data" + i,"Edit","Delete"};
//            lst.add(d);
//        }

        JSONObject getObj = new JSONObject();
        getObj.put("sEcho", sEcho);
        getObj.put("iTotalRecords", linenumber);                  // 实际的行数:数据表总共的行数
        getObj.put("iTotalDisplayRecords",linenumber);            // 显示的行数,这个要和上面写的一样
        //getObj.put("aaData", lst.subList(iDisplayStart,iDisplayStart + iDisplayLength));//要以JSON格式返回
        getObj.put("aaData", lst);                                // 要以JSON格式返回
        return getObj.toString();
    }

    /**
     * 404页
     */
    @RequestMapping("/404")
    public String error404() {
        return "404";
    }

    /**
     * 401页
     */
    @RequestMapping("/401")
    public String error401() {
        return "401";
    }

    /**
     * 500页
     */
    @RequestMapping("/500")
    public String error500() {
        return "500";
    }

}