package sample.svgcard.svgparser.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GNode {

    private String id;

    private String desc;

    private String title;

    private String fill;

    private String name;

    private Collection<TextNode> text;

    private ImageNode image;

    private String characterData;

    private SVGNode svgNode;

    public GNode() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFill() {
        return fill;
    }

    public void setFill(String fill) {
        this.fill = fill;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TextNode> getText() {
        if (text == null) text = new ArrayList<TextNode>();
        return (List<TextNode>) text;
    }

    public void setText(List<TextNode> text) {
        this.text = text;
    }

    public ImageNode getImage() {
        return image;
    }

    public void setImage(ImageNode image) {
        this.image = image;
    }

    public String getCharacterData() {
        if (characterData == null) characterData = "";
        return characterData;
    }

    public void setCharacterData(String characterData) {
        this.characterData = characterData;
    }

    @Override
    public String toString() {
        return "GNode [id=" + id + ", desc=" + desc + ", title=" + title
                + ", fill=" + fill + ", name=" + name + ", text=" + text
                + ", image=" + image + ", characterData=" + characterData + "]";
    }

}
