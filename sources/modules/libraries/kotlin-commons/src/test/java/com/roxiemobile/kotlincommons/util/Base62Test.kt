@file:Suppress("ConstantConditionIf")

package com.roxiemobile.kotlincommons.util

import com.roxiemobile.java.util.toByteArray
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test
import java.nio.ByteBuffer
import java.security.SecureRandom
import java.util.UUID

/**
 * A Base62 encoder/decoder tests.
 *
 * @see <a href="https://github.com/tuupola/base62/blob/2.1.0/tests/Base62Test.php">Base62Test.php</a>
 */
class Base62Test {

// MARK: - Tests

    @Test
    fun testShouldEncodeAndDecodeRandomBytes() {

        val data = SecureRandom().generateSeed(128)

        val encoded = Base62.SHARED.encode(data)

        val decoded = Base62.SHARED.decode(encoded)
        assertTrue(data.contentEquals(decoded))
    }

    @Test
    fun testShouldEncodeAndDecodeWithLeadingZero() {

        val data = "07d8e31da269bf28".decodeHex()

        val encoded = Base62.SHARED.encode(data)

        val decoded = Base62.SHARED.decode(encoded)
        assertTrue(data.contentEquals(decoded))
    }

    @Test
    fun testShouldUseDefaultCharacterSet() {

        val base62Coder = Base62.createInstanceWithGmpCharacterSet()

        if (true) {
            val data = "Hello world!".toByteArray()

            val encoded = base62Coder.encode(data)
            assertEquals(String(encoded), "T8dgcjRGuYUueWht")
        }

        if (true) {
            val data = "0000010203040506".decodeHex()

            val encoded = base62Coder.encode(data)
            assertEquals(String(encoded), "00JVb3WII")

            val decoded = base62Coder.decode(encoded)
            assertTrue(data.contentEquals(decoded))
        }
    }

    @Test
    fun testShouldUseInvertedCharacterSet() {

        val base62Coder = Base62.createInstanceWithInvertedCharacterSet()

        if (true) {
            val data = "Hello world!".toByteArray()

            val encoded = base62Coder.encode(data)
            assertEquals(String(encoded), "t8DGCJrgUyuUEwHT")
        }

        if (true) {
            val data = "0000010203040506".decodeHex()

            val encoded = base62Coder.encode(data)
            assertEquals(String(encoded), "00jvB3wii")

            val decoded = base62Coder.decode(encoded)
            assertTrue(data.contentEquals(decoded))
        }
    }

    @Test
    fun testShouldThrowExceptionOnDecodeInvalidData() {

        val invalid = "invalid~data-%@#!@*#-foo".toByteArray()

        assertThrows(IllegalArgumentException::class.java) {
            Base62.SHARED.decode(invalid)
        }
    }

    @Test
    fun testShouldEncodeAndDecodeSingleZeroByte() {

        val data = byteArrayOf(0x00)

        val encoded = Base62.SHARED.encode(data)
        assertEquals(String(encoded), "0")

        val decoded = Base62.SHARED.decode(encoded)
        assertTrue(data.contentEquals(decoded))
    }

    @Test
    fun testShouldEncodeAndDecodeMultipleZeroBytes() {

        val data = byteArrayOf(0x00, 0x00, 0x00)

        val encoded = Base62.SHARED.encode(data)
        assertEquals(String(encoded), "000")

        val decoded = Base62.SHARED.decode(encoded)
        assertTrue(data.contentEquals(decoded))
    }

    @Test
    fun testShouldEncodeAndDecodeSingleZeroBytePrefix() {

        val data = byteArrayOf(0x00, 0x01, 0x02)

        val encoded = Base62.SHARED.encode(data)
        assertEquals(String(encoded), "04A")

        val decoded = Base62.SHARED.decode(encoded)
        assertTrue(data.contentEquals(decoded))
    }

    @Test
    fun testShouldEncodeAndDecodeMultipleZeroBytePrefix() {

        val data = byteArrayOf(0x00, 0x00, 0x00, 0x01, 0x02)

        val encoded = Base62.SHARED.encode(data)
        assertEquals(String(encoded), "0004A")

        val decoded = Base62.SHARED.decode(encoded)
        assertTrue(data.contentEquals(decoded))
    }

    @Test
    fun testShouldEncodeAndDecodeRandomUUID() {

        val data = UUID.randomUUID().toByteArray()

        val encoded = Base62.SHARED.encode(data)

        val decoded = Base62.SHARED.decode(encoded)
        assertTrue(data.contentEquals(decoded))
    }

    @Test
    fun testShouldEncodeAndDecodeBadUUIDs() {

        val badUUIDs = mapOf(
            "0023a441-a3a3-4d9e-bd65-de3381c3a226" to "0GHs6XflJ51yCvZ4TwH4g",
            "1ee9a026-48ef-4592-9d87-88ceea7bc35e" to "wKXIE87UgfjIvSPLkAHao",
            "0e0aa2a8-1a10-45e4-a67a-c97b9c5a7d19" to "QUkgNC86JAY1A8JhVZ7iT",
            "1ad0d525-97c9-4c08-ad56-59acd47e3f7c" to "obEi3noEliUnbTQbhMrLo",
        )

        badUUIDs.forEach { (uuidString, base62String) ->

            val data = UUID.fromString(uuidString).toByteArray()

            val encoded = Base62.SHARED.encode(data)
            assertEquals(String(encoded), base62String)

            val decoded = Base62.SHARED.decode(encoded)
            assertEquals(uuidString, decoded.toUUID().toString())
        }
    }

// MARK: - Companion

    companion object {

        private fun String.decodeHex(): ByteArray {
            check(length % 2 == 0) { "Must have an even length" }

            return chunked(2)
                .map { it.toInt(16).toByte() }
                .toByteArray()
        }

        private fun ByteArray.toUUID(): UUID {
            return ByteBuffer.wrap(this).let {
                UUID(it.long, it.long)
            }
        }
    }
}
