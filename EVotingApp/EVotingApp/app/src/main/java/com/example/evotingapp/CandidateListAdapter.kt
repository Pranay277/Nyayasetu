package com.example.evotingapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class CandidateListAdapter(
    private val context: Context,
    private val candidates: List<Candidate>,
    private val onVoteClick: (Candidate) -> Unit
) : RecyclerView.Adapter<CandidateListAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val img = view.findViewById<ImageView>(R.id.candidateImage)
        val name = view.findViewById<TextView>(R.id.candidateName)
        val party = view.findViewById<TextView>(R.id.candidateParty)
        val constituency = view.findViewById<TextView>(R.id.candidateConstituency)
        val voteButton = view.findViewById<Button>(R.id.btnCastVote)
        val container = view
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_candidate, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val candidate = candidates[position]

        holder.name.text = candidate.name
        holder.party.text = "Party: ${candidate.party}"
        holder.constituency.text = "Constituency: ${candidate.constituency}"

        Glide.with(context).load(candidate.logoUrl).into(holder.img)

        // Hide button initially
        holder.voteButton.visibility = View.GONE

        // Show button when clicked
        holder.container.setOnClickListener {
            holder.voteButton.visibility = View.VISIBLE
        }

        holder.voteButton.setOnClickListener {
            onVoteClick(candidate)
        }
    }

    override fun getItemCount(): Int = candidates.size
}
