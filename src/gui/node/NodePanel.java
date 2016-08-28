package gui.node;

import models.Node;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NodePanel extends JPanel {
    private DrawUtils drawUtils;
    private List<Node> nodes;
    private List<Node> ignored = new ArrayList<>();
    private Node highlighted = null;
    private Map<Node, Point> pointMap = new HashMap<>();

    public NodePanel() {
        setupView();
    }

    public NodePanel(List<Node> nodes) {
        this.nodes = nodes;
        setupView();
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
        repaint();
    }

    public void addResolvedNode(Node node) {
        highlighted = node;
        repaint();
        ignored.add(node);
    }

    public void setCompleted() {
        highlighted = null;
        repaint();
    }

    private void setupView() {
        setLayout(new BorderLayout());
        setBackground(DrawUtils.parseColor("#ffebee"));
        addTopPanel();
    }

    private void addTopPanel() {
        JLabel title = new JLabel("Node Structure View", SwingConstants.CENTER);
        Font font = title.getFont();
        Font boldFont = new Font(font.getFontName(), Font.BOLD, font.getSize());
        title.setFont(boldFont);
        title.setOpaque(true);
        title.setBorder(new EmptyBorder(10, 10, 10, 10));
        title.setBackground(DrawUtils.parseColor("#f44336"));
        title.setForeground(new Color(255, 255, 255, 180));
        add(title, BorderLayout.NORTH);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D graphics2d = (Graphics2D) g;
        graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        super.paintComponent(graphics2d);

        drawUtils = new DrawUtils(graphics2d);

        mapPolygon(graphics2d);

        if (nodes == null)
            return;

        for (Node node : nodes) {
            boolean bold = false;
            if(node==highlighted)
                bold = true;
            else if(ignored.contains(node))
                continue;

            for (Node dep : node.getDependents()) {
                if(bold)
                    drawUtils.drawBoldEdge(pointMap.get(dep), pointMap.get(node));
                else
                    drawUtils.drawEdge(pointMap.get(dep), pointMap.get(node));
            }
        }

        for (Node node : nodes) {
            if(node == highlighted)
                drawUtils.drawHighlightedNode(pointMap.get(node));
            else if(ignored.contains(node))
                continue;
            drawUtils.drawNode(pointMap.get(node), node.getName());
        }

    }

    private void mapPolygon(Graphics2D graphics2D) {
        int n;
        if (nodes == null)
            n = 7;
        else
            n = nodes.size();

        int x[] = new int[n];
        int y[] = new int[n];
        int index = 0;

        drawUtils.drawHalo(new Point(getHeight() / 2, getWidth() / 2));

        for (Point point : DrawUtils.getPolygonPoints(new Point(getHeight() / 2, getWidth() / 2), 300, n)) {
            x[index] = point.x;
            y[index] = point.y;

            if (nodes != null) {
                pointMap.put(nodes.get(index), point);
            }

            index++;
        }

        graphics2D.setColor(DrawUtils.parseColor("#ffcdd2"));
        graphics2D.fillPolygon(x, y, n);
        drawUtils.drawSmallHalo(new Point(getHeight() / 2, getWidth() / 2));
    }
}
