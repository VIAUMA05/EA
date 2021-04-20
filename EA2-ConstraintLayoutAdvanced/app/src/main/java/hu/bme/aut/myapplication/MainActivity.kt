package hu.bme.aut.myapplication

import android.animation.ValueAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.Button
import androidx.constraintlayout.helper.widget.Layer
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Group

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_circular)

        //findViewById<Group>(R.id.group).visibility = View.INVISIBLE

        // activity_chain-hez
        /*findViewById<Button>(R.id.button12).setOnClickListener {
            ValueAnimator.ofFloat(0F, 360F)
                .apply {
                    addUpdateListener { animator ->
                        findViewById<Layer>(R.id.layer2).rotation = animator.animatedValue as Float
                    }
                    duration = 2000 // in ms = 2 sec
                    start()
                }
        }*/

        // activity_circular-hoz

        findViewById<Button>(R.id.btnEarth).setOnClickListener {
            val anim = ValueAnimator.ofFloat(0f, 360f)
            anim.addUpdateListener { valueAnimator ->
                val animatedValue = valueAnimator.animatedValue as Float
                val btnMoon = findViewById<Button>(R.id.btnMoon)
                val layoutParams = btnMoon.layoutParams as ConstraintLayout.LayoutParams
                layoutParams.circleAngle = animatedValue
                btnMoon.layoutParams = layoutParams

                //btnMoon.rotation = (animatedValue % 360 - 270)
            }
            anim.duration = 3000

            anim.interpolator = LinearInterpolator()
            anim.start()
        }
    }
}
