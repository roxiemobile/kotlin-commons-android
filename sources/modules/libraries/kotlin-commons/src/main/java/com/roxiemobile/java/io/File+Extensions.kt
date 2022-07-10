@file:Suppress("RedundantVisibilityModifier", "unused")

package com.roxiemobile.java.io

import java.io.File

// MARK: - Extensions

/**
 * Tests whether the file does not exist.
 */
public fun File.notExists(): Boolean {
    return !exists()
}
