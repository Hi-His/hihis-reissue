package com.bsoft.reissue.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bsoft.reissue.dao.AnchorPointLogDao;
import com.bsoft.reissue.dao.AnchorPointReissueDao;
import com.bsoft.reissue.entity.AnchorPointLog;
import com.bsoft.reissue.entity.AnchorPointReissue;
import com.bsoft.reissue.service.AnchorPointReissueService;
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
public class AnchorPointReissueServiceImpl extends ServiceImpl<AnchorPointReissueDao, AnchorPointReissue> implements AnchorPointReissueService {
    @Autowired
    AnchorPointReissueDao anchorPointReissueDao;
    @Override
    public Page<AnchorPointReissue> selectLogsPage(Page<AnchorPointReissue> logPage, QueryWrapper<AnchorPointReissue> wrapper) {
        return anchorPointReissueDao.selectPage(logPage,wrapper);
    }
}
