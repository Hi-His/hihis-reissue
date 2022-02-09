package com.bsoft.reissue.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("hi_anchorpoint_log")
public class AnchorPointLog implements Serializable {

    @TableId
    private String idAnchorpointlog;

    @TableField("cd")
    private String cd;
    /* rpc服务名称 */
    @TableField("bean_name")
    private String beanName;
    /* rpc方法名称 */
    @TableField("method")
    private String method;
    /* rpc方法入参 */
    @TableField("params")
    private String params;
    /* rpc方法出参 */
    @TableField("result")
    private String result;
    /* 执行时间 */
    @TableField("exec_time")
    private String execTime;
    /* 重发标记0、1 */
    @TableField("reissue_flag")
    private String reissueFlag;
    /* 插入时间 */
    @TableField("insert_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="Asia/Shanghai")
    private Date insertTime;

    @TableField(exist = false)
    private String tk;
}
