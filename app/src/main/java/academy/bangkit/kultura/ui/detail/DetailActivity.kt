package academy.bangkit.kultura.ui.detail

import academy.bangkit.kultura.R
import academy.bangkit.kultura.databinding.ActivityDetailBinding
import academy.bangkit.kultura.ui.dashboard.UserResponse
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra(EXTRA_NAME)
        val img = intent.getStringExtra(EXTRA_IMG)
        val desc = intent.getStringExtra(EXTRA_DESC)
        val shop = intent.getStringExtra(EXTRADATA)

        binding.detailName.text = name
        binding.detailDesc.text = desc
        Glide.with(this)
            .load(img)
            .into(binding.detailImg)

        val shopBtn = findViewById<FloatingActionButton>(R.id.shop_btn)
        shopBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(shop)
            startActivity(intent)
        }

    }
    companion object{
        const val EXTRADATA = "extra data"
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_IMG = "extra_img"
        const val EXTRA_DESC = "extra_desc"
    }
}