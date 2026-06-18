package com.github.owenliou.campkeeper.backend.app.service;

public interface ICampService {

    void saveSnapshot();

    void saveLinksSnapshot();

    int syncAll();

    int syncAllLinks();

}
