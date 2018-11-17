package com.example.memerun.classes;

public class achievement {

    public String imageName;
    public int imageId;

    public achievement(String name_id)
    {
        imageName = name_id;
    }

    public String getImageName() {
        return imageName;
    }


    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}
