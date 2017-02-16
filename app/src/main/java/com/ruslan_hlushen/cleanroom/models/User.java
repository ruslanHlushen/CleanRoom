package com.ruslan_hlushen.cleanroom.models;

/**
 * Created by Ruslan on 28.01.2017.
 */

public class User {

    private int Floor;
    private Integer HostelId;
    private String FirstName;
    private String LastName;
    private String Email;
    private String Login;
    private String Password;
    private String FirebaseToken;
    private String VkId;


    public User(String login,
                String password) {

        Login = login;
        Password = password;
    }


    public User(String login,
                String firstName,
                String lastName,
                String firebaseToken,
                String vkId) {

        Login = login;
        FirstName = firstName;
        LastName = lastName;
        FirebaseToken = firebaseToken;
        VkId = vkId;
    }


    public User(int floor,
                Integer hostelId,
                String firstName,
                String lastName,
                String email,
                String login,
                String password,
                String firebaseToken,
                String vkId) {

        Floor = floor;
        HostelId = hostelId;
        FirstName = firstName;
        LastName = lastName;
        Email = email;
        Login = login;
        Password = password;
        FirebaseToken = firebaseToken;
        VkId = vkId;
    }
}
