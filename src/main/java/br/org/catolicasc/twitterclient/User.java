package br.org.catolicasc.twitterclient;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Entity into which the Twitter user is deserialized.
 *
 * @author Rodrigo
 */
@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    @XmlElement(name = "name")
    private String name;

    public String getName() {
        return name;
    }
}