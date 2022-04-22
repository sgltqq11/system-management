package com.sgl.sm.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_admin")  //实现实体类型和数据库中的表实现映射
public class Admin {
    /*
    @Tableld注解是专门用在主键上的注解，
    如果数据库中的主键字段名和实体中的属性名，不一样且不是驼峰之类的对应关系，
    可以在实体中表示主键的属性上加@Tableid注解，
    并指定@Tableid注解的value属性值为表中主键的字段名既可以对应上。
     */
    @TableId(value ="id",type = IdType.AUTO)
    private Integer id;
    private String name;
    private char gender;
    private String password;
    private String email;
    private String telephone;
    private String address;
    private String portraitPath; //头像的图片路径
}
