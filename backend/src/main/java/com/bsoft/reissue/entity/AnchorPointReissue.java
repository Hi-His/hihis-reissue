package com.bsoft.reissue.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.beans.Transient;
import java.io.Serializable;

@Data
@TableName("hi_anchorpoint_reissue")
public class AnchorPointReissue implements Serializable {

    @TableId
    private String idAnchorpointreissue;
    /* rpc服务名称 */
    @TableField("bean_name")
    private String beanName;
    /* rpc方法名称 */
    @TableField("method")
    private String method;
    /* 参数JSON */
    @TableField("params")
    private String params;
    /* 参数的类型 */
    @TableField("params_class")
    private String paramsClass;
    /* 锚点ID */
    @TableField("cd")
    private String cd;
    /* 插入时间 */
    @TableField("insert_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="Asia/Shanghai")
    private String insertTime;

    @TableField(exist = false)
    private String tk;
}
