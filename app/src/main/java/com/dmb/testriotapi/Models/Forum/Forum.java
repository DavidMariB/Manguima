package com.dmb.testriotapi.Models.Forum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ricardo Borrull on 07/02/2018.
 */

public class Forum {

    private String titulo;
    private String uid;
    private String mensaje;
    Map <String, Object> comentarios = new HashMap<String, Object>();
    Map<String, Object> likes = new HashMap<String, Object>();

    public Forum() {

    }

    public Forum(String titulo, String uid, String mensaje) {
        this.titulo = titulo;
        this.uid = uid;
        this.mensaje = mensaje;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Map<String, Object> getComentarios() {
        return comentarios;
    }

    public void setComentarios(Map<String, Object> comentarios) {
        this.comentarios = comentarios;
    }

    public Map<String, Object> getLikes() {
        return likes;
    }

    public void setLikes(Map<String, Object> likes) {
        this.likes = likes;
    }

    @Override
    public String toString() {
        return "Forum{" +
                "titulo='" + titulo + '\'' +
                ", uid='" + uid + '\'' +
                ", mensaje='" + mensaje + '\'' +
                ", comentarios=" + comentarios +
                ", likes=" + likes +
                '}';
    }
}
