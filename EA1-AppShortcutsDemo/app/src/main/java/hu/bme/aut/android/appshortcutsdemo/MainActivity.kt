package hu.bme.aut.android.appshortcutsdemo

import android.app.PendingIntent
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addShortcut()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            addPinnedShortcut()
        }
    }

    private fun addShortcut() {
        val shortcutManager = getSystemService<ShortcutManager>(ShortcutManager::class.java)

        val shortcut = ShortcutInfo.Builder(applicationContext, "aut")
                .setShortLabel("Website")
                .setLongLabel("Open the website")
                .setIcon(Icon.createWithResource(applicationContext, R.drawable.baseline_language_black_24))
                .setIntent(Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.aut.bme.hu/")))
                .build()

        shortcutManager!!.dynamicShortcuts = Arrays.asList(shortcut)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun addPinnedShortcut(){
        val shortcutManager = getSystemService(ShortcutManager::class.java)

        if (shortcutManager!!.isRequestPinShortcutSupported) {
            val pinShortcutInfo = ShortcutInfo.Builder(applicationContext, "aut").build()

            val pinnedShortcutCallbackIntent = shortcutManager.createShortcutResultIntent(pinShortcutInfo)

            val successCallback = PendingIntent.getBroadcast(applicationContext, /* request code */ 0,
                    pinnedShortcutCallbackIntent, /* flags */ 0)

            shortcutManager.requestPinShortcut(pinShortcutInfo,
                    successCallback.intentSender)
        }

    }
}