package com.example.buttomnavigation;

import androidx.lifecycle.ViewModel;

public class FirstViewModel extends ViewModel {

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    private float rotation = 0;

}