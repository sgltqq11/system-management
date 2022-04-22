package com.sgl.sm.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sgl.sm.pojo.Admin;
import com.sgl.sm.pojo.LoginForm;
import com.sgl.sm.pojo.Student;
import com.sgl.sm.pojo.Teacher;
import com.sgl.sm.service.AdminService;
import com.sgl.sm.service.StudentService;
import com.sgl.sm.service.TeacherService;
import com.sgl.sm.util.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

//针对一些验证码，登录请求等的控制层

@Api(tags = "系统控制器")
@RestController
@RequestMapping("/sms/system")
public class SystemController {

    @Autowired
    AdminService adminService;
    @Autowired
    StudentService studentService;
    @Autowired
    TeacherService teacherService;


    @ApiOperation("个人信息-修改密码")
    @PostMapping("/updatePwd/{oldPwd}/{newPwd}")
    public Result updatePwd(
            @ApiParam("token口令") @RequestHeader("token") String token,
            @ApiParam("旧密码") @PathVariable("oldPwd") String oldPwd,
            @ApiParam("新密码") @PathVariable("newPwd") String newPwd
    ){
        //判断token是否过期
        boolean expiration = JwtHelper.isExpiration(token);
        if (expiration){
            return Result.fail().message("token失效");
        }
        //通过token获取当前的用户id
        Long userId = JwtHelper.getUserId(token);
        //通过token获取当前的用户类型
        Integer userType = JwtHelper.getUserType(token);

        //将明文密码转换为密文
        oldPwd = MD5.encrypt(oldPwd);
        newPwd = MD5.encrypt(newPwd);

        //判断用户类型进行修改密码
        switch (userType){
            case 1:
                QueryWrapper<Admin> adminQueryWrapper = new QueryWrapper<>();
                adminQueryWrapper.eq("id",userId.intValue());
                adminQueryWrapper.eq("password",oldPwd);
                Admin admin = adminService.getOne(adminQueryWrapper);
                if (admin != null){
                    //修改密码
                    admin.setPassword(newPwd);
                    adminService.saveOrUpdate(admin);
                }else {
                    return Result.fail().message("原密码有误！");
                }
                break;
            case 2:
                QueryWrapper<Student> studentQueryWrapper = new QueryWrapper<>();
                studentQueryWrapper.eq("id",userId.intValue());
                studentQueryWrapper.eq("password",oldPwd);
                Student student = studentService.getOne(studentQueryWrapper);
                if (student != null){
                    //修改密码
                    student.setPassword(newPwd);
                    studentService.saveOrUpdate(student);
                }else {
                    return Result.fail().message("原密码有误！");
                }
                break;
            case 3:
                QueryWrapper<Teacher> teacherQueryWrapper = new QueryWrapper<>();
                teacherQueryWrapper.eq("id",userId.intValue());
                teacherQueryWrapper.eq("password",oldPwd);
                Teacher teacher = teacherService.getOne(teacherQueryWrapper);
                if (teacher != null){
                    //修改密码
                    teacher.setPassword(newPwd);
                    teacherService.saveOrUpdate(teacher);
                }else {
                    return Result.fail().message("原密码有误！");
                }
                break;
        }

        return Result.ok();

    }


    @ApiOperation("头像上传统一入口")
    @PostMapping("/headerImgUpload")
    public Result headerImgUpload(
            @ApiParam("文件二进制数据") @RequestPart("multipartFile") MultipartFile multipartFile
    ){

        //使用UUID随机生成文件名
        String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        //生成新的文件名字
        String filename = uuid.concat(multipartFile.getOriginalFilename());
        //生成文件的保存路径(实际生产环境这里会使用真正的文件存储服务器)
        String portraitPath ="F:/IdeaProjects/system-management/target/classes/public/upload/".concat(filename);
        //保存文件
        try {
            multipartFile.transferTo(new File(portraitPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String headerImg ="upload/"+filename;
        return Result.ok(headerImg);
    }

    @ApiOperation("通过token口令获取当前登录的用户信息的方法")
    @GetMapping("/getInfo")
    public Result getInfoByToken(@ApiParam("token口令") @RequestHeader("token") String token){
        //判断token是否过期
        boolean expiration = JwtHelper.isExpiration(token);
        if (expiration){
            return Result.build(null,ResultCodeEnum.CODE_ERROR);
        }
        //从token中解析出 用户id和用户类型
        Long userId = JwtHelper.getUserId(token);
        Integer userType = JwtHelper.getUserType(token);

        // 准备一个Map集合用于存响应的数据
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        switch (userType){
            case 1:
                Admin admin = adminService.getAdminById(userId);
                map.put("userType",1);
                map.put("user",admin);
                break;
            case 2:
                Student student = studentService.getStudentById(userId);
                map.put("userType",2);
                map.put("user",student);
                break;
            case 3:
                Teacher teacher = teacherService.getTeacherById(userId);
                map.put("userType",3);
                map.put("user",teacher);
                break;
        }
        return Result.ok(map);
    }


    @ApiOperation("登录请求验证")
    @PostMapping("/login")
    public Result login(@ApiParam("登录提交的form表单") @RequestBody LoginForm loginForm, HttpServletRequest request){
        //验证码校验
        HttpSession session = request.getSession();
        //获取我们保存的session
        String sessionVerifiCode = (String) session.getAttribute("verifiCode");
        //获取前端输入的
        String loginVerifiCode = loginForm.getVerifiCode();
        //判断sessionVerifiCode是否为空或者空字符串
        if ("".equals(sessionVerifiCode) || null == sessionVerifiCode){
            // session过期,验证码超时,
            return Result.fail().message("验证码失效，请刷新后重试");
        }
        //判断用户输入的session和我们保存的session是否相同
        if (!loginVerifiCode.equalsIgnoreCase(sessionVerifiCode)){
            // 验证码有误
            return Result.fail().message("验证码有误，请重新输入");
        }
        // 验证码使用完毕,移除当前请求域中的验证码
        session.removeAttribute("verifiCode");


        //用户校验

        //准备一个map用户存放响应的数据
        Map<String, Object> map = new LinkedHashMap<>();
        // 根据用户身份,验证登录的用户信息
        switch (loginForm.getUserType()) {
            case 1:// 管理员身份
                try {
                    // 调用服务层登录方法,根据用户提交的LoginInfo信息,查询对应的Admin对象,找不到返回Null
                    Admin admin = adminService.login(loginForm);
                    if (null != admin) {
                        //用户的id和用户的类型转化成一个密文，以token的名称向客户端反馈
                        // String token = JwtHelper.createToken(admin.getId().longValue(), 1);
                        // map.put("token",token);
                        // 登录成功,将用户id和用户类型转换为token口令,作为信息响应给前端
                        map.put("token", JwtHelper.createToken(admin.getId().longValue(), 1));
                    } else {
                        throw new RuntimeException("用户名或者密码错误");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    // 捕获异常,向用户响应错误信息
                    return Result.fail().message(e.getMessage());
                }
            case 2:// 学生身份
                try {
                    // 调用服务层登录方法,根据用户提交的LoginInfo信息,查询对应的Student对象,找不到返回Null
                    Student student = studentService.login(loginForm);
                    if (null != student) {
                        //用户的id和用户的类型转化成一个密文，以token的名称向客户端反馈
                        // String token = JwtHelper.createToken(admin.getId().longValue(), 1);
                        // map.put("token",token);
                        // 登录成功,将用户id和用户类型转换为token口令,作为信息响应给前端
                        map.put("token", JwtHelper.createToken(student.getId().longValue(), 2));
                    } else {
                        throw new RuntimeException("用户名或者密码错误");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    // 捕获异常,向用户响应错误信息
                    return Result.fail().message(e.getMessage());
                }
            case 3:// 教师身份
                try {
                    // 调用服务层登录方法,根据用户提交的LoginInfo信息,查询对应的Teacher对象,找不到返回Null
                    Teacher teacher = teacherService.login(loginForm);
                    if (null != teacher) {
                        //用户的id和用户的类型转化成一个密文，以token的名称向客户端反馈
                        // String token = JwtHelper.createToken(admin.getId().longValue(), 1);
                        // map.put("token",token);
                        // 登录成功,将用户id和用户类型转换为token口令,作为信息响应给前端
                        map.put("token", JwtHelper.createToken(teacher.getId().longValue(), 3));
                    } else {
                        throw new RuntimeException("用户名或者密码错误");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    // 捕获异常,向用户响应错误信息
                    return Result.fail().message(e.getMessage());
                }
        }
        // 查无此用户,响应失败
        return Result.fail().message("查无此用户");
    }


    @ApiOperation("获取验证码图片")
    @GetMapping("/getVerifiCodeImage")
    public void getVerifiCodeImage(HttpServletRequest request,HttpServletResponse response){
        //获取图片
        BufferedImage verifiCodeImage = CreateVerifiCodeImage.getVerifiCodeImage();

        //获取图片的验证码（默认返回一个数组，用new String()转换成字符串对象）
        // char[] verifiCode = CreateVerifiCodeImage.getVerifiCode();
        String verifiCode = new String(CreateVerifiCodeImage.getVerifiCode());

        //将验证码文本放入session域（通过HttpServletRequest对象获取session对象），为下一次验证做准备
        HttpSession session = request.getSession();
        //void setAttribute(String var1, Object var2);用来设置session值，var1是名称，var2保存的值
        session.setAttribute("verifiCode",verifiCode);

        //将验证码图片响应给浏览器（通过HttpServletResponse对象获得输出流，ImageIO工具类响应给浏览器）
        try {
            //获得输出流
            // ServletOutputStream outputStream = response.getOutputStream();
            //通过ImageIO工具类的write方法把图片（verifiCodeImage）通过输出流响应给浏览器
            // ImageIO.write(verifiCodeImage,"JPEG",outputStream);
            ImageIO.write(verifiCodeImage,"JPEG",response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
