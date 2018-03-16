package com.damai.core;


public interface Cache {

    /**
     * 过期时间判断
     *
     * 需要和缓存策略配合使用，如果缓存策略需要检查缓存，则缓存时间为3g网络6小时，wifi半小时（可配置）
     *
     *
     * @author Randy
     *
     */
    public static interface CacheExpire{
        /**
         * 是否过期
         * @param time	需要判断的时间
         * @return
         */
        boolean isExpire(long time);
    }

    /**
     *
     * @author Randy
     *
     */
    public static class CacheResult{
        private byte[] data;
        private boolean isExpire;

        public CacheResult(byte[] data,boolean isExpire){
            this.data = data;
            this.isExpire = isExpire;
        }

        public byte[] getData() {
            return data;
        }
        public void setData(byte[] data) {
            this.data = data;
        }
        public boolean isExpire() {
            return isExpire;
        }
        public void setExpire(boolean isExpire) {
            this.isExpire = isExpire;
        }

    }

    /**
     * 判断结果是否存在
     * @param cacheExpire
     * @return
     */
    CacheResult get(String key,CacheExpire cacheExpire);

    /**
     * 获取，忽略过期时间
     * @param key
     * @return
     */
    byte[] get(String key);

    /**
     * 存放
     * @param key
     * @param data
     */
    void put(String key,byte[] data);

    void clear();


    /**
     * 移除缓存,匹配模式
     */
    void removeAllCache(String key);



}
