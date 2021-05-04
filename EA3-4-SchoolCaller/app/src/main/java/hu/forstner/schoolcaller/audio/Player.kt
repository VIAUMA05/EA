package hu.forstner.schoolcaller.audio

import android.content.Context
import android.media.MediaPlayer
import hu.forstner.schoolcaller.R

class Player(context: Context) {
    private lateinit var callingEffectPlayer: MediaPlayer;

    init {
        callingEffectPlayer = MediaPlayer.create(context, R.raw.callingeffect)
    }

    fun playCallingEffect() {
        if (!callingEffectPlayer.isPlaying)
            callingEffectPlayer.start()
    }

    fun stop() {
        callingEffectPlayer.stop()
 
    }
}

