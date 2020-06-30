package com.example.urduhandwritingtutor;

import java.io.Serializable;

public class evaluation_class implements Serializable {
    int Id;
    String Character;
    Integer Score;
    String Feedback;


    public evaluation_class(){}

    public evaluation_class(int id, String character, int score, String feedback) {
        Character = character;
        Score = score;
        Feedback = feedback;
        Id = id;
    }

    public String getCharacter() {
        return Character;
    }

    public int getScore() {
        return Score;
    }

    public String getFeedback() {
        return Feedback;
    }

    public int getId() {
        return Id;
    }
}

