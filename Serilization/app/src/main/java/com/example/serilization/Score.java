package com.example.serilization;

import java.io.Serializable;

public class Score implements Serializable {
    private int math;
    private int english;
    private int chinese;
    private String grade;

    public Score(int math, int english, int chinese) {
        this.math = math;
        this.english = english;
        this.chinese = chinese;

        float avg = (math + english + chinese) / 3;

        if (avg >= 90) grade = "A";
        else if (avg >= 80) grade = "B";
        else if (avg >= 70) grade = "C";
        else if (avg >= 60) grade = "D";
        else grade = "F";
    }

    public int getMath() {
        return math;
    }

    public void setMath(int math) {
        this.math = math;
    }

    public int getEnglish() {
        return english;
    }

    public void setEnglish(int english) {
        this.english = english;
    }

    public int getChinese() {
        return chinese;
    }

    public void setChinese(int chinese) {
        this.chinese = chinese;
    }

    public String getGrade() {
        return grade;
    }

    // public void setGrade(String grade) {
    //     this.grade = grade;
    // }
}
