package com.github.owenliou.campkeeper.backend.app.external.icamping.client;

import com.github.owenliou.campkeeper.backend.app.external.icamping.dto.ICampingStoreExternalLinkDto;
import com.github.owenliou.campkeeper.backend.app.external.icamping.response.ICampingResponse;
import com.github.owenliou.campkeeper.backend.app.external.icamping.response.ICampingStoreExternalLinkResponse;
import com.github.owenliou.campkeeper.backend.app.external.icamping.dto.ICampingStoreListDto;
import com.github.owenliou.campkeeper.backend.app.external.icamping.response.ICampingStoreListResponse;
import com.github.owenliou.campkeeper.backend.app.external.icamping.variables.ICampingApiKey;
import com.github.owenliou.campkeeper.backend.app.external.icamping.variables.ICampingApiPath;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.List;

@Component
public class ICampingClient {

    private final RestClient restClient;
    private final String apiKey;

    public ICampingClient(@Qualifier("iCampingRestClient") RestClient restClient) {
        this.restClient = restClient;
        this.apiKey = ICampingApiKey.GUEST.getValue();
    }

    public List<ICampingStoreListDto> fetchAllStores() {
        String url = buildUrl(ICampingApiPath.API_BASE_URL_TIER1, ICampingApiPath.STORE_LIST,
                "only_show_user_like", "false");
        return fetchItems(url, ICampingStoreListResponse.class);
    }

    public List<ICampingStoreExternalLinkDto> fetchStoreLinksByStoreName(String storeName) {
        String url = buildUrl(ICampingApiPath.API_BASE_URL_TIER2, ICampingApiPath.EXTERNAL_LINK_LIST,
                "store_name", storeName);
        return fetchItems(url, ICampingStoreExternalLinkResponse.class);
    }

    // ---- private helpers ----

    /**
     * 統一的 HTTP GET → 驗證 → 取 items 流程
     */
    private <T, R extends ICampingResponse<T>> List<T> fetchItems(String url, Class<R> responseType) {

        R response = restClient.get().uri(url).retrieve().body(responseType);

        return isValidResponse(response) ? response.getItems() : Collections.emptyList();
    }

    private boolean isValidResponse(ICampingResponse<?> response) {
        return response != null && "ok".equals(response.getStatus()) && response.getItems() != null;
    }

    /**
     * 統一的 URL builder，接受單一額外 query param
     * 如果未來有多個 param，可改傳 Map<String, String>
     */
    private String buildUrl(ICampingApiPath baseUrl, ICampingApiPath path, String paramKey, String paramValue) {
        return UriComponentsBuilder
                .fromUriString(baseUrl.getPath())
                .path(path.getPath())
                .queryParam(paramKey, paramValue)
                .queryParam("key", apiKey)
                .build()
                .toUriString();
    }
}