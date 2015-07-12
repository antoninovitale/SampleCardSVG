package sample.svgcard.svgparser.model;

public class ImageNode {
    private String width;
    private String height;
    private String x;
    private String y;
    private String id;
    private String xlink;
    private String characterData;

    public ImageNode() {
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getXlink() {
        return xlink;
    }

    public void setXlink(String xlink) {
        this.xlink = xlink;
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
        return "ImageNode [width=" + width + ", height=" + height + ", x=" + x
                + ", y=" + y + ", id=" + id + ", xlink=" + xlink
                + ", characterData=" + characterData + "]";
    }

}
