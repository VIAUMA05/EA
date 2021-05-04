package hu.forstner.posedetection.audio
import android.content.Context
import android.media.MediaPlayer
import hu.forstner.posedetection.R


class Player(context: Context) {
    private lateinit var jumpPlayer: MediaPlayer;
    private lateinit var cheesePlayer: MediaPlayer;
    private lateinit var handsPlayer: MediaPlayer;

    init {
        jumpPlayer = MediaPlayer.create(context, R.raw.hopp)
        cheesePlayer = MediaPlayer.create(context, R.raw.cheese)
        handsPlayer = MediaPlayer.create(context, R.raw.kezeketfel)
    }

    fun playJump()
    {
        //if(!jumpPlayer.isPlaying) / nem kell, nincs hatasa a startnak, ha true akkor sem
            jumpPlayer.start()
    }

    fun playCheese()
    {
        //if(!cheesePlayer.isPlaying)
            cheesePlayer.start()
    }

    fun playHandsUp()
    {
        //if(!handsPlayer.isPlaying)
            handsPlayer.start()
    }
}