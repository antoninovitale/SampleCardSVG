package sample.svgcard.svgparser.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SVGNode {

	private String height;

	private String width;

	private String version;

	private String id;

	private Collection<GNode> g;

	private String characterData;

	public SVGNode() {
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<GNode> getG() {
		if(g==null) g = new ArrayList<GNode>();
		return (List<GNode>) g;
	}

	public void setG(List<GNode> g) {
		this.g = g;
	}

	public String getCharacterData() {
		if(characterData==null) characterData = "";
		return characterData;
	}

	public void setCharacterData(String characterData) {
		this.characterData = characterData;
	}

	@Override
	public String toString() {
		return "SVGNode [height=" + height + ", width=" + width + ", version="
				+ version + ", id=" + id + ", g=" + g + ", characterData="
				+ characterData + "]";
	}
	
}
