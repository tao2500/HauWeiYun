package com.bdc.reservationService.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author bc
 * @since 2023-05-16
 */
@TableName("x_reservationinformation")
@Data
public class Reservationinformation implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 预约用户的id
     */
    private Integer userId;

    /**
     * 预约的时间，格式为：yyyy-mm-dd
     */
    private String time;

    /**
     * 办理预约的时间
     */
    private String timeNow;

    /**
     * 预约时段
     */
    private String timeRe;

    /**
     * 预约的行政区id
     */
    private Integer centerId;

    /**
     * 预约的一级业务类型id
     */
    private Integer firstId;

    /**
     * 预约状态，0表示取消预约，1表示成功预约
     */
    private Integer aState;

    /**
     * 办理状态，0表示未办理，1表示成功办理，2表示失约
     */
    private Integer pState;

    /**
     * 该预约修改次数，只允许修改一次，默认为0
     */
    private Integer num;

    /**
     * 服务窗口
     * @return
     */
    private String window;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
    public String getTimeRe() {
        return timeRe;
    }

    public void setTimeRe(String timeRe) {
        this.timeRe = timeRe;
    }
    public Integer getCenterId() {
        return centerId;
    }

    public void setCenterId(Integer centerId) {
        this.centerId = centerId;
    }
    public Integer getFirstId() {
        return firstId;
    }

    public void setFirstId(Integer firstId) {
        this.firstId = firstId;
    }
    public Integer getaState() {
        return aState;
    }

    public void setaState(Integer aState) {
        this.aState = aState;
    }
    public Integer getpState() {
        return pState;
    }

    public void setpState(Integer pState) {
        this.pState = pState;
    }
    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "Reservationinformation{" +
            "id=" + id +
            ", userId=" + userId +
            ", time=" + time +
            ", timeNow=" + timeNow +
            ", timeRe=" + timeRe +
            ", centerId=" + centerId +
            ", firstId=" + firstId +
            ", window=" + window +
            ", aState=" + aState +
            ", pState=" + pState +
            ", num=" + num +
        "}";
    }
}
