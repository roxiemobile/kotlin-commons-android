package com.roxiemobile.java.lang

import java.lang.RuntimeException

class DataChangedException: RuntimeException {

// MARK: - Construction

    constructor(): super()

    constructor(message: String): super(message)

    constructor(message: String, cause: Throwable): super(message, cause)

    constructor(cause: Throwable): super(cause)
}
