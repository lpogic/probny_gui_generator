package model;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FXMLWriter {
    public static String xmlSettings = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    public static String buttonImport = "<?import javafx.scene.control.Button?>";
    public static String paneImport = "<?import javafx.scene.layout.Pane?>";

    private File file;
    private Set<String> imports;
    private StringBuilder nodeTree;

    public FXMLWriter(File file){
         this.file = file;
    }

    public FXMLWriter(String filePath){
        this.file = new File(filePath);
    }

    public void write(Node node) throws Exception{
        PrintWriter out = new PrintWriter(new FileWriter(file));
        imports = new HashSet<>();
        nodeTree = new StringBuilder();
        write(node,0);

        out.println(xmlSettings);
        out.println();
        for(String it : imports){
            out.println(it);
        }
        out.println();
        out.println(nodeTree.toString());
        out.close();
    }

    private void write(Node node, int tabs){
        if(node instanceof Pane)write((Pane)node,tabs);
        else if(node instanceof Button)write((Button)node, tabs);
    }

    private void write(Pane pane, int tabs){
        imports.add(paneImport);

        FXMLNode fxmlNode = new FXMLNode("Pane",true);
        fxmlNode.add("maxHeight","-Infinity");
        fxmlNode.add("maxWidth","-Infinity");
        fxmlNode.add("minHeight","-Infinity");
        fxmlNode.add("minWidth","-Infinity");
        fxmlNode.add("prefHeight",pane.getHeight());
        fxmlNode.add("prefWidth",pane.getWidth());
        fxmlNode.add("xmlns","http://javafx.com/javafx/10.0.1");

        addLine(fxmlNode.toString(),tabs);
        addLine("<children>",tabs+1);
        for(Node it : pane.getChildren()){
            write(it,tabs + 2);
        }
        addLine("</children>",tabs+1);
        addLine(fxmlNode.closeString(),tabs);
    }

    private void write(Button button, int tabs){
        imports.add(buttonImport);
        FXMLNode fxmlNode = new FXMLNode("Button",false);
        fxmlNode.add("layoutX",button.getLayoutX());
        fxmlNode.add("layoutY",button.getLayoutY());
        fxmlNode.add("text",button.getText());

        addLine(fxmlNode.toString(),tabs);
    }

    private void addLine(String string, int tabs){
        for(;tabs > 0;--tabs)nodeTree.append("\t");
        nodeTree.append(string).append("\n");
    }

    private class FXMLNode{
        private String name;
        private boolean container;
        private Map<String, Object> attributes;

        public FXMLNode(String name, boolean container) {
            this.name = name;
            this.container = container;
            this.attributes = new HashMap<>();
        }

        public void add(String attributeName, Object attributeValue){
            attributes.put(attributeName,attributeValue);
        }

        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder("<" + name);
            for(String it : attributes.keySet()){
                stringBuilder.append(" ").append(it).append("=\"")
                        .append(attributes.get(it)).append("\"");
            }
            stringBuilder.append(container ? ">" : "/>");
            return stringBuilder.toString();
        }

        public String closeString(){
            if(!container)return "";
            return "</" + name + ">";
        }

    }
}
