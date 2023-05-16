package com.bdc.reservationService.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * <p>
 * 
 * </p>
 *
 * @author bc
 * @since 2023-05-13
 */
@Data
@TableName("x_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 登录时间
     * */
    @TableField(exist = false)
    private Long loginTime ;

    /**
     * 令牌过期时间
     * */
    @TableField(exist = false)
    private Long expireTime ;

    @TableField(exist = false)
    private Map<String, String> time;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 手机号码，用作登录账号
     */
    private String phone;

    /**
     * 密码
     */
    private String paswd;

    /**
     * 用户状态
     */
    private Integer state;

    /**
     * 权限
     */
    private Integer power;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getPaswd() {
        return paswd;
    }

    public void setPaswd(String paswd) {
        this.paswd = paswd;
    }
    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
    public Integer getPower() {
        return power;
    }

    public void setPower(Integer power) {
        this.power = power;
    }

    @Override
    public String toString() {
        return "User{" +
            "id=" + id +
            ", phone=" + phone +
            ", paswd=" + paswd +
            ", state=" + state +
            ", power=" + power +
        "}";
    }
}
