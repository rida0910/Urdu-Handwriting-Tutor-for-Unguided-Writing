package com.example.urduhandwritingtutor;

import java.io.Serializable;
import java.util.List;

public class Quiz_class implements Serializable {
    int quizID;
    int QuizNumber;
    List<evaluation_class> evaluations;

    public Quiz_class(int quizId, int quizNumber, List<evaluation_class> evaluations) {
        QuizNumber = quizNumber;
        this.evaluations = evaluations;
        quizID = quizId;
    }

    public int getQuizNumber() {
        return QuizNumber;
    }

    public List<evaluation_class> getEvaluations() {
        return evaluations;
    }

    public int getQuizID() {
        return quizID;
    }
}
