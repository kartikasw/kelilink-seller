package com.example.kelilinkseller.util

import java.text.NumberFormat
import java.util.*

fun Int.withCurrencyFormat(): String =
    NumberFormat.getCurrencyInstance(Locale("in", "ID")).format(this)