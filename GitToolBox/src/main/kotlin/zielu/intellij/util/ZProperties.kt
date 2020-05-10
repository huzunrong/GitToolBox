package zielu.intellij.util

import java.util.concurrent.atomic.AtomicReference

internal fun <T> createRefProperty(initialValue: T): ZProperty<T> {
  return ZRefPropertyImpl(initialValue)
}

private class ZRefPropertyImpl<T>(
  initialValue: T
) : ZProperty<T> {
  private val valueStore: AtomicReference<T> = AtomicReference(initialValue)

  override var value: T
    get() = valueStore.get()
    set(value) {
      valueStore.set(value)
    }
}
