package hu.bme.aut.aboutlicensedialogdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.mikepenz.aboutlibraries.LibsBuilder

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btnAbout).setOnClickListener {
            LibsBuilder()
                    .start(this)
        }

    }
}