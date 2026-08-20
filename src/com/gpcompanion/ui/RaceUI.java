package com.gpcompanion.ui;

import com.gpcompanion.auth.*;
import com.gpcompanion.race.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.geom.RoundRectangle2D;

public class RaceUI extends JPanel {
    private boolean showGap = true;

    public RaceUI(final RaceEngine engine, final SessionContext session, final Runnable onLogout, final AuthController authController) {
        Color bgColor = new Color(26, 26, 26);
        Color fgColor = new Color(220, 220, 220);
        Color neonYellow = new Color(200, 255, 0);

        setBackground(bgColor);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        topPanel.setBackground(bgColor);

        JLabel userLabel = new JLabel("Logged in as " + session.getCurrentUser().getUsername());
        userLabel.setForeground(Color.GRAY);

        final JLabel lapLabel = new JLabel("Lap " + engine.getCurrentLap() + "/" + engine.getTotalLaps());
        lapLabel.setForeground(fgColor);

        final JLabel statusLabel = new JLabel(" ");
        statusLabel.setForeground(neonYellow);

        final JButton toggleGapBtn = new RoundedButton("Interval", bgColor, fgColor, Color.GRAY);
        final JButton startBtn = new RoundedButton("Start", neonYellow, Color.BLACK, neonYellow);
        final JButton pauseBtn = new RoundedButton("Pause", bgColor, fgColor, Color.GRAY);
        JButton logoutBtn = new RoundedButton("Logout", bgColor, fgColor, Color.GRAY);
        pauseBtn.setEnabled(false);

        topPanel.add(userLabel);
        topPanel.add(statusLabel);
        topPanel.add(lapLabel);
        topPanel.add(toggleGapBtn);
        topPanel.add(startBtn);
        topPanel.add(pauseBtn);
        topPanel.add(logoutBtn);
        add(topPanel, BorderLayout.NORTH);

        final AbstractTableModel model = new AbstractTableModel() {
            public int getRowCount() { return engine.getStandings().size(); }
            public int getColumnCount() { return 6; }
            public Object getValueAt(int r, int c) {
                Standing s = engine.getStandings().get(r);
                switch (c) {
                    case 0: return s.getPosition();
                    case 1: return s.getDriver().getName();
                    case 2: return s.getDriver().getTeamName();
                    case 3: return String.format("%.3f", s.getLastLapTime());
                    case 4: return showGap
                            ? "+" + String.format("%.3f", s.getGapToLeader())
                            : "+" + String.format("%.3f", s.getIntervalToCarAhead());
                    case 5: return s.getCurrentTire();
                    default: return "";
                }
            }
            public String getColumnName(int c) {
                return new String[]{"#", "Driver", "Team", "Last Lap", "Interval", "Tire"}[c];
            }
        };

        final JTable table = new JTable(model);
        table.setBackground(bgColor);
        table.setForeground(fgColor);
        table.setGridColor(new Color(100, 100, 100));
        table.setShowGrid(false);
        table.setShowHorizontalLines(true);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setRowHeight(40);
        table.getTableHeader().setBackground(bgColor);
        table.getTableHeader().setForeground(Color.GRAY);
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.DARK_GRAY));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        centerRenderer.setBackground(bgColor);
        centerRenderer.setForeground(fgColor);

        final Color bgColorFinal = bgColor;
        final Color fgColorFinal = fgColor;
        final Color gainedColor = new Color(20, 90, 40);
        final Color lostColor = new Color(100, 25, 25);

        table.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, col);
                Standing s = engine.getStandings().get(row);
                setHorizontalAlignment(JLabel.CENTER);
                setBackground(rowColor(s, bgColorFinal, gainedColor, lostColor));
                setForeground(fgColorFinal);
                setBorder(BorderFactory.createMatteBorder(0, 6, 0, 0, s.getDriver().getTeamColor()));
                return c;
            }
        });
        table.getColumnModel().getColumn(1).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, col);
                Standing s = engine.getStandings().get(row);
                setHorizontalAlignment(JLabel.LEFT);
                setBackground(rowColor(s, bgColorFinal, gainedColor, lostColor));
                setForeground(fgColorFinal);
                setBorder(new EmptyBorder(0, 15, 0, 0));
                return c;
            }
        });
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(5).setCellRenderer(new TireRenderer(bgColor));

        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(bgColor);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        add(scroll, BorderLayout.CENTER);

        toggleGapBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showGap = !showGap;
                table.getColumnModel().getColumn(4).setHeaderValue(showGap ? "Gap" : "Interval");
                table.getTableHeader().repaint();
                model.fireTableDataChanged();
            }
        });

        final Timer timer = new Timer(1000, null);
        timer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                engine.advanceLap();
                if (engine.isFinished()) {
                    timer.stop();
                    startBtn.setEnabled(false);
                    pauseBtn.setEnabled(false);
                    statusLabel.setText("Race Finished");
                }
            }
        });

        startBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                timer.start();
                startBtn.setEnabled(false);
                pauseBtn.setEnabled(true);
            }
        });

        pauseBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                timer.stop();
                startBtn.setEnabled(true);
                pauseBtn.setEnabled(false);
            }
        });

        logoutBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                timer.stop();
                authController.handleLogout();
                onLogout.run();
            }
        });

        engine.setListener(new Runnable() {
            public void run() {
                lapLabel.setText("Lap " + engine.getCurrentLap() + "/" + engine.getTotalLaps());
                model.fireTableDataChanged();
            }
        });
    }

    private static Color rowColor(Standing s, Color defaultColor, Color gainedColor, Color lostColor) {
        switch (s.getPositionChange()) {
            case GAINED: return gainedColor;
            case LOST: return lostColor;
            default: return defaultColor;
        }
    }

    class RoundedButton extends JButton {
        private Color bg, fg, border;
        public RoundedButton(String text, Color bg, Color fg, Color border) {
            super(text);
            this.bg = bg;
            this.fg = fg;
            this.border = border;
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setForeground(fg);
            setFont(getFont().deriveFont(Font.BOLD));
            setPreferredSize(new Dimension(120, 30));
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(isEnabled() ? bg : Color.DARK_GRAY);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 30, 30));
            g2.setColor(border);
            g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 30, 30));
            g2.dispose();
            super.paintComponent(g);
        }
    }

    class TireRenderer extends JPanel implements javax.swing.table.TableCellRenderer {
        private String text = "";
        private Color bgColor;

        public TireRenderer(Color bg) {
            this.bgColor = bg;
            setOpaque(true);
            setBackground(bg);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            text = value != null ? value.toString() : "";
            return this;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Color pillColor = Color.WHITE;
            Color textColor = Color.BLACK;
            if (text.equals("Soft")) { pillColor = Color.RED; textColor = Color.WHITE; }
            else if (text.equals("Medium")) { pillColor = Color.YELLOW; textColor = Color.BLACK; }
            else if (text.equals("Hard")) { pillColor = Color.WHITE; textColor = Color.BLACK; }

            int w = 60, h = 24;
            int x = (getWidth() - w) / 2;
            int y = (getHeight() - h) / 2;

            g2.setColor(pillColor);
            g2.fill(new RoundRectangle2D.Float(x, y, w, h, h, h));

            g2.setColor(textColor);
            FontMetrics fm = g2.getFontMetrics();
            int stringWidth = fm.stringWidth(text);
            g2.drawString(text, x + (w - stringWidth) / 2, y + ((h - fm.getHeight()) / 2) + fm.getAscent());

            g2.setColor(new Color(100, 100, 100));
            g2.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);

            g2.dispose();
        }
    }
}