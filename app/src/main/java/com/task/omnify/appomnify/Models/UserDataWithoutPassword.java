package com.task.omnify.appomnify.Models;

public class UserDataWithoutPassword {
    String UID;
    String token;
    public UserDataWithoutPassword(String token,String UID){
        this.token=token;
        this.UID=UID;

    }
    public UserDataWithoutPassword(){

    }
}
