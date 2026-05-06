package com.github.owenliou.campkeeper.common.exception;

import com.github.owenliou.campkeeper.common.utils.StrUtils;

public class EndpointNotFoundException extends RestCustomException {

    public EndpointNotFoundException(String dpId, String endpoint) {
        super(StrUtils.format("找不到 DP 的 endpoint, {}, {}", dpId, endpoint));
    }

}
