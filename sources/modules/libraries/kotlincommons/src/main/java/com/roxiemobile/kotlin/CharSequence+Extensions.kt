@file:Suppress("NOTHING_TO_INLINE", "OPT_IN_IS_NOT_ENABLED", "RedundantVisibilityModifier")

package com.roxiemobile.kotlin

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

/**
 * Returns `true` if this nullable char sequence is either `null` or not empty and contains
 * some characters except of whitespace characters.
 */
@OptIn(ExperimentalContracts::class)
public inline fun CharSequence?.isNullOrNotBlank(): Boolean {
    contract {
        returns(false) implies (this@isNullOrNotBlank != null)
    }

    return (this == null) || this.isNotBlank()
}

/**
 * Returns `true` if this nullable char sequence is either `null` or not empty.
 */
@OptIn(ExperimentalContracts::class)
public inline fun CharSequence?.isNullOrNotEmpty(): Boolean {
    contract {
        returns(false) implies (this@isNullOrNotEmpty != null)
    }

    return (this == null) || this.isNotEmpty()
}
