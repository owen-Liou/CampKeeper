package com.github.owenliou.campkeeper.backend.app.external.icamping.client;

import com.github.owenliou.campkeeper.backend.app.external.icamping.dto.ICampingStore;
import com.github.owenliou.campkeeper.backend.app.external.icamping.dto.ICampingStoreListResponse;
import com.github.owenliou.campkeeper.backend.app.external.icamping.variables.ICampingApiKey;
import com.github.owenliou.campkeeper.backend.app.external.icamping.variables.ICampingApiPath;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
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

    public List<ICampingStore> fetchAllStores() {
        ICampingStoreListResponse response = restClient.get()
                .uri(ICampingApiPath.STORE_LIST.getPath() + "?only_show_user_like=false&key={key}", apiKey)
                .retrieve()
                .body(ICampingStoreListResponse.class);

        if (response == null || !"ok".equals(response.getStatus()) || response.getItems() == null) {
            return Collections.emptyList();
        }
        return response.getItems();
    }
}