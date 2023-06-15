package academy.bangkit.kultura.ui.dashboard

import academy.bangkit.kultura.R
import academy.bangkit.kultura.adapter.RecommendAdapter
import academy.bangkit.kultura.databinding.ActivityDashboardBinding
import academy.bangkit.kultura.ui.home.HomeActivity
import academy.bangkit.kultura.ui.profile.ProfileActivity
import android.content.Intent
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DashboardActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityDashboardBinding
    private lateinit var adapter: RecommendAdapter
    private lateinit var searchView: SearchView
    private lateinit var itemList: List<UserResponse.Item>
    private var filteredList: MutableList<UserResponse.Item> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = RecommendAdapter(listOf())

        val btnMoveActivity1: ImageButton = findViewById(R.id.imageButton1)
        btnMoveActivity1.setOnClickListener(this)
        val btnMoveActivity2: ImageButton = findViewById(R.id.imageButton2)
        btnMoveActivity2.setOnClickListener(this)

        binding.recyclerrecom.adapter = adapter
        binding.recyclerrecom.setHasFixedSize(true)

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerrecom.layoutManager = layoutManager

        remoteGetRecList()

        itemList = mutableListOf()

        searchView = findViewById(R.id.Search)
        val searchEditText = searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
        searchEditText.setHintTextColor(ContextCompat.getColor(this, android.R.color.black))
        searchEditText.setTextColor(ContextCompat.getColor(this, android.R.color.black))
        val searchIcon = searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_mag_icon)
        searchIcon.setColorFilter(ContextCompat.getColor(this, android.R.color.black), PorterDuff.Mode.SRC_IN)
        val searchCloseButton = searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_close_btn)
        searchCloseButton.setColorFilter(ContextCompat.getColor(this, android.R.color.black), PorterDuff.Mode.SRC_IN)


        val searchText = intent.getStringExtra("hasil")
        searchView.setQuery(searchText, false)
        searchView.clearFocus()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    performSearch(it)
                }
                return true
            }
        })
/*
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
            ) { /*
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
                    //            Log.d("Update",responseBody)


                            }
                        } else {
                            //Toast.makeText(this@DashboardActivity, response.message(), Toast.LENGTH_SHORT).show()

                        }
                    }
                    override fun onFailure(call: Call<List<UserResponse.Item>>, t: Throwable) {
                        //Toast.makeText(this@DashboardActivity, "Coba lagi", Toast.LENGTH_SHORT).show()

                    }
                })
                */
            }
            override fun afterTextChanged(editable: Editable?) {
                // This method is called after the text has changed.
            }
        })
*/
        val recyclerView: RecyclerView = findViewById(R.id.recyclerrecom)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

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

    fun remoteGetRecList() {
        val client = ApiConfigs().getApiService().batikList()
        client.enqueue(object : Callback<List<UserResponse.Item>> {
                override fun onResponse(
                    call: Call<List<UserResponse.Item>>,
                    response: Response<List<UserResponse.Item>>
                ) {
                    if (response.isSuccessful) {
                        val data = response.body()
                        data?.let {
                            setDataToAdapter(it)
                        }
                    }
                }
                override fun onFailure(call: Call<List<UserResponse.Item>>, t: Throwable) {
                    Log.d("error", "" + t.stackTraceToString())
                }

            })
    }
    fun setDataToAdapter(data: List<UserResponse.Item>) {
        itemList = data
        adapter.setItems(itemList)
    }

    private fun performSearch(query: String?) {
        filteredList.clear()
        query?.let {
            if (it.isNotEmpty()) {
                for (item in itemList) {
                    item.name?.let { name ->
                        if (name.contains(it, true)) {
                            filteredList.add(item)
                        }
                    }
                }
            } else {
                Log.d("error", "data ga ada coyy")
                filteredList.addAll(itemList)
            }
        }
        adapter.setItems(filteredList)
    }
}





