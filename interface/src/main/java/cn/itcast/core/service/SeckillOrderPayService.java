package cn.itcast.core.service;

import java.util.Map;

public interface SeckillOrderPayService {
    Map creatNative(String userName);

    String queryPayStatus(String userName) throws Exception;

}
