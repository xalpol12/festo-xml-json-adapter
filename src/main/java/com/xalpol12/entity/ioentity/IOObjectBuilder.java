package com.xalpol12.entity.ioentity;

import com.xalpol12.entity.ioentity.components.arnode.ARNodeBuilder;
import com.xalpol12.entity.ioentity.components.arnode.ARNodeDirector;
import com.xalpol12.entity.ioentity.components.arstaticimage.ARStaticImageBuilder;
import com.xalpol12.entity.ioentity.components.arlink.ARLinkBuilder;
import com.xalpol12.entity.ioentity.components.arlink.ARLinkDirector;
import com.xalpol12.entity.ioentity.components.arlink2d.ARLink2DBuilder;
import com.xalpol12.entity.ioentity.components.arlink2d.ARLink2DDirector;
import com.xalpol12.entity.ioentity.components.armodel.ARModelBuilder;
import com.xalpol12.entity.ioentity.components.armodel.ARModelDirector;
import com.xalpol12.entity.ioentity.components.arnode.ARNode;
import com.xalpol12.entity.ioentity.components.arstaticimage.ARStaticImageDirector;
import com.xalpol12.entity.ioentity.components.artext.ARTextBuilder;
import com.xalpol12.entity.ioentity.components.artext.ARTextDirector;
import com.xalpol12.entity.jsonentity.Vector3;
import com.xalpol12.entity.xmlentity.Node;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IOObjectBuilder {
    private final Float tx;
    private final Float ty;
    private final Float tz;
    private final Float rx;
    private final Float ry;
    private final Float rz;
    private final Float sx;
    private final Float sy;
    private final Float sz;
    private final String label;
    private final String url;
    private final String view;
    private final String objectView;
    private final String viewExcludingObject;
    private final String objectRefer;
    private String collapse;
    private String show;
    private Node openDetails;
    private Node arText;
    private Node staticImage;
    private Node details;
    private Node activeLink;
    private Node wireframe;
    private Node mainNode;

    public IOObjectBuilder(Vector3 position, Vector3 rotation, Vector3 scale, String label, String url, List<String> view) {
        this.tx = position.getX();
        this.ty = position.getY();
        this.tz = position.getZ();

        this.rx = rotation.getX();
        this.ry = rotation.getY();
        this.rz = rotation.getZ();

        this.sx = scale.getX();
        this.sy = scale.getY();
        this.sz = scale.getZ();

        this.url = url;

        this.label = label.toUpperCase();
        this.view = view.toString()
                .replace("[", "")
                .replace("]", "");

        collapse = "";
        show = "";

        if (view.contains(label.toLowerCase())) {
            viewExcludingObject = view.remove(view.indexOf(label.toLowerCase()));
        } else viewExcludingObject = this.view;

        objectView = "joining_" + label.toLowerCase() + "Details_i";
        objectRefer = "@view:" + objectView;

        mainNode = new Node();
    }

    public void setOpenDetails() {
        ARLinkBuilder builder = new ARLinkBuilder(viewExcludingObject, objectRefer);
        ARLinkDirector director = new ARLinkDirector();
        director.constructOpenDetails(builder);
        openDetails = builder.getComponent();
    }

    public void setText() {
        ARTextBuilder builder = new ARTextBuilder(viewExcludingObject, label); // TODO: view without current object
        ARTextDirector director =  new ARTextDirector();
        director.constructTextNode(builder);
        arText = builder.getComponent();
    }

    public void setStaticImage() {
        ARStaticImageBuilder builder = new ARStaticImageBuilder(view);
        ARStaticImageDirector director = new ARStaticImageDirector();
        director.constructStaticImage(builder);
        staticImage = builder.getComponent();
    }

    private Node setMenuName() {
        ARTextBuilder builder = new ARTextBuilder(objectView, label);
        ARTextDirector director = new ARTextDirector();
        director.constructMenuName(builder);
        return builder.getComponent();
    }

    private Node setDatasheet() {
        ARLink2DBuilder builder = new ARLink2DBuilder(objectView, url);
        ARLink2DDirector director = new ARLink2DDirector();
        director.constructDatasheet(builder);
        return builder.getComponent();
    }

    public void setDetails() {
        Node menuName = setMenuName();
        Node datasheet = setDatasheet();
        ARNodeBuilder builder = new ARNodeBuilder(objectView);
        ARNodeDirector director = new ARNodeDirector();
        director.constructDetails(builder, menuName, datasheet);
        details = builder.getComponent();
    }

    public void setActiveLink() {
        String refer = "@view:joining_inputs"; // TODO: dynamically create refer variables
        ARLinkBuilder builder = new ARLinkBuilder(objectView, refer);
        ARLinkDirector director = new ARLinkDirector();
        director.constructActiveLink(builder);
        activeLink = builder.getComponent();
    }

    private Node setInactive() {
        ARModelBuilder builder = new ARModelBuilder(viewExcludingObject); // TODO: view without current viewModel
        ARModelDirector director = new ARModelDirector();
        director.constructInactive(builder);
        return builder.getComponent();
    }

    private Node setActive() {
        ARModelBuilder builder = new ARModelBuilder(objectView);
        ARModelDirector director = new ARModelDirector();
        director.constructActive(builder);
        return builder.getComponent();
    }

    public void setWireframe() {
        Node inactive = setInactive();
        Node active = setActive();
        ARNodeBuilder builder = new ARNodeBuilder(view);
        ARNodeDirector director = new ARNodeDirector();
        director.constructWireframe(builder, inactive, active);
        wireframe = builder.getComponent();
    }

    public void setMainNode() {
        mainNode.setTx(tx);
        mainNode.setTy(ty);
        mainNode.setTz(tz);
        mainNode.setView(view);
        mainNode.setCollapse(collapse);
        mainNode.setShow(show);
    }

    public Node getObject() {
        return new ARNode(mainNode, Stream.of(openDetails, arText,
                staticImage, details, activeLink, wireframe)
                .collect(Collectors.toList())).getMainNode();
    }
}
