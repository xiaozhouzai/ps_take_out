package com.lcy.reggie.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 员工实体
 * @eo.api-type http
 * @eo.groupName 默认分组
 * @eo.path
 */
@Data
public class Employee implements Serializable {

    /**
     * serialVersionUID
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * username
     */
    private String username;

    /**
     * name
     */
    private String name;

    /**
     * password
     */
    private String password;

    /**
     * phone
     */
    private String phone;

    /**
     * sex
     */
    private String sex;

    /**
     * idNumber
     */
    private String idNumber;//身份证号码

    private Integer status;

    @TableField(fill=FieldFill.INSERT) //插入时填充字段
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)//插入和更新时填充字段
    private LocalDateTime updateTime;

    @TableField(fill = FieldFill.INSERT)//插入时填充字段
    private Long createUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)//插入和更新时填充字段
    private Long updateUser;

}
