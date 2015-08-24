package br.org.catolicasc.twitterclient;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author rodrigo
 */
@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
public class Status {

    @XmlElement(name = "created_at")
    private String createdAt;
    @XmlElement(name = "text")
    private String text;
    @XmlElement(name = "user")
    private User user;

    public String getCreatedAt() {
        return createdAt;
    }

    public String getText() {
        return text;
    }

    public User getUser() {
        return user;
    }
}    

