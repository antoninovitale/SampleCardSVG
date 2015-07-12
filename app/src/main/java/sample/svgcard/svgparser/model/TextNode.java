package sample.svgcard.svgparser.model;

import java.util.ArrayList;
import java.util.List;

public class TextNode {
    private String id;
    private String x;
    private String y;
    private String fontSize;
    private String fill;
    private String dx;
    private String dy;
    private String textLength;
    private String lengthAdjust;
    private String rotate;
    private String fontFamily;
    private List<String> tref;
    private String characterData;
    private GNode gNode;

    public TextNode() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getFontSize() {
        return fontSize;
    }

    public void setFontSize(String fontSize) {
        this.fontSize = fontSize;
    }

    public String getFill() {
        return fill;
    }

    public void setFill(String fill) {
        this.fill = fill;
    }

    public String getDx() {
        return dx;
    }

    public void setDx(String dx) {
        this.dx = dx;
    }

    public String getDy() {
        return dy;
    }

    public void setDy(String dy) {
        this.dy = dy;
    }

    public String getTextLength() {
        return textLength;
    }

    public void setTextLength(String textLength) {
        this.textLength = textLength;
    }

    public String getLengthAdjust() {
        return lengthAdjust;
    }

    public void setLengthAdjust(String lengthAdjust) {
        this.lengthAdjust = lengthAdjust;
    }

    public String getRotate() {
        return rotate;
    }

    public void setRotate(String rotate) {
        this.rotate = rotate;
    }

    public String getFontFamily() {
        return fontFamily;
    }

    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }

    public List<String> getTref() {
        if (tref == null) tref = new ArrayList<String>();
        return tref;
    }

    public void setTref(List<String> tref) {
        this.tref = tref;
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
        return "TextNode [id=" + id + ", x=" + x + ", y=" + y + ", fontSize="
                + fontSize + ", fill=" + fill + ", dx=" + dx + ", dy=" + dy
                + ", textLength=" + textLength + ", lengthAdjust="
                + lengthAdjust + ", rotate=" + rotate + ", fontFamily="
                + fontFamily + ", tref=" + tref + ", characterData="
                + characterData + "]";
    }

}
