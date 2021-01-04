package com.example.thevoice;

import java.io.Serializable;

public class Score implements Serializable {
    int id;
    String name;
    String surname;
    int dB;

    public Score(){
    }

    public Score(int i, String name, String surname, int dB) {
        this.id = i;
        this.name = name;
        this.surname = surname;
        this.dB = dB;
    }

    // ID
    public int getID() {
        return this.id;
    }

    public void setID(int id) {
        this.id = id;
    }

    // Name
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Surname
    public String getSurname() {
        return this.surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    // dB
    public int getdB() { return dB; }

    public void setdB(int dB) { this.dB = dB; }

}
