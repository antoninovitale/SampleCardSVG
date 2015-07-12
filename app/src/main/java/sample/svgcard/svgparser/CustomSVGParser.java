package sample.svgcard.svgparser;

import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import sample.svgcard.svgparser.model.GNode;
import sample.svgcard.svgparser.model.ImageNode;
import sample.svgcard.svgparser.model.SVGNode;
import sample.svgcard.svgparser.model.TextNode;


public class CustomSVGParser extends DefaultHandler {
	public final static String TAG = CustomSVGParser.class.getSimpleName();
	private String xmlFilename;
	private InputStream xmlFileInputStream;
	private String tmpValue;
	private SVGNode svgNode;
	private GNode gNode;
	private ImageNode imageNode;
	private TextNode textNode;
	private String tref;
	private String currentNode = "";
	
	public CustomSVGParser(InputStream xmlFileInputStream) {	
		this.xmlFileInputStream = xmlFileInputStream;
	}

	/**
	 * parser di un file xml che contiene un oggetto svg
	 * 
	 * @return SVGNode
	 */
	public SVGNode parseDocument() {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			SAXParser parser = factory.newSAXParser();
			if(xmlFileInputStream!=null) {
				parser.parse(xmlFileInputStream, this);
			}
			else if(xmlFilename!=null) {
				parser.parse(xmlFilename, this);
			}
			return svgNode;
		} catch (ParserConfigurationException e) {
			Log.e(TAG, "ParserConfig error");
		} catch (SAXException e) {
			Log.e(TAG, "SAXException : xml not well formed");
		} catch (IOException e) {
			Log.e(TAG, "IO error");
		}
		return null;
	}

	private void printDatas() {
		Log.i(TAG, "svg read " + svgNode);
	}

	@Override
	public void startElement(String s, String s1, String elementName,
			Attributes attributes) throws SAXException {
		if (elementName.equalsIgnoreCase("svg")) {
			svgNode = new SVGNode();					
			svgNode.setId(attributes.getValue("id"));
			svgNode.setVersion(attributes.getValue("version"));
			svgNode.setWidth(attributes.getValue("width"));
			svgNode.setHeight(attributes.getValue("height"));	
			currentNode = "svg";
		}
		if (elementName.equalsIgnoreCase("g")) {
			gNode = new GNode();
			gNode.setDesc(attributes.getValue("desc"));
			gNode.setFill(attributes.getValue("fill"));
			gNode.setId(attributes.getValue("id"));
			gNode.setName(attributes.getValue("name"));
			gNode.setTitle(attributes.getValue("title"));
			currentNode = "g";
		}
		if (elementName.equalsIgnoreCase("image")) {
			imageNode = new ImageNode();
			imageNode.setId(attributes.getValue("id"));			
			imageNode.setXlink(attributes.getValue("xlink:href"));
			imageNode.setWidth(attributes.getValue("width"));
			imageNode.setHeight(attributes.getValue("height"));	
			imageNode.setX(attributes.getValue("x"));
			imageNode.setY(attributes.getValue("y"));
			currentNode = "image";
		}
		if (elementName.equalsIgnoreCase("text")) {
			textNode = new TextNode();
			textNode.setId(attributes.getValue("id"));
			textNode.setX(attributes.getValue("x"));
			textNode.setY(attributes.getValue("y"));
			textNode.setDx(attributes.getValue("dx"));
			textNode.setDy(attributes.getValue("dy"));
			textNode.setFontSize(attributes.getValue("font-size"));
			textNode.setFontFamily(attributes.getValue("font-family"));
			textNode.setFill(attributes.getValue("fill"));
			textNode.setLengthAdjust(attributes.getValue("lengthAdjust"));
			textNode.setRotate(attributes.getValue("rotate"));
			textNode.setTextLength(attributes.getValue("textLength"));
			currentNode = "text";
		}
		if (elementName.equalsIgnoreCase("tref") || elementName.equalsIgnoreCase("svg:tref")) {
			tref = new String();
			tref = attributes.getValue("xlink:href");
			currentNode = "text";
		}
		
	}

	@Override
	public void endElement(String s, String s1, String element)
			throws SAXException {		
		if (element.equals("svg")) {
			printDatas();
		}
		if (element.equalsIgnoreCase("g")) {
			svgNode.getG().add(gNode);
		}
		if (element.equalsIgnoreCase("image")) {
			gNode.setImage(imageNode);
		}
		if (element.equalsIgnoreCase("text")) {
			gNode.getText().add(textNode);
		}
		if (element.equalsIgnoreCase("tref") || element.equalsIgnoreCase("svg:tref")) {
			textNode.getTref().add(tref);
		}		
	}

	@Override
	public void characters(char[] ac, int i, int j) throws SAXException {
		tmpValue = new String(ac, i, j);
		tmpValue = tmpValue.trim();
		if(!tmpValue.equalsIgnoreCase("")) {
			Log.i(TAG, "tmpValue read " + tmpValue);
			if(currentNode.equalsIgnoreCase("svg")) svgNode.setCharacterData(svgNode.getCharacterData().concat(tmpValue));
			else if(currentNode.equalsIgnoreCase("g")) gNode.setCharacterData(gNode.getCharacterData().concat(tmpValue));
			else if(currentNode.equalsIgnoreCase("image")) imageNode.setCharacterData(imageNode.getCharacterData().concat(tmpValue));
			else if(currentNode.equalsIgnoreCase("text")) textNode.setCharacterData(textNode.getCharacterData().concat(tmpValue));
		}
	}
	
}
