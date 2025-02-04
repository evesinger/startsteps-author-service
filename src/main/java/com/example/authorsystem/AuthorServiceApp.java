/*
pseudo code:
- package declaration
- import springboot libraries
- annotation indicating main application class
- main method for starting the app
(+ access modifier +belongs to class/instance + return value)
* */

//package declaration
package com.example.authorsystem;

//import springboot libraries
import org.springframework.boot.SpringApplication; //class to start the app
import org.springframework.boot.autoconfigure.SpringBootApplication; //annotations

//annotation indicating main application class
@SpringBootApplication
public class AuthorServiceApp {
    //main method
    public static void main(String[] args) {
        SpringApplication.run(AuthorServiceApp.class, args); //clas Object reference, input paramater
    }
}