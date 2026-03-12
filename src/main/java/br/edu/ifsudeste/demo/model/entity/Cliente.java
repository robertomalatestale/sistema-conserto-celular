package br.edu.ifsudeste.demo.model.entity;

import jakarta.persistence.Entity;

@Entity
public class Cliente extends Pessoa{


    //@ManyToOne
    private Dispositivo dispositivos;

}
