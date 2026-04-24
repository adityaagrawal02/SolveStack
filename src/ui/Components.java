package ui;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.border.*;

public class Components {

    // ── Pill badge ────────────────────────────────────────────────────────────
    public static JLabel badge(String text, Color bg, Color fg) {
        JLabel l = new JLabel(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        l.setFont(Theme.FONT_SMALL);
        l.setForeground(fg);
        l.setOpaque(false);
        l.setBorder(new EmptyBorder(5, 11, 5, 11));
        return l;
    }

    public static JLabel roleBadge(String role) {
        String normalizedRole = DashboardRouter.normalizeRole(role);
        return switch (normalizedRole) {
            case "Developer" -> badge(normalizedRole, Theme.GREEN_BG,   Theme.GREEN_TEXT);
            case "Evaluator" -> badge(normalizedRole, Theme.AMBER_BG,   Theme.AMBER_TEXT);
            case "Admin"     -> badge(normalizedRole, Theme.CORAL_BG,   Theme.CORAL_TEXT);
            default          -> badge(normalizedRole, Theme.PRIMARY_LIGHT, Theme.PRIMARY);
        };
    }

    // ── Avatar circle ─────────────────────────────────────────────────────────
    public static JPanel avatar(String initials, Color bg, Color fg) {
        return new JPanel() {
            { setPreferredSize(new Dimension(36, 36)); setOpaque(false); }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(15, 23, 42, 22));
                g2.fillOval(0, 2, 35, 35);
                g2.setColor(bg);
                g2.fillOval(0, 0, 35, 35);
                g2.setColor(fg);
                g2.setFont(Theme.FONT_SMALL.deriveFont(Font.BOLD));
                FontMetrics fm = g2.getFontMetrics();
                int x = (35 - fm.stringWidth(initials)) / 2;
                int y = (35 + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(initials, x, y);
                g2.dispose();
            }
        };
    }

    // ── Primary / secondary buttons ───────────────────────────────────────────
    public static JButton primaryBtn(String text) {
        JButton b = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color fill = getModel().isPressed() ? Theme.PRIMARY_DARK
                        : getModel().isRollover() ? Theme.PRIMARY.brighter() : Theme.PRIMARY;
                g2.setColor(fill);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));

                g2.setColor(new Color(255, 255, 255, 42));
                g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, getWidth() - 1f, getHeight() - 1f, 12, 12));

                g2.setColor(Color.WHITE);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int tx = (getWidth() - fm.stringWidth(getText())) / 2;
                int ty = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), tx, ty);
                g2.dispose();
            }
        };
        b.setFont(Theme.FONT_BODY.deriveFont(Font.BOLD));
        b.setForeground(Color.WHITE);
        b.setPreferredSize(new Dimension(160, 42));
        styleButton(b);
        return b;
    }

    public static JButton outlineBtn(String text) {
        JButton b = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(getModel().isRollover() ? Theme.PRIMARY_LIGHT : Theme.BG_WHITE);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 10, 10));
                g2.setColor(getModel().isRollover() ? Theme.PRIMARY : Theme.BORDER);
                g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, getWidth() - 2f, getHeight() - 2f, 10, 10));

                g2.setColor(Theme.TEXT_PRIMARY);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int tx = (getWidth() - fm.stringWidth(getText())) / 2;
                int ty = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), tx, ty);
                g2.dispose();
            }
        };
        b.setFont(Theme.FONT_BODY);
        b.setPreferredSize(new Dimension(120, 38));
        styleButton(b);
        return b;
    }

    public static JButton smallBtn(String text) {
        JButton b = outlineBtn(text);
        b.setFont(Theme.FONT_SMALL);
        b.setPreferredSize(new Dimension(96, 30));
        return b;
    }

    // ── Card panel ────────────────────────────────────────────────────────────
    public static JPanel card() {
        JPanel p = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(15, 23, 42, 12));
                g2.fillRoundRect(0, 2, getWidth() - 1, getHeight() - 2, 18, 18);

                GradientPaint gp = new GradientPaint(0, 0, new Color(255, 255, 255, 245),
                        0, getHeight(), new Color(248, 251, 255, 245));
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 3, 18, 18);

                g2.setColor(Theme.BORDER);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 3, 18, 18);
                g2.dispose();
            }
        };
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(22, 24, 22, 24));
        return p;
    }

    // ── Top nav bar ───────────────────────────────────────────────────────────
    public static JPanel navbar(String role, ActionListener onLogout) {
        JPanel nav = new JPanel(new BorderLayout());
        nav.setBackground(Theme.BG_WHITE);
        nav.setBorder(new MatteBorder(0, 0, 1, 0, Theme.BORDER));
        nav.setPreferredSize(new Dimension(0, 64));

        // brand
        JPanel brand = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        brand.setOpaque(false);
        brand.setBorder(new EmptyBorder(0, 16, 0, 0));
        JPanel dot = new JPanel() {
            { setPreferredSize(new Dimension(10, 10)); setOpaque(false); }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Theme.PRIMARY_DARK);
                g2.fillOval(1, 3, 7, 7);
                g2.setColor(Theme.PRIMARY);
                g2.fillOval(4, 0, 7, 7);
            }
        };
        JLabel title = new JLabel("  SolveStack");
        title.setFont(Theme.FONT_HEAD);
        title.setForeground(Theme.TEXT_PRIMARY);
        brand.add(dot);
        brand.add(title);

        // right side
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        right.setOpaque(false);
        String normalizedRole = DashboardRouter.normalizeRole(role);
        right.add(roleBadge(normalizedRole));

        String[] initials = {"Company", "Developer", "Evaluator", "Admin"};
        String[][] avatarData = {{"AC", "#D8E6FA", "#134A84"}, {"DV", "#D1E3FC", "#124A84"},
                  {"EV", "#FFE7C1", "#8D5A12"}, {"AD", "#F9D8CE", "#8F452A"}};
        int idx = java.util.Arrays.asList(initials).indexOf(normalizedRole);
        if (idx < 0) idx = 0;
        Color avBg  = Color.decode(avatarData[idx][1]);
        Color avFg  = Color.decode(avatarData[idx][2]);
        right.add(avatar(avatarData[idx][0], avBg, avFg));

        JButton logout = smallBtn("Sign out");
        logout.addActionListener(onLogout);
        right.add(logout);

        nav.add(brand, BorderLayout.WEST);
        nav.add(right,  BorderLayout.EAST);
        return nav;
    }

    // ── Metric card ───────────────────────────────────────────────────────────
    public static JPanel metric(String label, String value) {
        JPanel p = new JPanel(new BorderLayout(0, 6));
        p.setBackground(Theme.BG_SECONDARY);
        p.setBorder(new CompoundBorder(new LineBorder(Theme.BORDER, 1, true), new EmptyBorder(14, 14, 14, 14)));

        JLabel lbl = new JLabel(label);
        lbl.setFont(Theme.FONT_SMALL);
        lbl.setForeground(Theme.TEXT_MUTED);

        JLabel val = new JLabel(value);
        val.setFont(Theme.FONT_METRIC);
        val.setForeground(Theme.TEXT_PRIMARY);

        p.add(lbl, BorderLayout.NORTH);
        p.add(val, BorderLayout.CENTER);
        return p;
    }

    // ── Section header ────────────────────────────────────────────────────────
    public static JLabel sectionHeader(String text) {
        JLabel l = new JLabel(text);
        l.setFont(Theme.FONT_HEAD);
        l.setForeground(Theme.TEXT_PRIMARY);
        return l;
    }

    // ── Horizontal separator ─────────────────────────────────────────────────
    public static JSeparator sep() {
        JSeparator s = new JSeparator();
        s.setForeground(Theme.BORDER);
        return s;
    }

    // ── Styled text field ─────────────────────────────────────────────────────
    public static JTextField textField(String placeholder) {
        JTextField f = new JTextField();
        f.setFont(Theme.FONT_BODY);
        f.setBorder(new CompoundBorder(
                new RoundedBorder(12, Theme.BORDER),
                new EmptyBorder(10, 14, 10, 14)));
        f.setForeground(Theme.TEXT_MUTED);
        f.setText(placeholder);
        f.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (f.getText().equals(placeholder)) { f.setText(""); f.setForeground(Theme.TEXT_PRIMARY); }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (f.getText().isEmpty()) { f.setText(placeholder); f.setForeground(Theme.TEXT_MUTED); }
            }
        });
        return f;
    }

    public static JPasswordField passwordField() {
        JPasswordField f = new JPasswordField();
        f.setFont(Theme.FONT_BODY);
        f.setBorder(new CompoundBorder(
                new RoundedBorder(12, Theme.BORDER),
                new EmptyBorder(10, 14, 10, 14)));
        return f;
    }

    public static JTextArea textArea(String placeholder) {
        JTextArea a = new JTextArea(4, 20);
        a.setFont(Theme.FONT_BODY);
        a.setLineWrap(true);
        a.setWrapStyleWord(true);
        a.setBorder(new EmptyBorder(10, 14, 10, 14));
        a.setForeground(Theme.TEXT_MUTED);
        a.setText(placeholder);
        a.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (a.getText().equals(placeholder)) { a.setText(""); a.setForeground(Theme.TEXT_PRIMARY); }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (a.getText().isEmpty()) { a.setText(placeholder); a.setForeground(Theme.TEXT_MUTED); }
            }
        });
        return a;
    }

    // ── Form label ────────────────────────────────────────────────────────────
    public static JLabel formLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(Theme.FONT_SMALL);
        l.setForeground(Theme.TEXT_MUTED);
        return l;
    }

    // ── Notification banner ───────────────────────────────────────────────────
    public static JLabel notifBanner(String text) {
        JLabel l = new JLabel(text);
        l.setFont(Theme.FONT_BODY);
        l.setForeground(Theme.PRIMARY_DARK);
        l.setBackground(Theme.PRIMARY_LIGHT);
        l.setOpaque(true);
        l.setBorder(new CompoundBorder(
            new LineBorder(Theme.BORDER, 1, true),
                new EmptyBorder(10, 14, 10, 14)));
        return l;
    }

    // ── Scroll pane ───────────────────────────────────────────────────────────
    public static JScrollPane scroll(JComponent c) {
        JScrollPane sp = new JScrollPane(c);
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.getVerticalScrollBar().setUnitIncrement(12);
        return sp;
    }

    private static void styleButton(JButton b) {
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                b.repaint();
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                b.repaint();
            }
        });
    }

    private static class RoundedBorder extends AbstractBorder {
        private final int radius;
        private final Color color;

        RoundedBorder(int radius, Color color) {
            this.radius = radius;
            this.color = color;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2.dispose();
        }
    }
}
