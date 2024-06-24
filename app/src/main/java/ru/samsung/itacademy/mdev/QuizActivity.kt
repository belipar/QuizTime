package ru.samsung.itacademy.mdev

import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.database.database
import ru.samsung.itacademy.mdev.databinding.ActivityExitBinding
import ru.samsung.itacademy.mdev.databinding.ActivityQuizBinding
import ru.samsung.itacademy.mdev.databinding.ActivityValidBinding
import ru.samsung.itacademy.mdev.databinding.ScoreDialogBinding


class QuizActivity : AppCompatActivity(),View.OnClickListener {

    companion object {
        var questionModelList : List<QuestionModel> = listOf()
        var count : String = ""
        var idQiuz : String = ""
        var lastRecord : String = ""
    }

    lateinit var binding: ActivityQuizBinding
    var currentQuestionIndex = 0;
    var selectedAnswer = ""
    var score = 0;
    private var timer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val totalQuestions = questionModelList.size
        val dialogBinding  = ActivityValidBinding.inflate(layoutInflater)
        val alertDialog = AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .setCancelable(false)
            .show()
        dialogBinding.apply {
            countQuestion.text = "В квизе $totalQuestions вопросов. Время ответа на вопрос 60 секунд "
            btnStart.setOnClickListener {
                startQuiz()
                alertDialog.dismiss()
            }
            btnBack.setOnClickListener {
                finish()
            }
        }
    }

    private fun exit(){
        val dialog  = ActivityExitBinding.inflate(layoutInflater)
        val alert = AlertDialog.Builder(this)
            .setView(dialog.root)
            .setCancelable(false)
            .show()
        dialog.apply {
            btnCansel.setOnClickListener {
                alert.dismiss()
            }
            btnExit.setOnClickListener {
                finish()
            }
        }
    }

    private fun startQuiz(){
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.buttonBack.setOnClickListener {
            exit()
        }
        binding.apply {
            btn0.setOnClickListener(this@QuizActivity)
            btn1.setOnClickListener(this@QuizActivity)
            btn2.setOnClickListener(this@QuizActivity)
            btn3.setOnClickListener(this@QuizActivity)
            nextBtn.setOnClickListener(this@QuizActivity)
        }
        loadQuestions()
        startTimer()
    }

    private fun startTimer() {
        stopTimer()
        val totalTimeInMillis = 60 * 1000L
        timer = object : CountDownTimer(totalTimeInMillis, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished / 1000
                val minutes = seconds / 60
                val remainingSeconds = seconds % 60
                binding.timerIndicatorTextview.text = String.format("%02d:%02d", minutes, remainingSeconds)
            }
            override fun onFinish() {
                currentQuestionIndex++
                loadQuestions()
            }
        }.start()
    }

    private fun stopTimer() {
        timer?.cancel()
    }

    private fun loadQuestions(){
        selectedAnswer = ""
        if(currentQuestionIndex == questionModelList.size-1){
            binding.nextBtn.text = "Завершить"
        }
        if(currentQuestionIndex == questionModelList.size){
            stopTimer()
            finishQuiz()
            return
        }
        stopTimer()
        binding.apply {
            startTimer()
            questionIndicatorTextview.text = " ${currentQuestionIndex+1}/ ${questionModelList.size} "
            questionProgressIndicator.progress =
                ( currentQuestionIndex.toFloat() / questionModelList.size.toFloat() * 100 ).toInt()
            questionTextview.text = questionModelList[currentQuestionIndex].question
            btn0.text = questionModelList[currentQuestionIndex].options[0]
            btn1.text = questionModelList[currentQuestionIndex].options[1]
            btn2.text = questionModelList[currentQuestionIndex].options[2]
            btn3.text = questionModelList[currentQuestionIndex].options[3]
        }
    }

    override fun onClick(view: View?) {
        binding.apply {
            btn0.setBackgroundColor(getColor(R.color.btn))
            btn1.setBackgroundColor(getColor(R.color.btn))
            btn2.setBackgroundColor(getColor(R.color.btn))
            btn3.setBackgroundColor(getColor(R.color.btn))
        }

        val clickedBtn = view as Button
        if(clickedBtn.id==R.id.next_btn){
            if(selectedAnswer.isEmpty()){
                Toast.makeText(applicationContext,"Ответьте на вопрос",Toast.LENGTH_SHORT).show()
                return;
            }
            if(selectedAnswer == questionModelList[currentQuestionIndex].correct){
                score++
                Log.i("Результат",score.toString())
            }
            currentQuestionIndex++
            loadQuestions()
        }else{
            selectedAnswer = clickedBtn.text.toString()
            clickedBtn.setBackgroundColor(getColor(R.color.btn_click))
        }
    }

    private fun finishQuiz(){
        val database = Firebase.database
        val path = idQiuz.toInt()-1
        val myRef = database.getReference("$path")
        val childUpdates = HashMap<String, Any>()
        val childUpdates2 = HashMap<String, Any>()
        val totalQuestions = questionModelList.size
        val percentage = ((score.toFloat() / totalQuestions.toFloat() ) *100 ).toInt()
        val dialogBinding  = ScoreDialogBinding.inflate(layoutInflater)
        dialogBinding.apply {
            scoreProgressIndicator.progress = percentage
            scoreProgressText.text = "$percentage %"
            scoreTitle.text = "Результат:"
            if(percentage>50){
                scoreProgressIndicator.setIndicatorColor(Color.BLUE)
            }
            else{
                scoreProgressIndicator.setIndicatorColor(Color.RED)
            }
            scoreSubtitle.text = "$score из $totalQuestions"
            if(lastRecord.toInt() < percentage){
                childUpdates["record"] = "$percentage"
                childUpdates2["score"] = "$score"
                myRef.updateChildren(childUpdates)
                myRef.updateChildren(childUpdates2)
            }
            finishBtn.setOnClickListener {
                finish()
            }
        }

        AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .setCancelable(false)
            .show()
    }
}
