package gui;

import exceptions.DependencyResolutionError;
import exceptions.NodeCreationException;
import exceptions.ParseException;
import gui.node.DependencyPanel;
import gui.node.NodePanel;
import gui.xml.HTMLPanel;
import gui.xml.XMLFilter;
import gui.xml.XMLHighlighter;
import models.Node;
import parser.DependencySolver;
import parser.NodeParser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class MainPanel extends JFrame {

    private NodeParser parser;
    private java.util.List<Node> nodes;

    private DependencyPanel dependencyPanel;
    private NodePanel nodePanel;
    private List<Node> resolvedNodes;

    private int index = 0;


    public MainPanel() {
        setLayout(new BorderLayout());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(1500, 800));
        setTitle("Dependency Solver");

        showFileChooser();
        try {
            setHTMLView();
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(this, "Couldn't open file");
            System.exit(0);
        }

        setVisible(true);
        setNodePanel();
        setDependencyPanel();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                stepUp();
            }
        });

    }

    private void stepUp() {
        if (dependencyPanel == null || nodePanel == null || resolvedNodes == null || index>resolvedNodes.size())
            return;
        else if(index == resolvedNodes.size()){
            nodePanel.setCompleted();
            dependencyPanel.setCompleted();

            if(dependencyPanel.getError())
                JOptionPane.showMessageDialog(null, "Cyclic Dependency can't be solved");
            return;
        }

        Node resolvedNode = resolvedNodes.get(index);
        nodePanel.addResolvedNode(resolvedNode);
        dependencyPanel.addResolvedNode(resolvedNode);

        index++;
    }

    private void setHTMLView() throws IOException {
        String highlighted = XMLHighlighter.highlight(parser.getRawContent());

        HTMLPanel htmlPanel = new HTMLPanel();
        htmlPanel.setHTML(highlighted);
        add(htmlPanel, BorderLayout.WEST);
    }

    private void setDependencyPanel() {
        boolean error = false;
        DependencySolver dependencySolver = new DependencySolver(nodes);

        try {
            resolvedNodes = dependencySolver.getResolvedNodes();
        } catch (DependencyResolutionError dre) {
            resolvedNodes = dre.getUnresolved();
            error = true;
        }

        for (Node node : resolvedNodes) {
            System.out.print(node.getName() + " -> ");
        }

        dependencyPanel = new DependencyPanel();
        dependencyPanel.setError(error);
        add(dependencyPanel, BorderLayout.EAST);
        revalidate();
        repaint();

        if (error) {
            System.out.println("ERROR");
        } else
            System.out.println("END");
    }

    private void setNodePanel() {
        try {
            nodes = parser.parse().generateGraph();
            nodePanel = new NodePanel(nodes);
            add(nodePanel, BorderLayout.CENTER);
        } catch (ParseException pe) {
            showEmptyLayout();
            JOptionPane.showMessageDialog(null, "Error parsing XML : " + pe.getMessage());
            System.exit(0);
        } catch (NodeCreationException ne) {
            showEmptyLayout();
            JOptionPane.showMessageDialog(null, "Error creating nodes : " + ne.getMessage());
            System.exit(0);
        }
    }

    private void showEmptyLayout() {
        NodePanel nodePanel = new NodePanel();
        add(nodePanel, BorderLayout.CENTER);
        DependencyPanel dependencyPanel = new DependencyPanel();
        add(dependencyPanel, BorderLayout.EAST);
        revalidate();
        repaint();
    }

    private void showFileChooser() {
        JFileChooser fileChooser = new JFileChooser();
        XMLFilter filter = new XMLFilter();
        fileChooser.addChoosableFileFilter(filter);
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setFileFilter(filter);
        fileChooser.setCurrentDirectory(new File("/home/iamareebjamal/IdeaProjects/NodeParser/"));

        int returnVal = fileChooser.showDialog(this, "Open Nodes XML");

        if (returnVal == JFileChooser.APPROVE_OPTION && filter.accept(fileChooser.getSelectedFile())) {
            parser = new NodeParser(fileChooser.getSelectedFile());
        } else {
            JOptionPane.showMessageDialog(null, "Invalid File Selection");
            System.exit(0);
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    System.setProperty("awt.useSystemAAFontSettings", "on");
                    System.setProperty("swing.aatext", "true");
                } catch (Exception e) {
                }

                new MainPanel();
            }
        });
    }

}
