package com.arvifox.arvi.utils

inline fun <T> T?.ifNull(supplier: () -> T): T {
  return this ?: supplier.invoke()
}

fun <T> Any.unsafeCast(): T {
  @Suppress("UNCHECKED_CAST")
  return this as T
}

inline fun <reified T> Any?.safeCast(): T? {
  return if (this != null && this is T) this else null
}

inline fun <T:Any, R> whenNotNull(input: T?, callback: (T)->R): R? {
  return input?.let(callback)
}