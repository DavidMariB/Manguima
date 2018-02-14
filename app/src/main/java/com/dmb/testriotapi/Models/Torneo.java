package com.dmb.testriotapi.Models;

import java.util.HashMap;

/**
 * Created by Ricardo Borrull on 14/02/2018.
 */

public class Torneo {

    private String titulo;
    private HashMap<String, Object> participantes;

    public Torneo() {
    }

    public Torneo(String titulo, HashMap<String, Object> participantes) {
        this.titulo = titulo;
        this.participantes = participantes;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public HashMap<String, Object> getParticipantes() {
        return participantes;
    }

    public void setParticipantes(HashMap<String, Object> participantes) {
        this.participantes = participantes;
    }
}
