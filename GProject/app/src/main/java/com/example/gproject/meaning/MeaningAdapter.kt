//package com.example.gproject.meaning
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.recyclerview.widget.RecyclerView
//import com.example.gproject.WordCard.Meaning
//import com.example.gproject.databinding.MeaningRecyclerRowBinding
//
//class MeaningAdapter(private var meaningList : List<Meaning>) : RecyclerView.Adapter<MeaningAdapter.MeaningViewHolder>()  {
//
//    class MeaningViewHolder(private val binding: MeaningRecyclerRowBinding) : RecyclerView.ViewHolder(binding.root) {
//        var definitionsText: String? = null
//
//
//        fun bind(meaning: Meaning) {
//            binding.partOfSpeechTextview.text = meaning.partOfSpeech
//            definitionsText = meaning.definitions.joinToString("\n\n") { "${meaning.definitions.indexOf(it) + 1}. ${it.definition}" }
//            binding.definitionsTextview.text = definitionsText
//
//            if (meaning.synonyms.isEmpty()) {
//                binding.synonymsTitleTextview.visibility = View.GONE
//                binding.synonymsTextview.visibility = View.GONE
//            } else {
//                binding.synonymsTitleTextview.visibility = View.VISIBLE
//                binding.synonymsTextview.visibility = View.VISIBLE
//                binding.synonymsTextview.text = meaning.synonyms.joinToString(", ")
//            }
//
//            if (meaning.antonyms.isEmpty()) {
//                binding.antonymsTitleTextview.visibility = View.GONE
//                binding.antonymsTextview.visibility = View.GONE
//            } else {
//                binding.antonymsTitleTextview.visibility = View.VISIBLE
//                binding.antonymsTextview.visibility = View.VISIBLE
//                binding.antonymsTextview.text = meaning.antonyms.joinToString(", ")
//            }
//        }
////        fun getSelectedDefinitions(): String? {
////            return if (adapterPosition != RecyclerView.NO_POSITION && adapterPosition < meaningList.size) {
////                meaningList[adapterPosition].definitionsText
////            } else ""
////        }
//
//    }
//
//    fun updateNewData(newMeaningList: List<Meaning>) {
//        meaningList = newMeaningList
//        notifyDataSetChanged()
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeaningViewHolder {
//        val binding = MeaningRecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return MeaningViewHolder(binding)
//    }
//
//    override fun getItemCount(): Int {
//        return meaningList.size
//    }
//
//    override fun onBindViewHolder(holder: MeaningViewHolder, position: Int) {
//        holder.bind(meaningList[position])
//    }
//
//
//}
