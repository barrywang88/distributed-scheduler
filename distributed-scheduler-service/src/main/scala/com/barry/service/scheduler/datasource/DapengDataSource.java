package com.barry.service.scheduler.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.dapeng.core.helper.SoaSystemEnvProperties;
import com.barry.service.scheduler.datasource.utils.RSAUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.Cipher;

/**
 * 大鹏 自定义数据源
 *
 * @author huyj
 * @Created 2018-09-27 10:28
 */
public class DapengDataSource extends DruidDataSource {
    private static final Logger LOGGER = LoggerFactory.getLogger(DapengDataSource.class);

    @Value("${DB_PUBLICKEY}")
    private String publicKey;

    private static final String PUBLIC_KEY = "DB_PUBLICKEY";

    @Override
    public void setUsername(String username) {
        if (SoaSystemEnvProperties.SOA_DATABASE_ENCRYPT_ENABLE) {
            username = decrypt(username);
        }
        super.setUsername(username);
    }

    @Override
    public void setPassword(String password) {
        if (SoaSystemEnvProperties.SOA_DATABASE_ENCRYPT_ENABLE) {
            password = decrypt(password);
        }
        super.setPassword(password);
    }

    private String decrypt(String secret) {
        String publicKey = getPublicKey();
        if (StringUtils.isBlank(publicKey)) {
            LOGGER.error("找不到数据库公钥[" + PUBLIC_KEY + "]或者[database_encrypt_publickey]");
            System.exit(-1);
        }
        return RSAUtils.encryptByPublicKey(secret, publicKey, Cipher.DECRYPT_MODE);
    }

    private String getPublicKey() {
        String publicKey_ = publicKey;
        if (StringUtils.isBlank(publicKey_)) {
            publicKey_ = System.getProperty(PUBLIC_KEY);
            if (StringUtils.isBlank(publicKey_)) {
                publicKey_ = SoaSystemEnvProperties.SOA_DATABASE_ENCRYPT_PUBLICKEY;
            }
        }
        return publicKey_;
    }
}
