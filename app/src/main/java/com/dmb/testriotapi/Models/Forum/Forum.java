package com.dmb.testriotapi.Models.Forum;

import java.util.ArrayList;

/**
 * Created by Ricardo Borrull on 07/02/2018.
 */

public class Forum {

    private String titulo;
    private String uid;
    private String mensaje;
    private boolean anclado;
    private ArrayList<Comentario> comentarios;
    private ArrayList<Like> likes;

    public Forum() {

    }

    public Forum(String titulo, String uid, String mensaje, boolean anclado, ArrayList<Comentario> comentarios, ArrayList<Like> likes) {
        this.titulo = titulo;
        this.uid = uid;
        this.mensaje = mensaje;
        this.anclado = anclado;
        this.comentarios = comentarios;
        this.likes = likes;
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

    public boolean isAnclado() {
        return anclado;
    }

    public void setAnclado(boolean anclado) {
        this.anclado = anclado;
    }

    public ArrayList<Comentario> getComentarios() {
        return comentarios;
    }

    public void setComentarios(ArrayList<Comentario> comentarios) {
        this.comentarios = comentarios;
    }

    public ArrayList<Like> getLikes() {
        return likes;
    }

    public void setLikes(ArrayList<Like> likes) {
        this.likes = likes;
    }

    @Override
    public String toString() {
        return "Forum{" +
                "titulo='" + titulo + '\'' +
                ", uid='" + uid + '\'' +
                ", mensaje='" + mensaje + '\'' +
                ", anclado=" + anclado +
                ", comentarios=" + comentarios +
                ", likes=" + likes +
                '}';
    }
}
