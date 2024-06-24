package ru.samsung.itacademy.mdev

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ru.samsung.itacademy.mdev.databinding.RecordItemRecyclerRowBinding


class QuizRecordAdapter(private val quizModelList : List<QuizModel>) :
    RecyclerView.Adapter<QuizRecordAdapter.MyViewHolder>() {
    class MyViewHolder(private val binding: RecordItemRecyclerRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(model : QuizModel){
            binding.apply {
                quizTitleText.text = model.title
                Picasso.get().load(model.image).into(quizImageView)
                quizRecord.text = model.record + " %"
                quizScore.text = model.score + "/" + model.count
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = RecordItemRecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return quizModelList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(quizModelList[position])
    }
}