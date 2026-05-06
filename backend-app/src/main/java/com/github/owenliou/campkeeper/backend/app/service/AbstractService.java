package com.github.owenliou.campkeeper.backend.app.service;

import com.github.owenliou.campkeeper.base.service.impl.CommonServiceImpl;
import com.github.owenliou.campkeeper.web.common.sso.service.PrivilegeService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

public abstract class AbstractService<T, ID extends Serializable> extends CommonServiceImpl<T, ID> {

    @Autowired
    protected PrivilegeService privilegeService;

}
