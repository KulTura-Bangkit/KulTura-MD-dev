package academy.bangkit.kultura.ui.upload

import academy.bangkit.kultura.R
import academy.bangkit.kultura.databinding.ActivityUploadPhotoBinding
import academy.bangkit.kultura.ui.dashboard.DashboardActivity
import academy.bangkit.kultura.ui.dashboard.UserResponse
import academy.bangkit.kultura.ui.detail.DetailActivity
import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.exifinterface.media.ExifInterface
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*
import java.text.FieldPosition
import java.text.SimpleDateFormat
import java.util.*

class UploadPhotoActivity() : AppCompatActivity() {

    private lateinit var binding: ActivityUploadPhotoBinding
    private lateinit var photoPath : String
    private var getFile: File? = null
    private lateinit var hasil: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val judul: TextView = findViewById(R.id.textView4)
        hasil = intent.getStringExtra("pilihan")!!
        judul.text = "Analisis sebuah " + hasil + " !"


        binding.buttonCamera.setOnClickListener {
            takePhoto()
        }

        binding.buttonGallery.setOnClickListener {
            openGallery()
        }

        binding.buttonAnalyze.setOnClickListener{
            uploadImage()
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun takePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createCustomTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@UploadPhotoActivity,
                "academy.bangkit.kultura",
                it
            )
            photoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(photoPath)
            getFile = reduceFileImage(myFile)
            val result = BitmapFactory.decodeFile(getFile?.path)
            binding.imageView2.setImageBitmap(result)

        }
    }

    private fun openGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, getString(R.string.pick_picture))
        launcherIntentGallery.launch(chooser)
    }

    private fun createCustomTempFile(context: Context): File {
        val storageLoc: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val time: String = SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis())
        return File.createTempFile(time, "jpg", storageLoc)

    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri

            selectedImg.let { uri ->
                val myFile = uriToFile(uri, this@UploadPhotoActivity)
                getFile = myFile
                binding.imageView2.setImageURI(uri)
            }
        }
    }

    private fun uriToFile(selectedImg: Uri, context: Context): File {
        val contentResolver: ContentResolver = context.contentResolver
        val myFile = createCustomTempFile(context)

        val inputStream = contentResolver.openInputStream(selectedImg) as InputStream
        val outputStream: OutputStream = FileOutputStream(myFile)
        val buf = ByteArray(1024)
        var len: Int
        while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
        outputStream.close()
        inputStream.close()

        return myFile
    }

    fun reduceFileImage(file: File): File {
        val bitmap = BitmapFactory.decodeFile(file.path)
        val ei = ExifInterface(file.path)

        val orientation: Int = ei.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_UNDEFINED
        )

        var check = orientation
        if(android.os.Build.VERSION.SDK_INT < 29){
            check = 6969
            Log.d("info info", android.os.Build.VERSION_CODES.R.toString())
        }
        Log.d("info 1", android.os.Build.VERSION_CODES.R.toString())
        val rotatedBitmap = when (check) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(bitmap, 90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(bitmap, 180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(bitmap, 270f)
            ExifInterface.ORIENTATION_NORMAL -> bitmap
            6969 -> rotateImage(bitmap, 90f)
            else ->  bitmap
        }


        var compressQuality = 100
        var streamLength: Int

        do {
            val bmpStream = ByteArrayOutputStream()
            rotatedBitmap?.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
            val bmpPicByteArray = bmpStream.toByteArray()
            streamLength = bmpPicByteArray.size
            compressQuality -= 5
        } while (streamLength > 1000000)

        rotatedBitmap?.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))

        return file
    }

    fun rotateImage(source: Bitmap, angle: Float): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(
            source, 0, 0, source.width, source.height,
            matrix, true
        )
    }

    private fun uploadImage() {
        if (getFile != null) {
            val file = getFile as File

            val requestImageFile = file.asRequestBody("image/jpg".toMediaType())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "image",
                file.name,
                requestImageFile
            )
            val opsi1: Button = findViewById(R.id.option1)
            opsi1.isVisible = false
            val opsi2: Button = findViewById(R.id.option2)
            opsi2.isVisible = false
            val opsi3: Button = findViewById(R.id.option3)
            opsi3.isVisible = false

            var service = ApiConfig().getApiService().uploadImage(imageMultipart)

            if(hasil == "batik"){

            }else if (hasil == "makanan"){
                service = ApiConfig().getApiService().uploadImage2(imageMultipart)
            }else if (hasil == "bangunan"){
                service = ApiConfig().getApiService().uploadImage3(imageMultipart)
            }


            Toast.makeText(this@UploadPhotoActivity, "Uploading Image", Toast.LENGTH_SHORT).show()
            val opsi4: ProgressBar = findViewById(R.id.progressBar)
            opsi4.isVisible = true

            service.enqueue(object : Callback<FileUploadResponse> {
                override fun onResponse(
                    call: Call<FileUploadResponse>,
                    response: Response<FileUploadResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            opsi4.isVisible = false
                            val opsi1: Button = findViewById(R.id.option1)
                            opsi1.text = responseBody.percent_1.toString() + "% - " + responseBody.label_1
                            opsi1.setOnClickListener{
                                //navigateToDetailActivity(responseBody.label_1)
                                val pindah = Intent(this@UploadPhotoActivity, DashboardActivity::class.java)
                                    pindah.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                    pindah.putExtra("hasil", responseBody.label_1)
                                    startActivity(pindah)
                            }
                            opsi1.isVisible = true
                            val opsi2: Button = findViewById(R.id.option2)
                            opsi2.text = responseBody.percent_2.toString() + "% - " + responseBody.label_2
                            opsi2.setOnClickListener{
                                //navigateToDetailActivity(responseBody.label_2)
                                val pindah = Intent(this@UploadPhotoActivity, DashboardActivity::class.java)
                                    pindah.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                    pindah.putExtra("hasil", responseBody.label_2)
                                    startActivity(pindah)

                            }
                            opsi2.isVisible = true
                            val opsi3: Button = findViewById(R.id.option3)
                            opsi3.text = responseBody.percent_3.toString() + "% - " + responseBody.label_3
                            opsi3.setOnClickListener{
                                //navigateToDetailActivity(responseBody.label_3)
                                val pindah = Intent(this@UploadPhotoActivity, DashboardActivity::class.java)
                                    pindah.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                    pindah.putExtra("hasil", responseBody.label_3)
                                    startActivity(pindah)
                            }
                            opsi3.isVisible = true
                            Toast.makeText(this@UploadPhotoActivity,"Hasil Analisis sudah ditampilkan", Toast.LENGTH_SHORT).show()

                        }
                    } else {
                        Toast.makeText(this@UploadPhotoActivity, response.message(), Toast.LENGTH_SHORT).show()
                        opsi4.isVisible = false
                    }
                }
                override fun onFailure(call: Call<FileUploadResponse>, t: Throwable) {
                    Toast.makeText(this@UploadPhotoActivity, "Gagal instance Retrofit", Toast.LENGTH_SHORT).show()
                    opsi4.isVisible = false
                }
            })
        } else {
            Toast.makeText(this@UploadPhotoActivity, "Silakan masukkan berkas gambar terlebih dahulu.", Toast.LENGTH_SHORT).show()
            val opsi4: ProgressBar = findViewById(R.id.progressBar)
            opsi4.isVisible = false
        }
    }

    private fun navigateToDetailActivity(label: String) {
        val img = intent.getStringExtra(DetailActivity.EXTRA_IMG)
        val desc = intent.getStringExtra(DetailActivity.EXTRA_DESC)
        val shop = intent.getStringExtra(DetailActivity.EXTRADATA)
        val intent = Intent(this@UploadPhotoActivity, DetailActivity::class.java).apply {
            putExtra(DetailActivity.EXTRA_NAME, label)
            putExtra(DetailActivity.EXTRA_IMG, img)
            putExtra(DetailActivity.EXTRA_DESC, desc)
            putExtra(DetailActivity.EXTRADATA, shop)
        }
        startActivity(intent)
    }
    companion object{
        const val FILENAME_FORMAT = "dd-MMM-yyyy"
    }
}