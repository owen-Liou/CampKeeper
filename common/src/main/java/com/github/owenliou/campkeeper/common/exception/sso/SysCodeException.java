package com.github.owenliou.campkeeper.common.exception.sso;

import com.github.owenliou.campkeeper.common.utils.StrUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tw.gov.nat.mydata.type.SysCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SysCodeException extends Exception {

    private final SysCode sysCode;

    public SysCodeException(SysCode sysCode) {
        super(sysCode.getMessage());
        this.sysCode = sysCode;
    }

    public SysCodeException(SysCode sysCode, String message, Object... arguments) {
        super(StrUtils.format(message, arguments));
        this.sysCode = sysCode;
    }

}
