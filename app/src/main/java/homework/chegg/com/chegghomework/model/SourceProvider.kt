package homework.chegg.com.chegghomework.model

import homework.chegg.com.chegghomework.model.cache.abs.CacheManager
import homework.chegg.com.chegghomework.model.network.RequestBuilder

/**
 * This class represents a data source.
 *
 */
class SourceProvider(val requestBuilder: RequestBuilder, val cacheManager: CacheManager)