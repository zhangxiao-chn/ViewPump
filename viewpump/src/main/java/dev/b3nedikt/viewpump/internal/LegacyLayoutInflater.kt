package dev.b3nedikt.viewpump.internal

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import java.lang.reflect.Field

/**
 * Wrapper around [LayoutInflater] for android versions < Q
 */
internal class LegacyLayoutInflater(
    original: LayoutInflater,
    newContext: Context
) : LayoutInflater(original, newContext) {

  override fun cloneInContext(newContext: Context): LayoutInflater {
    return LegacyLayoutInflater(this, newContext)
  }

  /**
   * Creates a view, legacy version of the [createView] method of [LayoutInflater] added in
   * android Q that takes a context.
   *
   * @param viewContext The context used as the context parameter of the View constructor. Theme
   * attributes will get retrieved from this
   * @param name Tag name to be inflated.
   * @param attrs Inflation attributes as specified in XML file.
   */
  fun createViewLegacy(
      viewContext: Context,
      name: String,
      attrs: AttributeSet
  ): View? {
    CONSTRUCTOR_ARGS_FIELD.set(this, viewContext)
    return createView(name, null, attrs)
  }

  private companion object {
    private val CONSTRUCTOR_ARGS_FIELD: Field by lazy {
      requireNotNull(LayoutInflater::class.java.getDeclaredField("mConstructorArgs")) {
        "No constructor arguments field found in LayoutInflater!"
      }.apply { isAccessible = true }
    }
  }
}