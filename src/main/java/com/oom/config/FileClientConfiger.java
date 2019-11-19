package com.oom.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="fileClient")
@Data
public class FileClientConfiger {

    /** 服务器地址**/
    private String fileServerUrl;

    /** 删除的服务器地址**/
    private String deleteServerUrl;

    /** token**/
    private String token;

    /** 客户端私钥**/
    private String clientPrivateKey;

    /** 客户端公钥**/
    private String clientPublicKey;

    /** 服务端公钥**/
    private String serverPublicKey;
}
