package ui;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;

/**
 * SolveStack logo panel — isometric stacked blocks with a checkmark.
 *
 * Usage variants:
 *   new LogoPanel()            → default: icon + "SolveStack" text, 160×36
 *   new LogoPanel(true)        → icon only (for tight spaces), 36×36
 *   new LogoPanel(false, 28f)  → custom font size
 */
public class LogoPanel extends JPanel {

    private final boolean iconOnly;
    private final float   fontSize;

    /** Default: icon + wordmark */
    public LogoPanel() {
        this(false, 20f);
    }

    /** Icon-only variant */
    public LogoPanel(boolean iconOnly) {
        this(iconOnly, 20f);
    }

    /** Full control */
    public LogoPanel(boolean iconOnly, float fontSize) {
        this.iconOnly = iconOnly;
        this.fontSize = fontSize;
        setOpaque(false);
        int w = iconOnly ? 36 : 320;
        setPreferredSize(new Dimension(w, 40));
        setMaximumSize(new Dimension(w, 40));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // ── Isometric stacked blocks ──────────────────────────────────────
        // Each block is 3 polygons: top face, left face, right face
        // Coordinate origin: top-left of this panel, icon fits in ~32×32

        int ox = 2;   // x offset
        int oy = 2;   // y offset

        // Colors derived from Theme
        Color topLight   = new Color(191, 219, 254);  // light blue top
        Color topMid     = new Color(147, 197, 253);  // mid blue top
        Color topBright  = new Color(255, 255, 255, 200); // highlight top

        Color leftDark   = Theme.PRIMARY_DARK;         // darkest face
        Color rightMid   = Theme.PRIMARY;              // mid face

        // Block dimensions (isometric)
        // We draw 3 layers. Bottom → top.

        // ── Bottom layer ──
        drawBlock(g2,
            ox, oy + 20,
            topLight, leftDark, rightMid,
            false);

        // ── Middle layer ──
        drawBlock(g2,
            ox, oy + 12,
            topMid, leftDark, rightMid,
            false);

        // ── Top layer ──
        drawBlock(g2,
            ox, oy + 4,
            new Color(219, 234, 254),
            leftDark, rightMid,
            true);  // checkmark on top

        g2.dispose();

        // ── Wordmark ─────────────────────────────────────────────────────
        if (!iconOnly) {
            Graphics2D g3 = (Graphics2D) g.create();
            g3.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                                RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

            Font base = Theme.FONT_HEAD.deriveFont(Font.BOLD, fontSize);

            // "Solve" in PRIMARY blue
            g3.setFont(base);
            g3.setColor(Theme.PRIMARY);
            g3.drawString("Solve", 38, (int)(getHeight() * 0.72f));

            FontMetrics fm = g3.getFontMetrics();
            int solveW = fm.stringWidth("Solve");

            // "Stack" in TEXT_PRIMARY dark
            g3.setColor(Theme.TEXT_PRIMARY);
            g3.drawString("Stack", 38 + solveW, (int)(getHeight() * 0.72f));

            g3.dispose();
        }
    }

    /**
     * Draws one isometric block layer.
     * @param bx     left-most x of block
     * @param by     y of the top-face apex
     * @param top    top face color
     * @param left   left face color
     * @param right  right face color
     * @param check  whether to draw checkmark on top face
     */
    private void drawBlock(Graphics2D g2,
                           int bx, int by,
                           Color top, Color left, Color right,
                           boolean check) {

        // Block geometry (flat isometric, 28 wide, 7 tall per face)
        int W  = 28;  // total width
        int HT = 6;   // top face height
        int HF = 6;   // front face height

        int midX = bx + W / 2;

        // Top face: diamond/parallelogram
        int[] tx = { midX,      bx + W, midX,      bx     };
        int[] ty = { by,        by + HT, by + HT*2, by + HT };
        g2.setColor(top);
        g2.fillPolygon(tx, ty, 4);
        g2.setColor(top.darker());
        g2.drawPolygon(tx, ty, 4);

        // Left face
        int[] lx = { bx,     midX,      midX,      bx     };
        int[] ly = { by + HT, by + HT*2, by + HT*2 + HF, by + HT + HF };
        g2.setColor(left);
        g2.fillPolygon(lx, ly, 4);
        g2.setColor(left.darker());
        g2.drawPolygon(lx, ly, 4);

        // Right face
        int[] rx = { midX,      bx + W, bx + W,      midX      };
        int[] ry = { by + HT*2, by + HT, by + HT + HF, by + HT*2 + HF };
        g2.setColor(right);
        g2.fillPolygon(rx, ry, 4);
        g2.setColor(right.darker());
        g2.drawPolygon(rx, ry, 4);

        // Checkmark on top face
        if (check) {
            g2.setColor(Theme.PRIMARY_DARK);
            g2.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            // Checkmark points mapped to top-face diamond center
            int cx = midX;
            int cy = by + HT;
            g2.drawLine(cx - 6, cy + 2,  cx - 2, cy + 5);
            g2.drawLine(cx - 2, cy + 5,  cx + 6, cy - 2);
            g2.setStroke(new BasicStroke(1f));
        }
    }
}