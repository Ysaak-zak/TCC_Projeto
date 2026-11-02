package com.api.Projeto_3.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.api.Projeto_3.service.PerfilService;




@Controller
public class PublicController {
   
    @Autowired
    PerfilService service;

    @GetMapping("/")
    public String getIndex() {
         return "redirect:/home";
    }

    @GetMapping("/home")
    public String getHome() {
         return "publicPlages/home";
    }


    @GetMapping("/login")
    public String getMethodName() {
        return "publicPlages/Login";
    }



}


