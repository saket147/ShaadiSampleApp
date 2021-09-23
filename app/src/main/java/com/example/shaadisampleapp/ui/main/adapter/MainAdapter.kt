package com.example.shaadisampleapp.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.shaadisampleapp.R
import com.example.shaadisampleapp.network.model.Results
import com.example.shaadisampleapp.ui.main.interfaces.VideoItemClickInterface
import kotlinx.android.synthetic.main.item_layout.view.*

class MainAdapter(
    private val videos: ArrayList<Results>,
    private val videoItemClickInterface: VideoItemClickInterface?
) :
    RecyclerView.Adapter<MainAdapter.DataViewHolder>() {

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(video: Results, videoItemClickInterface: VideoItemClickInterface?) {
            itemView.apply {
                artistNameTv.text = video.name?.first
                videoTitleTv.text = video.name?.first

                val requestOptions = RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
                Glide.with(context).load(video.picture?.large)
                    .placeholder(R.drawable.thumbnail_placeholder)
                    .error(R.drawable.thumbnail_placeholder).apply(requestOptions)
                    .into(videoThumbnailImg)

                Glide.with(context).load(video.picture?.large)
                    .placeholder(R.drawable.user_placeholder)
                    .error(R.drawable.user_placeholder).apply(requestOptions)
                    .into(artistImgView)


                cardLayout.setOnClickListener {
                    videoItemClickInterface?.onItemClick(result = video)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder =
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        )

    override fun getItemCount(): Int = videos.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(videos[position], videoItemClickInterface)
    }

    fun addUsers(results: Collection<Results?>?) {
        this.videos.apply {
            clear()
            if (results != null)
                addAll(results as Collection<Results>)
        }

    }
}