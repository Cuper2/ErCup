package com.ErCup;

import com.ErCup.ui.*;
import javax.swing.*;
import com.formdev.flatlaf.*;

public class Main {
    public static void main(String[] args) {
        FlatDarculaLaf.setup();
        Default defaultUI = new Default();
        defaultUI.setSize(700, 400);
        defaultUI.setExtendedState(JFrame.MAXIMIZED_BOTH);
        defaultUI.setVisible(true);
    }
}

