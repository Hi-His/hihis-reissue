package com.bsoft.reissue.dto;

import com.bsoft.reissue.entity.AnchorPointLog;
import com.bsoft.reissue.entity.AnchorPointReissue;
import lombok.Data;

/**
 * @author: 何胜豪
 * @Title: TODO
 * @Package: com.bsoft.reissue.dto
 * @Description:
 * @date : 2022/1/12 13:50
 */
@Data
public class Pagination {
    private Integer current;
    private String size;

    private AnchorPointReissue anchorPointReissue;

    private AnchorPointLog anchorPointLog;
}
