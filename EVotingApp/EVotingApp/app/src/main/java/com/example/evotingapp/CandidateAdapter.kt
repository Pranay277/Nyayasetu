package com.example.evotingapp;
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.evotingapp.Candidate
import com.example.evotingapp.R

class CandidateAdapter(private val context: Context, private val candidates: List<Candidate>) : BaseAdapter() {
    override fun getCount(): Int = candidates.size
    override fun getItem(position: Int): Any = candidates[position]
    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_candidate, parent, false)
        val candidate = candidates[position]

        // Set the candidate name
        val candidateName: TextView = view.findViewById(R.id.candidateName)
        candidateName.text = candidate.name

        // Set the party name
        val candidateParty: TextView = view.findViewById(R.id.candidateParty)
        candidateParty.text = "Party: ${candidate.party}"

        // Set the constituency
        val candidateConstituency: TextView = view.findViewById(R.id.candidateConstituency)
        candidateConstituency.text = "Constituency: ${candidate.constituency}"

        // Set the candidate logo (image)
        val candidateImage: ImageView = view.findViewById(R.id.candidateImage)
        Glide.with(context).load(candidate.logoUrl).into(candidateImage)

        return view
    }
}

