package hu.bme.aut.nearbymessagedemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.messages.Message
import com.google.android.gms.nearby.messages.MessageListener
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var messageListener: MessageListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        messageListener = object: MessageListener() {
            override fun onFound(msg: Message?) {
                tvData.append("${String(msg!!.getContent())}\n")
            }

            override fun onLost(msg: Message?) {
                tvData.append("lost ${String(msg!!.getContent())}\n")
            }
        }

        btnSend.setOnClickListener {
            var text: String = Date(System.currentTimeMillis()).toString()
            Nearby.getMessagesClient(this).publish(Message(text.toByteArray()))
        }
    }

    override fun onStart() {
        super.onStart()
        Nearby.getMessagesClient(this).subscribe(messageListener)
    }

    override fun onStop() {
        super.onStop()
        Nearby.getMessagesClient(this).unsubscribe(messageListener)
    }
}

