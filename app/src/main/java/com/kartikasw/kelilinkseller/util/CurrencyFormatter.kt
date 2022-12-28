package com.kartikasw.kelilinkseller.util

import java.text.NumberFormat
import java.util.*

fun Int.withCurrencyFormat(): String =
    NumberFormat.getCurrencyInstance(Locale("in", "ID")).format(this)