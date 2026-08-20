package com.gpcompanion.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class WelcomePanel extends JPanel {
    public WelcomePanel(final Runnable onLoginClick, final Runnable onRegisterClick) {
        Color bgColor = new Color(26, 26, 26);
        Color neonYellow = new Color(204, 255, 0);

        setBackground(bgColor);
        setLayout(new GridBagLayout());

        JPanel box = new JPanel();
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        box.setBackground(bgColor);

        JLabel title = new JLabel("Grand Prix Companion");
        title.setForeground(Color.WHITE);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 22f));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton loginBtn = new JButton("Login");
        loginBtn.setBackground(neonYellow);
        loginBtn.setForeground(Color.BLACK);
        loginBtn.setFocusPainted(false);
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginBtn.setMaximumSize(new Dimension(200, 40));
        loginBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onLoginClick.run();
            }
        });

        JButton registerBtn = new JButton("Register");
        registerBtn.setBackground(Color.GRAY);
        registerBtn.setForeground(Color.WHITE);
        registerBtn.setFocusPainted(false);
        registerBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerBtn.setMaximumSize(new Dimension(200, 40));
        registerBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onRegisterClick.run();
            }
        });

        box.add(title);
        box.add(Box.createVerticalStrut(20));
        box.add(loginBtn);
        box.add(Box.createVerticalStrut(10));
        box.add(registerBtn);

        add(box);
    }
}