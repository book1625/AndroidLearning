package com.example.parcelable;

import android.os.Parcel;
import android.os.Parcelable;

public class Score implements Parcelable {

    //成員
    private int math;
    private int english;

    //ctor
    public Score(int math, int english) {
        this.math = math;
        this.english = english;
    }

    //getter setter

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

    //Parcelable

    protected Score(Parcel in) {
        math = in.readInt();
        english = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(math);
        dest.writeInt(english);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Score> CREATOR = new Creator<Score>() {
        @Override
        public Score createFromParcel(Parcel in) {
            return new Score(in);
        }

        @Override
        public Score[] newArray(int size) {
            return new Score[size];
        }
    };
}
