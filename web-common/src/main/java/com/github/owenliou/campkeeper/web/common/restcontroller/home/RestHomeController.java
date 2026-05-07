package com.github.owenliou.campkeeper.web.common.restcontroller.home;

import com.github.owenliou.campkeeper.common.CustomResult;
import com.github.owenliou.campkeeper.config.variables.Profiles;
import com.github.owenliou.campkeeper.web.common.restcontroller.AbstractUnSyncRestController;
import com.github.owenliou.campkeeper.web.common.sso.dto.CustomIdToken;
import com.github.owenliou.campkeeper.web.common.sso.service.PrivilegeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Profile(Profiles.NOT_PRODS)
@RestController
@RequestMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
@Tag(name = "API Root", description = "測試用")
public class RestHomeController extends AbstractUnSyncRestController
{

    @Autowired
    private PrivilegeService privilegeService;

    @GetMapping("/")
    public Mono<ResponseEntity<CustomResult<CustomIdToken>>> home() {
        return ok(CustomResult.result(true, privilegeService.getCurrentSsoUserDetails().getIdToken()));
    }


}
