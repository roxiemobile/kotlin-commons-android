@file:Suppress("NOTHING_TO_INLINE", "RedundantVisibilityModifier", "unused")

package com.roxiemobile.kotlin

// MARK: - Extensions

public fun ByteArray.toHex(uppercased: Boolean = false): String {
    val builder = StringBuilder(this.size * 2)

    val hexAlphabet = when (uppercased) {
        true -> Const.hexUpper
        else -> Const.hexLower
    }

    forEach {
        val uByte = it.toUByte().toInt()

        builder.append(hexAlphabet[uByte / 16])
        builder.append(hexAlphabet[uByte % 16])
    }

    return builder.toString()
}

public inline fun ByteArray.toLowerHex(): String {
    return toHex(uppercased = false)
}

public inline fun ByteArray.toUpperHex(): String {
    return toHex(uppercased = true)
}

// MARK: - Constants

private object Const {
    val hexUpper = arrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F')
    val hexLower = arrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f')
}
