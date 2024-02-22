package org.example.youth_be.common.redis;

public interface TokenRepository {

    void setBlackList(String key, String data);

    Boolean hasBlackList(String key);


}
