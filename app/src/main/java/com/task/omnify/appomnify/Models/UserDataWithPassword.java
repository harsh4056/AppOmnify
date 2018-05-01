package com.task.omnify.appomnify.Models;

public class UserDataWithPassword {
   public String token;
   public String UID;
   public String password;


    public UserDataWithPassword(String token,String UID,String password){
        this.token=token;
        this.password=password;
        this.UID=UID;

    }
    public UserDataWithPassword(){

    }
}
