package com.bif812.roman.hackyourlife;

import java.io.Serializable;

/**
 * Created by Elsi on 26/07/2015.
 * Palabra class para guardar la palabra
 * con su imagen
 */
public class Palabra implements Serializable {
    private String id;
    private String name;
    private int img;

    public Palabra (String id, int img, String name){
        this.id = id;
        this.name = name;
        this.img = img;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getImg() {
        return img;
    }
    public void setImg(int img) {
        this.img = img;
    }
}
