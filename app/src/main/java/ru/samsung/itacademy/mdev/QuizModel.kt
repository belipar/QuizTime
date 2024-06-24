package ru.samsung.itacademy.mdev


data class QuizModel(
    val id: String,
    val title: String,
    val subtitle: String,
    val count: String,
    val image: String,
    val record: String,
    val score: String,
    val questionList: List<QuestionModel>
){
    constructor() : this("","","","", "", "", "", emptyList())
}

data class QuestionModel(
    val question : String,
    val options : List<String>,
    val correct : String,
){
    constructor() : this ("", emptyList(),"")
}

