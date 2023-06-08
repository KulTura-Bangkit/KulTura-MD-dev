package academy.bangkit.kultura.ui.splash

import academy.bangkit.kultura.databinding.ActivitySplashBinding
import academy.bangkit.kultura.ui.dashboard.DashboardActivity
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val handler = Handler()
        handler.postDelayed({

            val intent = Intent(this@SplashActivity, DashboardActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)

        splashAnimation()
    }

    private fun splashAnimation() {
        ObjectAnimator.ofFloat(binding.logoSplash, View.TRANSLATION_Y, 0f, 0f).apply {
            duration = 1500
        }.start()

        ObjectAnimator.ofFloat(binding.logoSplash, View.ALPHA, 0f, 1f).apply {
            duration = 1500
        }.start()

        ObjectAnimator.ofFloat(binding.logoSplash, View.SCALE_X, 0f, 1f).apply {
            duration = 1500
        }.start()

        ObjectAnimator.ofFloat(binding.logoSplash, View.SCALE_Y, 0f, 1f).apply {
            duration = 1500
        }.start()
    }
}