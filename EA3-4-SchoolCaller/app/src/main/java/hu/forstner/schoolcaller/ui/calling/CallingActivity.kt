package hu.forstner.schoolcaller.ui.calling

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator.*
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.coroutineScope
import hu.forstner.schoolcaller.R
import hu.forstner.schoolcaller.audio.Player
import hu.forstner.schoolcaller.data.model.PeopleRepository
import hu.forstner.schoolcaller.databinding.ActivityCallingBinding
import hu.forstner.schoolcaller.ui.videocall.VideoCallActivity
import hu.forstner.schoolcaller.util.Global
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random


class CallingActivity : AppCompatActivity() {

    private lateinit var viewModel: CallingViewModel
    private lateinit var binding: ActivityCallingBinding
    private val maxCallingDelayMS = 11000
    private val maxShortCallingDelayMS = 3000


    var position = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCallingBinding.inflate(layoutInflater)
        viewModel = ViewModelProviders.of(this).get(CallingViewModel::class.java)
        setContentView(binding.root)
        setTitle(getString(R.string.titleCalling));

        val b = intent.extras
        if (b != null) position = b.getInt("position")

        val person =PeopleRepository().list[position]
        binding.imgHead.setImageDrawable(this.resources.getDrawable(person.resId))
        binding.tvName.setText(person.name)

        val moveRightAnim = ObjectAnimator.ofFloat(binding.imgRightArrow, "translationX", 200.0f, 400.0f).apply {
            duration = 1200
            repeatCount=INFINITE
            repeatMode = RESTART
        }
        val fadeRightAnim = ObjectAnimator.ofFloat(binding.imgRightArrow, "alpha", 1f, 0f).apply {
            duration = 1200
            repeatCount=INFINITE
            repeatMode = RESTART
        }
        val moveLeftAnim = ObjectAnimator.ofFloat(binding.imgLeftArrow, "translationX", -200.0f, -400.0f).apply {
            duration = 1200
            repeatCount=INFINITE
            repeatMode = RESTART
        }
        val fadeLeftAnim = ObjectAnimator.ofFloat(binding.imgLeftArrow, "alpha", 1f, 0f).apply {
            duration = 1200
            repeatCount=INFINITE
            repeatMode = RESTART
        }

        val combinedAnimation = AnimatorSet().apply {
            play(moveRightAnim).with(fadeRightAnim).with(moveLeftAnim).with(fadeLeftAnim)
            start()
        }


        val  player = Player(this)
        player.playCallingEffect()

        binding.fabEndCall.setOnClickListener {
            player.stop()
            finishAfterTransition()
        }

        lifecycle.coroutineScope.launch(Dispatchers.Main) {
            val latency : Long = if(Global.callCount>2)
                2000L+ Random.nextInt(maxShortCallingDelayMS) else
                    2000L+Random.nextInt(maxCallingDelayMS)
            delay(latency);
            Global.callCount++
            player.stop()
            startVideoCallActivity()
        }



    }

    fun startVideoCallActivity() {
        val videoCallIntent = Intent(this, VideoCallActivity::class.java)


        val b = Bundle().also {
            it.putInt("position", position)
            videoCallIntent.putExtras(it)
        }

        finish()
        startActivity(videoCallIntent)
    }


}


