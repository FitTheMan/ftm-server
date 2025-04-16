package com.ftm.server.common.consts;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PropertiesHolder {

    @Value("${cdn.path.root}")
    private String cdnPathValue;

    @Value("${aws.s3.default.user}")
    private String userDefaultImage;

    @Value("${aws.s3.default.post}")
    private String postDefaultImage;

    @Value("${aws.s3.default.product}")
    private String productDefaultImage;

    public static String CDN_PATH;
    public static String USER_DEFAULT_IMAGE;
    public static String POST_DEFAULT_IMAGE;
    public static String PRODUCT_DEFAULT_IMAGE;

    @PostConstruct
    public void init() {
        CDN_PATH = cdnPathValue;
        USER_DEFAULT_IMAGE = userDefaultImage;
        POST_DEFAULT_IMAGE = postDefaultImage;
        PRODUCT_DEFAULT_IMAGE = productDefaultImage;
    }
}
