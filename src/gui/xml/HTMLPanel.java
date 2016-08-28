package gui.xml;


import sun.swing.SwingUtilities2;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class HTMLPanel extends JPanel implements MouseWheelListener {

    private JTextPane textPane;
    private String html;

    private int fontSize = 18;

    public HTMLPanel() {
        setup();
    }

    public HTMLPanel(String html) {
        setup();
        setHTML(html);
    }

    private void setup() {
        setLayout(new BorderLayout());

        JLabel title = new JLabel("XML View", SwingConstants.CENTER);
        Font font = title.getFont();
        Font boldFont = new Font(font.getFontName(), Font.BOLD, font.getSize());
        title.setFont(boldFont);
        title.setOpaque(true);
        title.setBorder(new EmptyBorder(10, 10, 10, 10));
        title.setBackground(new Color(156, 39, 176));
        title.setForeground(new Color(255, 255, 255, 180));

        add(title, BorderLayout.NORTH);
    }

    public void setHTML(String html) {
        this.html = html;

        textPane = new JTextPane() {
            @Override
            public void paintComponent(Graphics g) {
                Graphics2D graphics2d = (Graphics2D) g;
                graphics2d.setRenderingHint(
                        RenderingHints.KEY_TEXT_ANTIALIASING,
                        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                graphics2d.setRenderingHint(
                        RenderingHints.KEY_RENDERING,
                        RenderingHints.VALUE_RENDER_QUALITY);

                super.paintComponent(graphics2d);
            }

        };

        textPane.setContentType("text/html");
        textPane.setText(html);
        textPane.setMargin(new Insets(0, 10, 0, 20));
        textPane.setBackground(new Color(243, 229, 245));
        textPane.putClientProperty(SwingUtilities2.AA_TEXT_PROPERTY_KEY, null);
        textPane.setEditable(false);
        updateFont();


        JScrollPane scrollPane = new JScrollPane(textPane);
        scrollPane.setPreferredSize(new Dimension(350, 500));
        scrollPane.setViewportBorder(null);
        scrollPane.setBorder(null);
        add(scrollPane);

        scrollPane.addMouseWheelListener(this);
    }

    private void updateFont() {
        textPane.setFont(new Font("monospace", Font.PLAIN, fontSize));
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (!e.isControlDown())
            return;

        int rot = e.getWheelRotation();
        if (rot < 0) {
            fontSize++;
        } else {
            if (fontSize > 8)
                fontSize--;
        }

        updateFont();
    }
}
