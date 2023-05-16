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
@TableName("x_secondarybusiness")
public class Secondarybusiness implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 二级业务类型名称
     */
    private String name;

    /**
     * 所属一级业务类型id
     */
    private Integer firstId;

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
    public Integer getFirstId() {
        return firstId;
    }

    public void setFirstId(Integer firstId) {
        this.firstId = firstId;
    }

    @Override
    public String toString() {
        return "Secondarybusiness{" +
            "id=" + id +
            ", name=" + name +
            ", firstId=" + firstId +
        "}";
    }
}
