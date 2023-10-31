package com.gzlh.cache;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import com.gzlh.entity.AgreeBO;
import com.gzlh.task.WhiteIpDTO;

public class CacheManager {
    private static final TimedCache<String, AgreeBO> CACHE_MAP = CacheUtil.newTimedCache(60000);

    private static WhiteIpDTO whiteIpDTO = null;

    /**
     * @param key
     * @param result 0 等待结果 1 成功 2 重新填写
     */
    public static void setCache(String key, AgreeBO bo) {
        CACHE_MAP.put(key, bo);
    }

    public static AgreeBO getCache(String key) {
        AgreeBO result = CACHE_MAP.get(key);
        return result;
    }

    public static void setWhiteIpDTO(WhiteIpDTO dto) {
        whiteIpDTO = dto;
    }

    public static WhiteIpDTO getWhiteIpDTO() {
        return whiteIpDTO == null ? new WhiteIpDTO() : whiteIpDTO;
    }
}
