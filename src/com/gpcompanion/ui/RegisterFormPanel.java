package com.gpcompanion.ui;

import com.gpcompanion.auth.*;
import com.gpcompanion.exceptions.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class RegisterFormPanel extends JPanel {
    public RegisterFormPanel(final AuthController authController, final Runnable onBack) {
        Color bgColor = new Color(26, 26, 26);
        Color fieldBg = Color.DARK_GRAY;
        Color errorRed = new Color(255, 90, 90);
        Color successGreen = new Color(140, 255, 140);

        setBackground(bgColor);
        setLayout(new GridBagLayout());

        JPanel form = new JPanel(new GridLayout(0, 2, 10, 10));
        form.setBackground(bgColor);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setForeground(Color.WHITE);
        JLabel passLabel = new JLabel("Password:");
        passLabel.setForeground(Color.WHITE);
        JLabel confirmLabel = new JLabel("Confirm Password:");
        confirmLabel.setForeground(Color.WHITE);

        final JTextField userField = new JTextField(15);
        userField.setBackground(fieldBg);
        userField.setForeground(Color.WHITE);
        userField.setCaretColor(Color.WHITE);
        userField.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        final JPasswordField passField = new JPasswordField(15);
        passField.setBackground(fieldBg);
        passField.setForeground(Color.WHITE);
        passField.setCaretColor(Color.WHITE);
        passField.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        final JPasswordField confirmField = new JPasswordField(15);
        confirmField.setBackground(fieldBg);
        confirmField.setForeground(Color.WHITE);
        confirmField.setCaretColor(Color.WHITE);
        confirmField.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        final JLabel statusLabel = new JLabel(" ");
        statusLabel.setForeground(errorRed);
        final Color errorRedFinal = errorRed;
        final Color successGreenFinal = successGreen;

        JButton registerBtn = new JButton("Register");
        registerBtn.setBackground(Color.GRAY);
        registerBtn.setForeground(Color.WHITE);
        registerBtn.setFocusPainted(false);

        JButton backBtn = new JButton("Back");
        backBtn.setBackground(Color.GRAY);
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false);
        backBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onBack.run();
            }
        });

        registerBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = userField.getText().trim();
                char[] password = passField.getPassword();
                char[] confirm = confirmField.getPassword();

                try {
                    if (!Arrays.equals(password, confirm)) {
                        statusLabel.setForeground(errorRedFinal);
                        statusLabel.setText("Passwords do not match.");
                        return;
                    }
                    authController.handleRegister(username, password);
                    statusLabel.setForeground(successGreenFinal);
                    statusLabel.setText("Registration successful — go back and log in.");
                } catch (DuplicateUserException | NoSuchAlgorithmException | IllegalArgumentException ex) {
                    statusLabel.setForeground(errorRedFinal);
                    statusLabel.setText(ex.getMessage());
                } finally {
                    Arrays.fill(password, '\0');
                    Arrays.fill(confirm, '\0');
                    passField.setText("");
                    confirmField.setText("");
                }
            }
        });

        form.add(userLabel);
        form.add(userField);
        form.add(passLabel);
        form.add(passField);
        form.add(confirmLabel);
        form.add(confirmField);
        form.add(new JLabel());
        form.add(statusLabel);
        form.add(backBtn);
        form.add(registerBtn);

        add(form);
    }
}