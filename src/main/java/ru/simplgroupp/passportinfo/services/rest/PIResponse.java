/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.simplgroupp.passportinfo.services.rest;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author stech_000
 */
@XmlRootElement
public class PIResponse {
    
    private int code;
    
    private String description;

    public PIResponse() {
    }

    public PIResponse(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    
    
}
