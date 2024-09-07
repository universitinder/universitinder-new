package com.universitinder.app.file

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap

fun getFileExtensionFromUri(context: Context, uri: Uri): String? {
    val contentResolver: ContentResolver = context.contentResolver
    val mimeType = contentResolver.getType(uri)
    return if (mimeType != null) {
        MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
    } else {
        uri.lastPathSegment?.let {
            MimeTypeMap.getFileExtensionFromUrl(Uri.encode(it))
        }
    }
}
