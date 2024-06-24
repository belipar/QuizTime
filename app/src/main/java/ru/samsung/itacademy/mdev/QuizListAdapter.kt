package ru.samsung.itacademy.mdev

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ru.samsung.itacademy.mdev.databinding.QuizItemRecyclerRowBinding


class QuizListAdapter(private val quizModelList : List<QuizModel>) :
    RecyclerView.Adapter<QuizListAdapter.MyViewHolder>() {
    class MyViewHolder(private val binding: QuizItemRecyclerRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(model : QuizModel){
            binding.apply {
                quizTitleText.text = model.title
                quizSubtitleText.text = model.subtitle
                quizCountText.text = model.count + " вопросов"
                Picasso.get().load(model.image).into(quizImageView)
                root.setOnClickListener {
                    val intent  = Intent(root.context,QuizActivity::class.java)
                    QuizActivity.questionModelList = model.questionList
                    QuizActivity.count = model.count
                    QuizActivity.idQiuz = model.id
                    QuizActivity.lastRecord = model.record
                    root.context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = QuizItemRecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return quizModelList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(quizModelList[position])
    }
}