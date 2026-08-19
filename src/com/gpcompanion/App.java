package com.gpcompanion;

import com.gpcompanion.auth.*;
import com.gpcompanion.exceptions.*;
import com.gpcompanion.race.*;
import com.gpcompanion.ui.*;

import javax.swing.*;
import java.util.HashMap;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame("Grand Prix Companion");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(800, 600);

                UserCredentialStore store = new FileUserCredentialStore("users.txt", new HashMap<>());
                SessionContext session = new SessionContext();
                AuthService authService = new AuthService(store, session);
                AuthController authController = new AuthController(authService, session);

                showWelcome(frame, authController, session);

                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }

    private static void swapScreen(JFrame frame, JPanel panel) {
        frame.getContentPane().removeAll();
        frame.add(panel);
        frame.revalidate();
        frame.repaint();
    }

    private static void showWelcome(final JFrame frame, final AuthController authController, final SessionContext session) {
        frame.setTitle("Grand Prix Companion");
        swapScreen(frame, new WelcomePanel(
                new Runnable() {
                    public void run() {
                        showLoginForm(frame, authController, session);
                    }
                },
                new Runnable() {
                    public void run() {
                        showRegisterForm(frame, authController, session);
                    }
                }
        ));
    }

    private static void showLoginForm(final JFrame frame, final AuthController authController, final SessionContext session) {
        swapScreen(frame, new LoginFormPanel(
                authController,
                new Runnable() {
                    public void run() {
                        showRace(frame, authController, session);
                    }
                },
                new Runnable() {
                    public void run() {
                        showWelcome(frame, authController, session);
                    }
                }
        ));
    }

    private static void showRegisterForm(final JFrame frame, final AuthController authController, final SessionContext session) {
        swapScreen(frame, new RegisterFormPanel(
                authController,
                new Runnable() {
                    public void run() {
                        showWelcome(frame, authController, session);
                    }
                }
        ));
    }

    private static void showRace(final JFrame frame, final AuthController authController, final SessionContext session) {
        frame.setTitle("Grand Prix Companion — " + session.getCurrentUser().getUsername());
        try {
            RaceLoader loader = new RaceLoader();
            RaceEngine engine = new RaceEngine(loader.load("race_data.csv"));
            swapScreen(frame, new RaceUI(engine, session, new Runnable() {
                public void run() {
                    showWelcome(frame, authController, session);
                }
            }));
        } catch (RaceDataException ex) {
            JOptionPane.showMessageDialog(frame, "Could not load race data: " + ex.getMessage());
            showWelcome(frame, authController, session);
        }
    }
}