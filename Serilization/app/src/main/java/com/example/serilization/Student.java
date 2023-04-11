package com.example.serilization;

import java.io.Serializable;

//note

public class Student implements Serializable {

    //在 java 裡，可序列類別其實都會自動生成這個版本號，當類成員有變動時，這個版號就會變
    //然而這會造成， Serializable 出來的資料 在不同版號中是無法相容的，如果這裡強制宣告了固定版號
    //則不同版本生成的序列化資料就一樣可讀，缺的欄位就只填預設值，行為就類似 json
    //這裡寫與不寫各有用途，就須視情況決定
    private static final long serialVersionUID = 32132132132165125L;

    private String name;
    private int age;
    private Score score;

    //這是示範一個不要序列化的欄位，它用 transient 來避免
    private transient String useless;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Score getScore() {
        return score;
    }

    public void setScore(Score score) {
        this.score = score;
    }

    public String getUseless() {
        return useless;
    }

    public void setUseless(String useless) {
        this.useless = useless;
    }

    public Student(String name, int age, Score score) {
        this.name = name;
        this.age = age;
        this.score = score;
    }
}
