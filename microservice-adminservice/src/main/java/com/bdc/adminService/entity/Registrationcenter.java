package com.bdc.adminService.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author bc
 * @since 2023-05-15
 */
@TableName("x_registrationcenter")
public class Registrationcenter implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 不动产登记中心名称
     */
    private String name;

    /**
     * 不动产登记中心地址
     */
    private String address;

    /**
     * 不动产登记中心电话
     */
    private String phone;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Registrationcenter{" +
            "id=" + id +
            ", name=" + name +
            ", address=" + address +
            ", phone=" + phone +
        "}";
    }
}
