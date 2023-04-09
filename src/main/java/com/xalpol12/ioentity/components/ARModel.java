package com.xalpol12.ioentity.components;

import com.xalpol12.xmlentity.Model;
import com.xalpol12.xmlentity.Node;

public class ARModel extends IOComponent {
    Model model;

    public ARModel(Node mainNode, Model model) {
        super(mainNode);
        this.model = model;
        mainNode.setModel(model);
    }
}
