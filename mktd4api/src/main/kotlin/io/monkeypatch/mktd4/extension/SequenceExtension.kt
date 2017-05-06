package io.monkeypatch.mktd4.extension

import java.util.*


inline fun <reified T> Sequence<T>.toTypedArray(): Array<T> = this.toList().toTypedArray()

fun <T> Collection<T>.nextRandomOrElse(default: () -> T): T = if (this.isEmpty()) default.invoke() else this.elementAt(Random().nextInt(this.size))

fun <T> Iterable<T>.asString(): String = this.joinToString(prefix = "[", postfix = "]")
