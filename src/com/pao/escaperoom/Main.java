package com.pao.escaperoom;
import com.pao.escaperoom.model.*;
import com.pao.escaperoom.service.MenuService;


public class Main {
    static void main(String[] args){
        MenuService menu = new MenuService();
        menu.start();
    }
}