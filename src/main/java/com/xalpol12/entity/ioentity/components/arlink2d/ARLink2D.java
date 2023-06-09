package com.xalpol12.entity.ioentity.components.arlink2d;

import com.xalpol12.entity.ioentity.components.IOComponent;
import com.xalpol12.entity.xmlentity.Link;
import com.xalpol12.entity.xmlentity.Model;
import com.xalpol12.entity.xmlentity.Node;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ARLink2D extends IOComponent {
    private Node subNode;
    private Model model;
    private Link link;

    public ARLink2D(Node mainNode, Node subNode, Model model, Link link) {
        super(mainNode);
        this.subNode = subNode;
        this.model = model;
        this.link = link;
        model.setLink(link);
        subNode.setModel(model);
        mainNode.setNodeList(Stream.of(subNode).collect(Collectors.toList()));  //java 8 does not support List.of() :/
    }
}
