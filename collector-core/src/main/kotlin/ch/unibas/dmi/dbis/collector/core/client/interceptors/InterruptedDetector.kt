package ch.unibas.dmi.dbis.collector.core.client.interceptors

import okhttp3.Interceptor
import okhttp3.Response

class InterruptedDetector : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (Thread.interrupted()) {
            throw InterruptedException("Interceptor detected thread interruption.")
        }

        return chain.proceed(chain.request())
    }

}
