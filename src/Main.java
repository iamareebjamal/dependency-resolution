import exceptions.DependencyResolutionError;
import exceptions.NodeCreationException;
import exceptions.ParseException;
import models.Node;
import parser.DependencySolver;
import parser.NodeParser;

import java.util.List;
import java.util.Queue;

public class Main {
    public static void main(String[] args) {
        String filePath = "/home/iamareebjamal/IdeaProjects/NodeParser/nodes.xml";

        try {
            NodeParser nodeParser = new NodeParser(filePath);
            List<Node> nodeList = nodeParser.parse().generateGraph();

            DependencySolver ds = new DependencySolver(nodeList);
            List<Node> resolvedNodes = ds.getResolvedNodes();

            for (Node node : resolvedNodes) {
                System.out.print(node.getName() + " -> ");
            }
            System.out.println("END");

        } catch (ParseException pe) {
            System.out.println("Error parsing XML : " + pe.getMessage());
        } catch (NodeCreationException ne) {
            System.out.println("Error creating nodes : " + ne.getMessage());
        } catch (DependencyResolutionError dre) {
            System.out.println(dre);

            for (Node node : dre.getUnresolved()) {
                System.out.print(node.getName() + " -> ");
            }
            System.out.println("ERROR");
        }

    }
}
