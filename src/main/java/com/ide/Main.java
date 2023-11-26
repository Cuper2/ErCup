package com.ide;

import com.ide.ui.*;
import javax.swing.*;
import com.formdev.flatlaf.*;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel( new FlatDarculaLaf() );
        } catch( Exception ex ) {
            System.err.println( "Failed to initialize LaF" );
        }
        Default defaultUI = new Default();
        defaultUI.setSize(700, 400);
        defaultUI.setVisible(true);
    }
}

