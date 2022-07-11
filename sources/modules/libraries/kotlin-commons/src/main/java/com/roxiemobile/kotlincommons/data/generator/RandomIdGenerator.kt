@file:Suppress("unused")

package com.roxiemobile.kotlincommons.data.generator

import com.roxiemobile.java.util.toByteArray
import com.roxiemobile.kotlin.toHex
import java.security.MessageDigest
import java.util.UUID

object RandomIdGenerator {

// MARK: - Methods

    fun createId(length: Short = 20, uppercased: Boolean = false): String {

        // MD5, SHA-1, SHA-256 and SHA-512 speed performance
        // @link https://automationrhapsody.com/md5-sha-1-sha-256-sha-512-speed-performance/

        val algorithm = when (length) {
            in 1..40 -> "SHA-1"
            in 41..56 -> "SHA-224"
            in 57..64 -> "SHA-256"
            in 65..96 -> "SHA-384"
            in 97..128 -> "SHA-512"
            else -> error("length must be in range [1...128], passed: ${length}")
        }

        val uuidBytes = UUID.randomUUID()
            .toByteArray()

        val hash = MessageDigest.getInstance(algorithm)
            .digest(uuidBytes)

        val randomId = hash
            .toHex(uppercased)
            .substring(startIndex = 0, endIndex = length.toInt())

        return randomId
    }
}
