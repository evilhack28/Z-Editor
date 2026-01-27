package com.example.z_editor

import android.content.Context

object Translator {
    /**
     * Universal function to get any string by its key.
     * @param context: any Context (Activity, Fragment, Application)
     * @param key: the name of the string resource in strings.xml
     * @return the translated string or the key if not found
     */
    fun t(context: Context, key: String): String {
        val resId = context.resources.getIdentifier(key, "string", context.packageName)
        return if (resId != 0) context.getString(resId) else key
    }
}