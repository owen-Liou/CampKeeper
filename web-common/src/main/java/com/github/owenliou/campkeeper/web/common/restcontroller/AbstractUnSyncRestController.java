package com.github.owenliou.campkeeper.web.common.restcontroller;

import com.github.owenliou.campkeeper.common.CustomResult;
import com.github.owenliou.campkeeper.common.exception.sso.ApiKeyInvalidException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import tw.gov.nat.mydata.base.BaseReply;
import tw.gov.nat.mydata.base.Paging;
import tw.gov.nat.mydata.type.SysCode;
import tw.gov.nat.mydata.util.EnvUtils;
import com.github.owenliou.campkeeper.web.common.sso.service.PrivilegeService;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public abstract class AbstractUnSyncRestController {

    @Autowired
    protected PrivilegeService privilegeService;

    protected void checkApiKey(HttpServletRequest request) {
        final String xApiKey = request.getHeader("x-api-key");

        if (StringUtils.isBlank(xApiKey) || !StringUtils.equals(xApiKey, EnvUtils.discoveryApiKey)) {
            throw new ApiKeyInvalidException("apiKey is invalid, {}", StringUtils.isBlank(xApiKey)? null: StringUtils.substring(xApiKey, 0, 10));
        }
    }

    /**
     * 重導向 http 403 及回覆資料。
     * @param response HttpServletResponse
     * @param url 重導向網址 (來源根目錄：/)
     */
    public Mono<ResponseEntity<?>> redirect(HttpServletResponse response, String url) {
        // 將參數進行 URL 編碼
        response.setHeader("Location", url);
        return Mono.just(ResponseEntity.status(HttpStatus.FOUND).build());
    }

    protected Mono<ResponseEntity<BaseReply>> ok(Object body) {
        return ok(body, (Paging) null);
    }

    protected Mono<ResponseEntity<BaseReply>> ok(Page<?> p) {
        Paging paging = Paging.builder()
                .limit(p.getPageable().getPageSize())
                .offset(p.getPageable().getOffset())
                .total(p.getTotalElements())
                .build();
        return sendMessage(HttpStatus.OK, SysCode.OK, null, p.getContent(), paging);
    }

    protected Mono<ResponseEntity<BaseReply>> ok(Object body, Paging paging) {
        return sendMessage(HttpStatus.OK, SysCode.OK, null, body, paging);
    }

    protected Mono<ResponseEntity<BaseReply>> badRequest(SysCode sysCode) {
        return badRequest(sysCode, null, null);
    }

    protected Mono<ResponseEntity<BaseReply>> badRequest(SysCode sysCode, String information) {
        return badRequest(sysCode, information, null);
    }

    protected ResponseEntity<BaseReply> syncBadRequest(SysCode sysCode, String information) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseReply(sysCode, information, null, null));
    }

    /**
     * 回覆 http 400 及錯誤代碼及訊息。
     * @param sysCode 錯誤代碼
     * @param information 回覆訊息
     * @param data 輸出資料
     */
    protected Mono<ResponseEntity<BaseReply>> badRequest(SysCode sysCode, String information, Object data) {
        return sendMessage(HttpStatus.BAD_REQUEST, sysCode, information, data,null);
    }

    /**
     * 回覆 http 401 及錯誤代碼及訊息。
     */
    public Mono<ResponseEntity<BaseReply>> unauthorized(SysCode sysCode) {
        return unauthorized(sysCode, null);
    }

    /**
     * 回覆 http 401 及錯誤代碼及訊息。
     */
    protected Mono<ResponseEntity<BaseReply>> unauthorized(SysCode sysCode, String information) {
        return sendMessage(HttpStatus.UNAUTHORIZED, sysCode, information, null,null);
    }

    /**
     * 回覆
     * @param httpStatus http狀態
     * @param sysCode 錯誤代碼
     * @param information 回覆訊息(null時採用sysCode的訊息)
     * @param data 輸出資料
     */
    protected Mono<ResponseEntity<BaseReply>> sendMessage(HttpStatus httpStatus, SysCode sysCode, String information, Object data, Paging paging) {
        return Mono.just(ResponseEntity.status(httpStatus).body(new BaseReply(sysCode, information, data, paging)));
    }

    /*
     * 驗證 Session 是否合法，不能是 null，也不能是新建
     * @param session
    protected boolean isSessionInvalid(HttpSession session) {
        return session == null || session.isNew();
    }
     */

    protected <T> Mono<ResponseEntity<CustomResult<T>>> ok(CustomResult<T> data) {
        return Mono.just(ResponseEntity.status(HttpStatus.OK).body(data));
    }

    protected ResponseEntity<CustomResult<String>> notFound(String information) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(CustomResult.result(false, information));
    }

    protected ResponseEntity<CustomResult<String>> forbidden(String information) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(CustomResult.result(false, information));
    }

    protected static final String CONTENT_DISPOSITION_ATTACHMENT = "attachment";
//    protected static final String CONTENT_DISPOSITION_INLINE = "inline";

    protected ResponseEntity<Resource> downloadFile(byte[] data, MediaType contentType, String fileOutputName) {
        if (data == null || data.length == 0) {
            return ResponseEntity.notFound().build();
        }

        ByteArrayResource resource = new ByteArrayResource(data);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, genContentDisposition(CONTENT_DISPOSITION_ATTACHMENT, fileOutputName))
                .contentType(contentType)
                .contentLength(data.length)
                .body(resource);

    }

    @SneakyThrows
    protected static String genContentDisposition(String disposition, String fileName) {
        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        return "%s; filename=\"%s\"; filename*=UTF-8''%s".formatted(
                disposition,
                new String(fileName.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.ISO_8859_1), // 舊瀏覽器
                encodedFileName // 現代瀏覽器
        );
    }

}
