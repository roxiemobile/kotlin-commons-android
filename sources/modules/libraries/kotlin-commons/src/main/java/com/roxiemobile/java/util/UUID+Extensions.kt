package com.roxiemobile.java.util

import com.roxiemobile.kotlincommons.util.Base62
import java.nio.ByteBuffer
import java.util.UUID

// MARK: - Extensions

fun UUID.toByteArray(): ByteArray {
    return ByteBuffer
        .allocate(16)
        .putLong(this.mostSignificantBits)
        .putLong(this.leastSignificantBits)
        .array()
}

fun UUID.toBase62(): String {
    return String(Base62.SHARED.encode(toByteArray()))
}
