/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.simplgroupp.passportinfo.exception;

/**
 *
 * @author stechiev
 */
public class PassportInfoException extends Exception{

    public PassportInfoException() {
    }

    public PassportInfoException(String message) {
        super(message);
    }

    public PassportInfoException(String message, Throwable cause) {
        super(message, cause);
    }

    public PassportInfoException(Throwable cause) {
        super(cause);
    }
    
    
    
}
