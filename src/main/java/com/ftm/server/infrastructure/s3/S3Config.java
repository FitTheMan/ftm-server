package com.ftm.server.infrastructure.s3;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.sts.StsClient;
import software.amazon.awssdk.services.sts.auth.StsAssumeRoleCredentialsProvider;
import software.amazon.awssdk.services.sts.model.AssumeRoleRequest;

@Configuration
public class S3Config {

    @Value("${aws.s3.region}")
    private String region;

    @Value("${aws.credentials.access-key}")
    private String accessKey;

    @Value("${aws.credentials.secret-key}")
    private String secretKey;

    @Value("${aws.sts.role-arn}")
    private String roleArn;

    @Value("${aws.sts.role-session-name}")
    private String roleSessionName;

    @Bean
    public S3Client s3Client() {
        StaticCredentialsProvider baseCredentials =
                StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey));

        StsClient sts =
                StsClient.builder()
                        .region(Region.of(region))
                        .credentialsProvider(baseCredentials)
                        .build();

        StsAssumeRoleCredentialsProvider assumeRoleProvider =
                StsAssumeRoleCredentialsProvider.builder()
                        .stsClient(sts)
                        .refreshRequest(
                                AssumeRoleRequest.builder()
                                        .roleArn(roleArn)
                                        .roleSessionName(roleSessionName)
                                        .build())
                        .build();

        assumeRoleProvider.resolveCredentials();

        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(assumeRoleProvider)
                .build();
    }
}
