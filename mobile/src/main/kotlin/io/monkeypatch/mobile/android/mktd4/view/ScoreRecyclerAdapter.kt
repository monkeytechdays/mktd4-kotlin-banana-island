package io.monkeypatch.mobile.android.mktd4.view

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import io.monkeypatch.mktd4.model.Player
import io.monkeypatch.mobile.android.mktd4.R
import io.monkeypatch.mobile.android.mktd4.model.getMonkey
import io.monkeypatch.mobile.android.mktd4.model.getResource

class ScoreRecyclerAdapter : RecyclerView.Adapter<ScoreRecyclerAdapter.ViewHolder>() {

    var items = emptyArray<Player>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.view_item_score, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.teamImage.setImageResource(item.getMonkey().getResource())
        holder.teamName.text = item.name
        holder.teamScore.text = "${item.score}"
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val teamImage = view.findViewById(R.id.teamImage) as ImageView
        val teamName = view.findViewById(R.id.teamName) as TextView
        val teamScore = view.findViewById(R.id.teamScore) as TextView
    }

}
