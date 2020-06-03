package com.example.urduhandwritingtutor;

import java.util.ArrayList;
import java.util.List;

public class evaluation_class {
    List<String> Characters = new ArrayList<>();
    List<Integer> Score = new ArrayList<>();
    List<String> Feedback = new ArrayList<>();


    public evaluation_class(List<String> characters, List<Integer> score, List<String> feedback) {
        Characters = characters;
        Score = score;
        Feedback = feedback;
    }

    public List<String> getCharacters() {
        return Characters;
    }

    public List<Integer> getScore() {
        return Score;
    }

    public List<String> getFeedback() {
        return Feedback;
    }
}
