package com.ErCup;

import com.ErCup.ui.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import com.formdev.flatlaf.*;
import static java.awt.event.KeyEvent.VK_F11;

public class Main {
    public static void main(String[] args) {
        FlatDarculaLaf.setup();
        Default defaultUI = new Default(args);
        defaultUI.setSize(800, 500);
        defaultUI.setExtendedState(JFrame.MAXIMIZED_BOTH);
        defaultUI.setVisible(true);
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(e -> {
            if(e.getKeyCode()==VK_F11 && e.getID() == KeyEvent.KEY_PRESSED){
                if(!defaultUI.isUndecorated()){
                    defaultUI.dispose();
                    defaultUI.setUndecorated(true);
                    defaultUI.setExtendedState(JFrame.MAXIMIZED_BOTH);
                }
                else{
                    defaultUI.dispose();
                    defaultUI.setUndecorated(false);
                    defaultUI.setExtendedState(JFrame.NORMAL);
                }
                defaultUI.setVisible(true);
            }
            return false;
        });
    }
}