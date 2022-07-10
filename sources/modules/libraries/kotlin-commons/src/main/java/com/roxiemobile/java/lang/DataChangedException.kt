@file:Suppress("unused")

package com.roxiemobile.java.lang

class DataChangedException: RuntimeException {

// MARK: - Construction

    constructor(): super()

    constructor(message: String): super(message)

    constructor(message: String, cause: Throwable): super(message, cause)

    constructor(cause: Throwable): super(cause)
}
