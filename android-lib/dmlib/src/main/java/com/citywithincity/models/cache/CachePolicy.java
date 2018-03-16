package com.citywithincity.models.cache;

/**
 * 缓存策略
 * 
 * @author Randy
 * 
 */

public enum CachePolicy {

	CachePolity_NoCache, // 没有缓存
	CachePolicy_TimeLimit, // 时间限制，如wifi环境，为3小时；3g为6小时，没有网络，则无法得到结果
	CachePolity_Permanent, // 永久缓存
	CachePolity_UseCacheFirst, // 优先使用缓存,如缓存存在，直接返回，然后再访问网络。
	CachePolity_Offline; // 离线模式，如缓存存在，使用缓存，如不存在直接返回

	public static CachePolicy get(int value) {
		switch (value) {
		case 0:
			return CachePolity_NoCache;
		case 1:
			return CachePolicy_TimeLimit;
		case 2:
			return CachePolity_Permanent;
		case 3:
			return CachePolity_UseCacheFirst;
		default:
			return CachePolity_Offline;
		}
	}

}
