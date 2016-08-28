package parser;

import exceptions.NodeCreationException;
import exceptions.ParseException;
import models.Node;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class NodeParser {
    private String path;
    private List<Node> nodes = new ArrayList<>();

    public NodeParser(String path) {
        this.path = path;
    }

    public NodeParser(File file) {
        path = file.getPath();
    }

    public static void generateGraph(List<Node> nodes) throws NodeCreationException {
        for (Node node : nodes) {
            for (String dependencyName : node.getDependenciesName()) {
                Node dependency = getNodeFromName(nodes, dependencyName);
                if (dependency == null)
                    throw new NodeCreationException("Inadequate Nodes. Dependency information missing");

                node.addDependency(dependency);
                dependency.addDependent(node);
            }
        }
    }

    private static Node getNodeFromName(List<Node> nodes, String name) {
        for (Node node : nodes) {
            if (node.getName().equals(name))
                return node;
        }

        return null;
    }

    public String getRawContent() throws IOException {
        return new String(Files.readAllBytes(Paths.get(path)));
    }

    public NodeParser parse() throws ParseException, NodeCreationException {
        try {
            DocumentBuilderFactory dbf =
                    DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(path);
            NodeList elements = doc.getElementsByTagName("node");
            if (elements == null)
                throw new ParseException("No node present in XML");

            for (int i = 0; i < elements.getLength(); i++)
                nodes.add(createNodeFromElement((Element) elements.item(i)));

            validateNodeUniqueness();

        } catch (ParserConfigurationException pce) {
            throw new ParseException("There was an error in configuring the parser");
        } catch (IOException ioe) {
            throw new ParseException("There was an error opening the file");
        } catch (SAXException saxe) {
            throw new ParseException("SAXException occurred");
        }

        return this;
    }

    public List<Node> generateGraph() throws NodeCreationException {
        for (Node node : nodes) {
            for (String dependencyName : node.getDependenciesName()) {
                Node dependency = getNodeFromName(dependencyName);
                if (dependency == null)
                    throw new NodeCreationException("Inadequate Nodes. Dependency information missing");

                node.addDependency(dependency);
                dependency.addDependent(node);
            }
        }

        return nodes;
    }

    private Node createNodeFromElement(Element element) throws NodeCreationException {
        try {
            Node node = new Node(Integer.parseInt(element.getAttribute("id")));
            node.setName(element.getElementsByTagName("name").item(0).getTextContent());

            NodeList dependencies = element.getElementsByTagName("depends");
            if (dependencies == null)
                return node;

            List<String> names = new ArrayList<>();
            for (int i = 0; i < dependencies.getLength(); i++) {
                names.add(dependencies.item(i).getTextContent());
            }
            node.setDependencies(names);
            return node;
        } catch (NumberFormatException nfe) {
            throw new NodeCreationException("ID is either not provided or is not a number");
        } catch (NullPointerException npe) {
            throw new NodeCreationException("Name tag not provided");
        }
    }

    private Node getNodeFromName(String name) {
        for (Node node : nodes) {
            if (node.getName().equals(name))
                return node;
        }

        return null;
    }

    private void validateNodeUniqueness() throws NodeCreationException {

        for (Node node : nodes) {
            int id = 0;
            int name = 0;
            for (Node inner : nodes) {
                if (node.getId() == inner.getId())
                    id++;

                if (node.getName().equals(inner.getName()))
                    name++;
            }
            if (id > 1 || name > 1)
                throw new NodeCreationException("Found duplicate ID or name : \n" + node);
        }

    }
}
