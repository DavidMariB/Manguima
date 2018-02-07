package com.dmb.testriotapi.Models.Forum;

/**
 * Created by Ricardo Borrull on 07/02/2018.
 */

public class Comentario {

    private String mensaje;
    private String uid;

    public Comentario () {

    }

    public Comentario(String mensaje, String uid) {
        this.mensaje = mensaje;
        this.uid = uid;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return "Comentario{" +
                "mensaje='" + mensaje + '\'' +
                ", uid='" + uid + '\'' +
                '}';
    }
}
