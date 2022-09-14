package ch.unibas.dmi.dbis.collector.rest.status

object StatusCode {

    /*
    * Unfortunately we cannot use an enum here since we want it to be compile-time constant for annotations.
    * https://en.wikipedia.org/wiki/List_of_HTTP_status_codes
    */

    // 1xx: Informational Response.
    const val CONTINUE = 100
    const val SWITCH_PROTOCOLS = 101
    const val PROCESSING = 102
    const val EARLY_HINTS = 103

    // 2xx: Success.
    const val OK = 200
    const val CREATED = 201
    const val ACCEPTED = 202
    const val NON_AUTH_INFO = 203
    const val NO_CONTENT = 204
    const val RESET_CONTENT = 205
    const val PARTIAL_CONTENT = 206
    const val MULTI_STATUS = 207
    const val ALREADY_REPORTED = 208

    // 3xx: Redirection.
    const val MULTIPLE_CHOICES = 300
    const val MOVED_PERMANENTLY = 301
    const val FOUND = 302 // Moved temporarily.
    const val SEE_OTHER = 303
    const val NOT_MODIFIED = 304
    const val USE_PROXY = 305
    const val SWITCH_PROXY = 306
    const val TEMPORARY_REDIRECT = 307
    const val PERMANENT_REDIRECT = 308

    // 4xx: Client errors.
    const val BAD_REQUEST = 400
    const val UNAUTHORIZED = 401
    const val PAYMENT_REQUIRED = 402
    const val FORBIDDEN = 403
    const val NOT_FOUND = 404
    const val METHOD_NOT_ALLOWED = 405
    const val NOT_ACCEPTABLE = 406
    const val PROXY_AUTH_REQUIRED = 407
    const val REQUEST_TIMEOUT = 408
    const val CONFLICT = 409
    const val GONE = 410
    const val LENGTH_REQUIRED = 411

    // 5xx: Server errors.
    const val INTERNAL_SERVER_ERROR = 500
    const val NOT_IMPLEMENTED = 501
    const val BAD_GATEWAY = 502
    const val SERVICE_UNAVAILABLE = 503
    const val GATEWAY_TIMEOUT = 504
    const val HTTP_VERSION_NOT_SUPPORTED = 505

}
