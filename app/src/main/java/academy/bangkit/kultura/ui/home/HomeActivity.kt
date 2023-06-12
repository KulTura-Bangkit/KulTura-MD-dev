package academy.bangkit.kultura.ui.home

import academy.bangkit.kultura.R
import academy.bangkit.kultura.ui.upload.UploadPhotoActivity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton

class HomeActivity : AppCompatActivity(), View.OnClickListener  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val btnMoveActivity3: ImageButton = findViewById(R.id.btn_home_batik)
        btnMoveActivity3.setOnClickListener(this)
        val btnMoveActivity4: ImageButton = findViewById(R.id.btn_home_food)
        btnMoveActivity4.setOnClickListener(this)
        val btnMoveActivity5: ImageButton = findViewById(R.id.btn_home_house)
        btnMoveActivity5.setOnClickListener(this)
    }
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_home_batik -> {
                val moveIntent = Intent(this@HomeActivity, UploadPhotoActivity::class.java)
                moveIntent.putExtra("pilihan","batik")
                startActivity(moveIntent)
            }
            R.id.btn_home_food ->{
                val moveIntent = Intent(this@HomeActivity, UploadPhotoActivity::class.java)
                moveIntent.putExtra("pilihan","makanan")
                startActivity(moveIntent)
            }
            R.id.btn_home_house ->{
                val moveIntent = Intent(this@HomeActivity, UploadPhotoActivity::class.java)
                moveIntent.putExtra("pilihan","bangunan")
                startActivity(moveIntent)
            }
        }
    }
}