package com.example.antoanbmtt.helper

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment

fun Fragment.showToast(message : String){
    Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
}
fun Fragment.copyToClipboard(message : String){
    val manager = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
    val clipData = ClipData.newPlainText("label",message)
    if (manager != null) {
        manager.setPrimaryClip(clipData);
        Toast.makeText(context, "Text copied to clipboard", Toast.LENGTH_SHORT).show();
    } else {
        Toast.makeText(context, "Failed to access clipboard", Toast.LENGTH_SHORT).show();
    }
}