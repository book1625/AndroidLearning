package com.example.buttomnavigation;

import androidx.lifecycle.ViewModel;

public class SecondViewModel extends ViewModel {

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    private float scale = 1;

}