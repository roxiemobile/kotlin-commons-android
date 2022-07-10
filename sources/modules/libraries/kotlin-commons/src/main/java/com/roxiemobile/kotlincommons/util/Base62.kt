@file:Suppress("MemberVisibilityCanBePrivate")

package com.roxiemobile.kotlincommons.util

import java.io.ByteArrayOutputStream
import kotlin.math.ceil
import kotlin.math.ln

/**
 * A Base62 encoder/decoder.
 *
 * @see <a href="https://github.com/seruco/base62/blob/0.1.3/src/main/java/io/seruco/encoding/base62/Base62.java">Base62.java</a>
 */
class Base62 {

// MARK: - Construction

    private constructor(alphabet: ByteArray) {
        _alphabet = alphabet
        _lookup = createLookupTable(alphabet)
    }

// MARK: - Methods

    /**
     * Encodes a sequence of bytes in Base62 encoding.
     *
     * @param bytes a byte sequence.
     * @return a sequence of Base62-encoded bytes.
     */
    fun encode(bytes: ByteArray): ByteArray {
        val indices = convert(bytes, Bases.STANDARD, Bases.TARGET)
        return translate(indices, _alphabet)
    }

    /**
     * Decodes a sequence of Base62-encoded bytes.
     *
     * @param bytes a sequence of Base62-encoded bytes.
     * @return a byte sequence.
     * @throws IllegalArgumentException when `encoded` is not encoded over the Base62 alphabet.
     */
    fun decode(bytes: ByteArray): ByteArray {
        require(isBase62Encoding(bytes)) { "Input is not encoded correctly" }

        val prepared = translate(bytes, _lookup)
        return convert(prepared, Bases.TARGET, Bases.STANDARD)
    }

    /**
     * Checks whether a sequence of bytes is encoded over a Base62 alphabet.
     *
     * @param bytes a sequence of bytes.
     * @return `true` when the bytes are encoded over a Base62 alphabet, `false` otherwise.
     */
    fun isBase62Encoding(bytes: ByteArray): Boolean {

        return bytes.all { byte ->
            // @formatter:off
            IntRange('A'.code, 'Z'.code).contains(byte) ||
            IntRange('a'.code, 'z'.code).contains(byte) ||
            IntRange('0'.code, '9'.code).contains(byte)
            // @formatter:on
        }
    }

// MARK: - Private Methods

    /**
     * Creates the lookup table from character to index of character in character set.
     */
    private fun createLookupTable(alphabet: ByteArray): ByteArray {
        val lookup = ByteArray(256)

        alphabet.forEachIndexed { index, byte ->
            lookup[byte.toInt()] = (index and 0xFF).toByte()
        }

        return lookup
    }

    /**
     * Uses the elements of a byte array as indices to a dictionary and returns the corresponding values
     * in form of a byte array.
     */
    private fun translate(indices: ByteArray, dictionary: ByteArray): ByteArray {
        val translation = ByteArray(indices.size)

        indices.forEachIndexed { index, byte ->
            translation[index] = dictionary[byte.toInt()]
        }

        return translation
    }

// MARK: - Private Methods

    /**
     * Converts a byte array from a source base to a target base using the alphabet.
     */
    private fun convert(message: ByteArray, sourceBase: Int, targetBase: Int): ByteArray {

        // This algorithm is inspired by:
        // @link http://codegolf.stackexchange.com/a/21672

        val estimatedLength = estimateOutputLength(message.size, sourceBase, targetBase)
        val output = ByteArrayOutputStream(estimatedLength)

        var source = message

        while (source.isNotEmpty()) {

            val quotient = ByteArrayOutputStream(source.size)
            var remainder = 0

            source.forEach { byte ->

                val accumulator = (byte.toInt() and 0xFF) + remainder * sourceBase
                val digit = (accumulator - (accumulator % targetBase)) / targetBase

                remainder = accumulator % targetBase

                if (quotient.size() > 0 || digit > 0) {
                    quotient.write(digit)
                }
            }

            output.write(remainder)

            source = quotient.toByteArray()
        }

        // Pad output with zeroes corresponding to the number of leading zeroes in the message
        for (index in 0 until (message.size - 1)) {

            if (message[index] != 0.toByte()) {
                break
            }
            output.write(0)
        }

        return output.toByteArray().reversedArray()
    }

    /**
     * Estimates the length of the output in bytes.
     */
    private fun estimateOutputLength(inputLength: Int, sourceBase: Int, targetBase: Int): Int {
        return ceil(ln(sourceBase.toDouble()) / ln(targetBase.toDouble()) * inputLength).toInt()
    }

// MARK: - Constants

    private object Bases {
        const val STANDARD = 256
        const val TARGET = 62
    }

    private object CharacterSets {

        val GMP = byteArrayOf(
            '0'.code.toByte(), '1'.code.toByte(), '2'.code.toByte(), '3'.code.toByte(), '4'.code.toByte(),
            '5'.code.toByte(), '6'.code.toByte(), '7'.code.toByte(), '8'.code.toByte(), '9'.code.toByte(),
            'A'.code.toByte(), 'B'.code.toByte(), 'C'.code.toByte(), 'D'.code.toByte(), 'E'.code.toByte(),
            'F'.code.toByte(), 'G'.code.toByte(), 'H'.code.toByte(), 'I'.code.toByte(), 'J'.code.toByte(),
            'K'.code.toByte(), 'L'.code.toByte(), 'M'.code.toByte(), 'N'.code.toByte(), 'O'.code.toByte(),
            'P'.code.toByte(), 'Q'.code.toByte(), 'R'.code.toByte(), 'S'.code.toByte(), 'T'.code.toByte(),
            'U'.code.toByte(), 'V'.code.toByte(), 'W'.code.toByte(), 'X'.code.toByte(), 'Y'.code.toByte(),
            'Z'.code.toByte(), 'a'.code.toByte(), 'b'.code.toByte(), 'c'.code.toByte(), 'd'.code.toByte(),
            'e'.code.toByte(), 'f'.code.toByte(), 'g'.code.toByte(), 'h'.code.toByte(), 'i'.code.toByte(),
            'j'.code.toByte(), 'k'.code.toByte(), 'l'.code.toByte(), 'm'.code.toByte(), 'n'.code.toByte(),
            'o'.code.toByte(), 'p'.code.toByte(), 'q'.code.toByte(), 'r'.code.toByte(), 's'.code.toByte(),
            't'.code.toByte(), 'u'.code.toByte(), 'v'.code.toByte(), 'w'.code.toByte(), 'x'.code.toByte(),
            'y'.code.toByte(), 'z'.code.toByte(),
        )

        val INVERTED = byteArrayOf(
            '0'.code.toByte(), '1'.code.toByte(), '2'.code.toByte(), '3'.code.toByte(), '4'.code.toByte(),
            '5'.code.toByte(), '6'.code.toByte(), '7'.code.toByte(), '8'.code.toByte(), '9'.code.toByte(),
            'a'.code.toByte(), 'b'.code.toByte(), 'c'.code.toByte(), 'd'.code.toByte(), 'e'.code.toByte(),
            'f'.code.toByte(), 'g'.code.toByte(), 'h'.code.toByte(), 'i'.code.toByte(), 'j'.code.toByte(),
            'k'.code.toByte(), 'l'.code.toByte(), 'm'.code.toByte(), 'n'.code.toByte(), 'o'.code.toByte(),
            'p'.code.toByte(), 'q'.code.toByte(), 'r'.code.toByte(), 's'.code.toByte(), 't'.code.toByte(),
            'u'.code.toByte(), 'v'.code.toByte(), 'w'.code.toByte(), 'x'.code.toByte(), 'y'.code.toByte(),
            'z'.code.toByte(), 'A'.code.toByte(), 'B'.code.toByte(), 'C'.code.toByte(), 'D'.code.toByte(),
            'E'.code.toByte(), 'F'.code.toByte(), 'G'.code.toByte(), 'H'.code.toByte(), 'I'.code.toByte(),
            'J'.code.toByte(), 'K'.code.toByte(), 'L'.code.toByte(), 'M'.code.toByte(), 'N'.code.toByte(),
            'O'.code.toByte(), 'P'.code.toByte(), 'Q'.code.toByte(), 'R'.code.toByte(), 'S'.code.toByte(),
            'T'.code.toByte(), 'U'.code.toByte(), 'V'.code.toByte(), 'W'.code.toByte(), 'X'.code.toByte(),
            'Y'.code.toByte(), 'Z'.code.toByte(),
        )
    }

// MARK: - Companion

    companion object {

        /**
         * A [Base62] instance using the GMP-style character set.
         */
        val SHARED: Base62 = createInstance()

        /**
         * Creates a [Base62] instance. Defaults to the GMP-style character set.
         *
         * @return a [Base62] instance.
         */
        fun createInstance(): Base62 {
            return createInstanceWithGmpCharacterSet()
        }

        /**
         * Creates a [Base62] instance using the GMP-style character set.
         *
         * @return a [Base62] instance.
         */
        fun createInstanceWithGmpCharacterSet(): Base62 {
            return Base62(CharacterSets.GMP)
        }

        /**
         * Creates a [Base62] instance using the inverted character set.
         *
         * @return a [Base62] instance.
         */
        fun createInstanceWithInvertedCharacterSet(): Base62 {
            return Base62(CharacterSets.INVERTED)
        }
    }

// MARK: - Variables

    private val _alphabet: ByteArray

    private val _lookup: ByteArray
}
