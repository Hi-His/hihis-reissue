package com.bsoft.reissue.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * @author: 何胜豪
 * @Title: TODO
 * @Package: com.bsoft.reissue.dto
 * @Description:
 * @date : 2022/1/12 19:27
 */
@Data
@AllArgsConstructor
public class BasicResponse {

    private Boolean success;
    private String result;
}
