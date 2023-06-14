package academy.bangkit.kultura.adapter

import academy.bangkit.kultura.R
import academy.bangkit.kultura.ui.dashboard.UserResponse
import android.content.Context
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

    override fun onBindViewHolder(holder: RecommendAdapter.ViewHolder, position: Int) {
        val rvItem = listRec[position]
        holder.apply {
            Glide.with(itemView.context)
                .load(rvItem.url_image)
                .into(tvImg)
            tvName.text = rvItem.name

            holder.itemView.setOnClickListener {

            }
        }
    }

    override fun getItemCount() = listRec.size

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val tvImg : ImageView = view.findViewById(R.id.rv_batik_img)
        val tvName : TextView = view.findViewById(R.id.rv_batik_name)
    }
/*
    fun setData(data: List<UserResponse.Item>?){
        listRec.clear()
        listRec.addAll(data!!)
        notifyDataSetChanged()
    }
*/
}