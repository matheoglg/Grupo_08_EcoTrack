/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelos;

/**
 *
 * @author USER
 */

public class Usuario {
    private String user;
    private String pass;

    public Usuario(String user, String pass) {
        this.user = user;
        this.pass = pass;
    }

    public String getUser() { return user; }
    public String getPass() { return pass; }

    @Override
    public String toString() {
        return user + "," + pass; //formato para el txt
    }
}