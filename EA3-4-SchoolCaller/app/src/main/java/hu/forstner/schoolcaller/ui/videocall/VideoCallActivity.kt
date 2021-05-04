package hu.forstner.schoolcaller.ui.videocall

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import hu.forstner.schoolcaller.R
import hu.forstner.schoolcaller.data.model.People
import hu.forstner.schoolcaller.data.model.PeopleRepository
import hu.forstner.schoolcaller.databinding.ActivityVideoCallBinding
import hu.forstner.schoolcaller.ui.calling.CallingViewModel

class VideoCallActivity : YouTubeBaseActivity(), YouTubePlayer.OnInitializedListener {

    private lateinit var binding: ActivityVideoCallBinding
    var position = 0
    lateinit var person : People

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVideoCallBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTitle(getString(R.string.titleVideoCall));

        val b = intent.extras
        if (b != null) position = b.getInt("position")

        person = PeopleRepository().list[position]
        binding.youtubeView.initialize("TEDD IDE A SAJAT GOOGLE YOUTUBE API KULCSODAT", this)

        binding.tvName.setText(person.name)
        binding.imgHead.setImageDrawable(this.resources.getDrawable(person.resId))

        binding.fabEndCall.setOnClickListener({
            finishAfterTransition()
        })

    }

    override fun onInitializationSuccess(
        provider: YouTubePlayer.Provider?,
        youTubePlayer: YouTubePlayer?,
        wasRestored: Boolean
    ) {
        if (!wasRestored) {
            youTubePlayer?.loadVideo(person.youtubeId)
            youTubePlayer?.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);
        }
        else
            youTubePlayer?.play()

        youTubePlayer?.setPlayerStateChangeListener(playerStateChangeListener)
    }

    override fun onInitializationFailure(
        p0: YouTubePlayer.Provider?,
        p1: YouTubeInitializationResult?
    ) {
        Toast.makeText(this@VideoCallActivity, "Init error", Toast.LENGTH_SHORT).show()
    }



    val playerStateChangeListener = object : YouTubePlayer.PlayerStateChangeListener {
        override fun onAdStarted() {
            //Toast.makeText(this@VideoCallActivity, "Click on ad!", Toast.LENGTH_SHORT).show()
        }

        override fun onLoading() {

        }

        override fun onVideoStarted() {
            //Toast.makeText(this@VideoCallActivity, "Video started", Toast.LENGTH_SHORT).show()
        }

        override fun onLoaded(p0: String?) {

        }

        override fun onVideoEnded() {
            finish()
        }

        override fun onError(p0: YouTubePlayer.ErrorReason?) {
            Toast.makeText(this@VideoCallActivity, "Hiba: $p0", Toast.LENGTH_SHORT).show()

        }
    }
}