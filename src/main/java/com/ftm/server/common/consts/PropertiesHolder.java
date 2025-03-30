package com.ftm.server.common.consts;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PropertiesHolder {

    @Value("${cdn.path.root}")
    private String cdnPathValue;

    public static String CDN_PATH;

    @PostConstruct
    public void init() {
        CDN_PATH = cdnPathValue;
    }
}
