package com.example.shaadisampleapp.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.shaadisampleapp.R
import com.example.shaadisampleapp.network.model.Results
import com.example.shaadisampleapp.ui.main.interfaces.MatchesItemClickInterface
import com.example.shaadisampleapp.utils.Constants
import kotlinx.android.synthetic.main.item_layout.view.*

class MainAdapter(
    private val matches: ArrayList<Results>,
    private val matchesItemClickInterface: MatchesItemClickInterface?
) :
    RecyclerView.Adapter<MainAdapter.DataViewHolder>() {

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(
            match: Results,
            matchesItemClickInterface: MatchesItemClickInterface?,
            position: Int
        ) {
            itemView.apply {
                tvName.text = "${match.name?.title} ${match.name?.first} ${match.name?.last}"
                tvDetails.text =
                    "${match.dob?.age} yrs ${match.gender} \n${match.location?.city}, ${match.location?.country}"
                val requestOptions = RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
                Glide.with(context).load(match.picture?.large)
                    .placeholder(R.drawable.thumbnail_placeholder)
                    .error(R.drawable.thumbnail_placeholder).apply(requestOptions)
                    .into(matchImg)
                when (match.matchStatus) {
                    Constants.PROFILE_ACCEPTED -> {
                        tvAccept.text = "Accepted"
                        tvDecline.text = "Decline"
                    }
                    Constants.PROFILE_DECLINED -> {
                        tvAccept.text = "Accept"
                        tvDecline.text = "Declined"
                    }
                    else -> {
                        tvAccept.text = "Accept"
                        tvDecline.text = "Decline"
                    }
                }
                tvAccept.setOnClickListener {
                    if (match.matchStatus == Constants.PROFILE_ACCEPTED) {
                        Toast.makeText(context, "Already Accepted", Toast.LENGTH_SHORT).show()
                    } else {
                        matchesItemClickInterface?.onMatchStatusChange(
                            result = match,
                            Constants.PROFILE_ACCEPTED,
                            position
                        )
                    }
                }
                ivAccept.setOnClickListener {
                    if (match.matchStatus == Constants.PROFILE_ACCEPTED) {
                        Toast.makeText(context, "Already Accepted", Toast.LENGTH_SHORT).show()
                    } else {
                        matchesItemClickInterface?.onMatchStatusChange(
                            result = match,
                            Constants.PROFILE_ACCEPTED,
                            position
                        )
                    }
                }
                tvDecline.setOnClickListener {
                    if (match.matchStatus == Constants.PROFILE_DECLINED) {
                        Toast.makeText(context, "Already Declined", Toast.LENGTH_SHORT).show()
                    } else {
                        matchesItemClickInterface?.onMatchStatusChange(
                            result = match,
                            Constants.PROFILE_DECLINED,
                            position
                        )
                    }
                }
                ivDecline.setOnClickListener {
                    if (match.matchStatus == Constants.PROFILE_DECLINED) {
                        Toast.makeText(context, "Already Declined", Toast.LENGTH_SHORT).show()
                    } else {
                        matchesItemClickInterface?.onMatchStatusChange(
                            result = match,
                            Constants.PROFILE_DECLINED,
                            position
                        )
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder =
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        )

    override fun getItemCount(): Int = matches.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(matches[position], matchesItemClickInterface, position)
    }

    fun addUsers(results: Collection<Results?>?) {
        this.matches.apply {
            clear()
            if (results != null)
                addAll(results as Collection<Results>)
        }

    }
}