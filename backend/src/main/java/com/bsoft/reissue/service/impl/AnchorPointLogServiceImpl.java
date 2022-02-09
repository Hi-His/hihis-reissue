package com.bsoft.reissue.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bsoft.reissue.dao.AnchorPointLogDao;
import com.bsoft.reissue.entity.AnchorPointLog;
import com.bsoft.reissue.service.AnchorPointLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: 何胜豪
 * @Title: TODO
 * @Package: com.bsoft.reissue.service.impl
 * @Description:
 * @date : 2022/1/12 12:26
 */
@Service
public class AnchorPointLogServiceImpl extends ServiceImpl<AnchorPointLogDao,AnchorPointLog> implements AnchorPointLogService {

    @Autowired
    AnchorPointLogDao anchorPointLogDao;

    @Override
    public Page<AnchorPointLog> selectLogsPage(Page<AnchorPointLog> page, QueryWrapper queryWrapper) {
        return anchorPointLogDao.selectPage(page,queryWrapper);
    }
}
