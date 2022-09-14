package ch.unibas.dmi.dbis.collector.core.pooling

import mu.KotlinLogging
import java.util.concurrent.ConcurrentHashMap

private val logger = KotlinLogging.logger {}

abstract class SubscribablePoolManager<T : Any> {

    private val keyPoolMap = ConcurrentHashMap<String, SubscribablePoolContainer<T>>()

    protected abstract fun createPool(): SubscribablePoolContainer<T>

    fun getPoolByKey(key: String): SubscribablePoolContainer<T>? = keyPoolMap[key]

    @Synchronized
    fun subscribe(key: String, subscriber: T): SubscribablePoolContainer<T> {
        val storedPool: SubscribablePoolContainer<T>? = keyPoolMap[key]

        /*
         * We need to add a new pool if either:
         * 1. We don't have one for the given key.
         * 2. We can't subscribe anymore because the pool has been terminated.
         */
        if (storedPool == null || removeByKeyIfTerminated(key, storedPool)) {
            val newPool = createPool()
            keyPoolMap[key] = newPool
            newPool.subscribe(subscriber)
            return newPool
        }

        storedPool.subscribe(subscriber)
        return storedPool
    }

    @Synchronized
    fun unsubscribe(key: String, subscriber: T): Boolean {
        val storedPool: SubscribablePoolContainer<T> = keyPoolMap[key] ?: return false

        if (storedPool.unsubscribe(subscriber)) {
            removeByKeyIfTerminated(key, storedPool)
            return true
        }

        return false
    }

    private fun removeByKeyIfTerminated(key: String, pool: SubscribablePoolContainer<T>): Boolean {
        if (pool.isTerminated()) {
            logger.info { "Removing terminated pool for key $key." }

            keyPoolMap.remove(key, pool)

            // Terminated, pool removed.
            return true
        }

        // Not terminated, pool not removed.
        return false
    }

}
