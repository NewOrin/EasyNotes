package com.neworin.easynotes.model;

import android.graphics.Bitmap;

public class EditData {
    public String inputStr;
    public String imagePath;
    public Bitmap bitmap;

    public String getInputStr() {
        return inputStr;
    }

    public void setInputStr(String inputStr) {
        this.inputStr = inputStr;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public String toString() {
        return "EditData{" +
                "inputStr='" + inputStr + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", bitmap=" + bitmap +
                '}';
    }
}