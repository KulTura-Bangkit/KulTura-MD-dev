package academy.bangkit.kultura.adapter

import academy.bangkit.kultura.R
import academy.bangkit.kultura.ui.dashboard.UserResponse
import academy.bangkit.kultura.ui.detail.DetailActivity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class RecommendAdapter(private val listRec: List<UserResponse.Item>) : RecyclerView.Adapter<RecommendAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_recomendation, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val rvItem = listRec[position]
        holder.apply {
            Glide.with(itemView.context)
                .load(rvItem.url_image)
                .into(tvImg)
            tvName.text = rvItem.name
            holder.itemView.setOnClickListener {
                val acontext = holder.itemView.context
                val sendName = rvItem.name
                val moveToDetail = Intent(acontext, DetailActivity::class.java).apply {
                    putExtra(DetailActivity.EXTRA_NAME, rvItem.name)
                    putExtra(DetailActivity.EXTRA_DESC, rvItem.description)
                    putExtra(DetailActivity.EXTRA_IMG, rvItem.url_image)
                    putExtra(DetailActivity.EXTRADATA, rvItem.url_product)
                }
                acontext.startActivity(moveToDetail)
            }
        }
    }

    override fun getItemCount() = listRec.size

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val tvImg : ImageView = view.findViewById(R.id.rv_batik_img)
        val tvName : TextView = view.findViewById(R.id.rv_batik_name)
    }
}