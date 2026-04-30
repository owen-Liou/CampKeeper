package com.github.owenliou.campkeeper.base.exception;

import com.github.owenliou.campkeeper.base.utils.text.StrUtils;

public class EndpointNotFoundException extends RestCustomException {

    public EndpointNotFoundException(String dpId, String endpoint) {
        super(StrUtils.format("找不到 DP 的 endpoint, {}, {}", dpId, endpoint));
    }

}
