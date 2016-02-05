/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.infinity.dto;

/**
 *
 * @author t311372
 */


/*
    "properties": {
          "email": {
            "type": "string",
            "index" : "not_analyzed"
          },
          "id": {
            "type": "string"
          },
          "name": {
            "type": "string",
            "index" : "not_analyzed"
          },
          "phone": {
            "type": "string",
            "index" : "not_analyzed"
          }
        }

*/
public class Clients {
    
    
    private String id;
    private String name;
    private String email;
    private String phone;
    
    

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    

   

}
