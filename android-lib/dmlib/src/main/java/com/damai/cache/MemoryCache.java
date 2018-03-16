package com.damai.cache;

import android.provider.Settings;
import android.support.v4.util.LruCache;

import com.damai.core.Cache;

import java.util.Map;

/**
 * Created by renxueliang on 17/4/11.
 */

public class MemoryCache implements Cache {

    private LruCache<String,CacheInfo> cache;

    static class CacheInfo{

        public long getLastModified() {
            return lastModified;
        }

        public void setLastModified(long lastModified) {
            this.lastModified = lastModified;
        }

        long lastModified;

        public byte[] getData() {
            return data;
        }

        public void setData(byte[] data) {
            this.data = data;
        }

        byte[] data;
    }

    public MemoryCache(){
        cache = new LruCache<String,CacheInfo>(30);
    }

    @Override
    public CacheResult get(String key, CacheExpire cacheExpire) {
        CacheInfo info = cache.get(key);
        if(info==null){
            return null;
        }
        return new CacheResult(info.getData(),cacheExpire.isExpire(info.getLastModified()));
    }

    @Override
    public byte[] get(String key) {
        CacheInfo info = cache.get(key);
        if(info==null){
            return null;
        }
        return info.getData();
    }

    @Override
    public void put(String key, byte[] data) {
        CacheInfo info = new CacheInfo();
        info.setLastModified(System.currentTimeMillis());
        info.setData(data);

        cache.put(key,info);
    }

    @Override
    public void clear() {

    }

    @Override
    public void removeAllCache(String key) {

       Map<String,CacheInfo> map = cache.snapshot();
        for (String name: map.keySet()) {
            if(name.startsWith(key)){
                cache.remove(name);
            }
        }
    }
}
