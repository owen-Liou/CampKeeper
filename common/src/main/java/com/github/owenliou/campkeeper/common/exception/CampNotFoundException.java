package com.github.owenliou.campkeeper.common.exception;

import com.github.owenliou.campkeeper.common.utils.StrUtils;

public class CampNotFoundException extends RestCustomException {

    public CampNotFoundException(String id) {
        super(StrUtils.format("找不到 Camp 的 id, {}, {}", id));
    }

}
