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
    private String fecha;
    private String key;
    Map <String, Object> comentarios = new HashMap<String, Object>();
    Map<String, Object> likes = new HashMap<String, Object>();

    public Forum() {

    }

    public Forum(String titulo, String uid, String mensaje, String fecha, String key) {
        this.titulo = titulo;
        this.uid = uid;
        this.mensaje = mensaje;
        this.fecha = fecha;
        this.key = key;
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

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "Forum{" +
                "titulo='" + titulo + '\'' +
                ", uid='" + uid + '\'' +
                ", mensaje='" + mensaje + '\'' +
                ", fecha='" + fecha + '\'' +
                ", key='" + key + '\'' +
                ", comentarios=" + comentarios +
                ", likes=" + likes +
                '}';
    }
}
