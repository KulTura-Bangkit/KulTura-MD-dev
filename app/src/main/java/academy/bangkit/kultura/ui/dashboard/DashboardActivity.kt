package academy.bangkit.kultura.ui.dashboard

import academy.bangkit.kultura.R
import academy.bangkit.kultura.ui.home.HomeActivity
import academy.bangkit.kultura.ui.profile.ProfileActivity
import academy.bangkit.kultura.ui.upload.ApiConfig
import academy.bangkit.kultura.ui.upload.FileUploadResponse
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DashboardActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val btnMoveActivity1: ImageButton = findViewById(R.id.imageButton1)
        btnMoveActivity1.setOnClickListener(this)
        val btnMoveActivity2: ImageButton = findViewById(R.id.imageButton2)
        btnMoveActivity2.setOnClickListener(this)

        val cari:EditText = findViewById(R.id.Search)
        cari.setText(intent.getStringExtra("hasil"))
        cari.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                // This method is called before the text is changed.
            }

            override fun onTextChanged(
                charSequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                // This method is called when the text is changing.
                // Perform your action here.
                var entah: EditText= findViewById(R.id.Search)
                var service = ApiConfigs().getApiService().getData(entah.text.toString())
                service.enqueue(object : Callback<List<UserResponse.Item>> {
                    override fun onResponse(
                        call: Call<List<UserResponse.Item>>,
                        response: Response<List<UserResponse.Item>>
                    ) {
                        if (response.isSuccessful) {
                            val responseBody = response.body()
                            if (responseBody != null) {
                                //var cek:TextView = findViewById(R.id.textView3)
                                Log.d("Update",responseBody)


                            }
                        } else {
                            //Toast.makeText(this@DashboardActivity, response.message(), Toast.LENGTH_SHORT).show()

                        }
                    }
                    override fun onFailure(call: Call<List<UserResponse.Item>>, t: Throwable) {
                        //Toast.makeText(this@DashboardActivity, "Coba lagi", Toast.LENGTH_SHORT).show()

                    }
                })
            }
            override fun afterTextChanged(editable: Editable?) {
                // This method is called after the text has changed.
            }
        })

        val recyclerView: RecyclerView = findViewById(R.id.recyclerrecom)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

    }
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.imageButton1 -> {
                val moveIntent = Intent(this@DashboardActivity, HomeActivity::class.java)
                startActivity(moveIntent)
            }
            R.id.imageButton2 ->{
                val moveIntent = Intent(this@DashboardActivity, ProfileActivity::class.java)
                startActivity(moveIntent)
            }
        }
    }
}


