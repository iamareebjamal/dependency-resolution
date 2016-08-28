package gui.node;

import models.Node;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DependencyPanel extends JPanel {

    private List<Node> resolvedNodes = new ArrayList<>();
    private boolean error = false;
    private boolean isCompleted = false;


    public DependencyPanel() {
        setup();
    }

    public boolean getError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public void setCompleted() {
        isCompleted = true;
        repaint();
    }

    public void setResolvedNodes(List<Node> resolvedNodes) {
        this.resolvedNodes = resolvedNodes;
        repaint();
    }

    public void addResolvedNode(Node node) {
        resolvedNodes.add(node);
        repaint();
    }

    private void setup() {
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Resolved Dependencies", SwingConstants.CENTER);
        Font font = title.getFont();
        Font boldFont = new Font(font.getFontName(), Font.BOLD, font.getSize());
        title.setFont(boldFont);
        title.setOpaque(true);
        title.setBorder(new EmptyBorder(10, 10, 10, 10));
        title.setBackground(DrawUtils.parseColor("#2196F3"));
        title.setForeground(new Color(255, 255, 255, 180));

        setBackground(Color.BLACK);
        add(title, BorderLayout.NORTH);
        setScrollView();
    }

    public void setScrollView() {
        DependencyDrawPanel dependencyDrawPanel = new DependencyDrawPanel();
        dependencyDrawPanel.setBackground(DrawUtils.parseColor("#E3F2FD"));
        dependencyDrawPanel.setPreferredSize(new Dimension(350, 5000));

        JScrollPane scrollPane = new JScrollPane(dependencyDrawPanel);
        scrollPane.setPreferredSize(new Dimension(350, 500));
        scrollPane.setViewportBorder(null);
        scrollPane.setBorder(null);
        add(scrollPane);
    }

    class DependencyDrawPanel extends JPanel {
        private DrawUtils drawUtils;

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D graphics2d = (Graphics2D) g;
            graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            graphics2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            super.paintComponent(graphics2d);

            drawUtils = new DrawUtils(graphics2d);

            if (resolvedNodes == null || resolvedNodes.isEmpty())
                return;

            int x = getWidth() / 2, y = 0;
            for (Node node : resolvedNodes) {
                Point from = new Point(x, y += 100);
                Point to = new Point(x, y + 100);
                drawUtils.drawEdge(from, to);
                drawUtils.drawNode(from, node.getName(), DrawUtils.parseColor("#2196F3"), DrawUtils.parseColor("#BBDEFB"));
            }

            if(!isCompleted)
                return;

            if (error)
                drawUtils.drawNode(new Point(x, y + 100), "ERROR");
            else
                drawUtils.drawNode(new Point(x, y + 100), "END", DrawUtils.parseColor("#00C853"), DrawUtils.parseColor("#B9F6CA"));
        }
    }
}
