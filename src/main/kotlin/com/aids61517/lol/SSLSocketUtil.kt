package com.aids61517.lol

import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.*

object SSLSocketUtil {

    fun getSocketFactory(manager: TrustManager): SSLSocketFactory {
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, arrayOf(manager), SecureRandom())
        return sslContext.socketFactory
    }

    fun getX509TrustManager(): X509TrustManager {
        return object : X509TrustManager {
            override fun checkClientTrusted(p0: Array<out X509Certificate>?, p1: String?) {
            }

            override fun checkServerTrusted(p0: Array<out X509Certificate>?, p1: String?) {
            }

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return emptyArray()
            }

        }
    }

    fun getHostnameVerifier(): HostnameVerifier {
        return HostnameVerifier { p0, p1 -> true }
    }
}