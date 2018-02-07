package com.dmb.testriotapi.Models;

public class Friend {

    String username;
    String name;
    String surname;
    String profileimage;
    String conectado;

    public Friend(){

    }

    public Friend(String username, String name, String surname, String profileimage, String conectado) {
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.profileimage = profileimage;
        this.conectado = conectado;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getProfileimage() {
        return profileimage;
    }

    public void setProfileimage(String profileimage) {
        this.profileimage = profileimage;
    }

    public String getConectado() {
        return conectado;
    }

    public void setConectado(String conectado) {
        this.conectado = conectado;
    }
}
