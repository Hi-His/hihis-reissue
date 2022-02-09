package com.bsoft.reissue.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bsoft.reissue.entity.AnchorPointLog;
import org.springframework.stereotype.Service;

/**
 * @author: 何胜豪
 * @Title: TODO
 * @Package: com.bsoft.reissue.service
 * @Description:
 * @date : 2022/1/12 12:26
 */

public interface AnchorPointLogService  extends IService<AnchorPointLog>   {

    Page<AnchorPointLog> selectLogsPage(Page<AnchorPointLog> page, QueryWrapper queryWrapper);


}
