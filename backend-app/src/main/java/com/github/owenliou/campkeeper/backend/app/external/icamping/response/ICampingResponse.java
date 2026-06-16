package com.github.owenliou.campkeeper.backend.app.external.icamping.response;

import java.util.List;

public interface ICampingResponse<T> {
    String getStatus();
    List<T> getItems();
}