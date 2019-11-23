package oom.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="fileClient")
@Data
public class FileClientConfiger {

    /** 上传服务器地址**/
    private String uploadServerUrl;

    /** 下载的服务器地址**/
    private String downloadServerUrl;

    /** 删除的服务器地址**/
    private String deleteServerUrl;

    /** 查询的服务器地址**/
    private String listServerUrl;

    /** token**/
    private String token;

    /** 客户端私钥**/
    private String clientPrivateKey;

    /** 客户端公钥**/
    private String clientPublicKey;

    /** 服务端公钥**/
    private String serverPublicKey;

    /** 服务端私钥**/
    private String serverPrivateKey;
}
