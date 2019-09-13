package org.sgdk.resourcemanager.entities;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.Icon;

import org.apache.batik.transcoder.TranscoderException;
import org.sgdk.resourcemanager.entities.exceptions.SGDKInvalidFormatException;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = SGDKElementDeserializer.class)
public abstract class SGDKElement {
	
	public enum Type{
		SGDKProject,
		SGDKFolder,
		SGDKBackground,
		SGDKSprite,
		SGDKFXSound,
		SGDKEnvironmentSound
	}

	private String path;
	
	private Type type;
	
	@JsonIgnore
	private SGDKElement parent = null;
	
	@JsonIgnore
	private Icon icon;
		
	public SGDKElement() {}
	
	public SGDKElement(JsonNode node) throws SGDKInvalidFormatException, IOException {
		this(node.get("path").asText());
	};
	
	public SGDKElement(String path) throws SGDKInvalidFormatException, IOException{
		this.path = path;
		try {
			File f = new File(path);
			if(!f.exists()) {
				throw new IOException("File " + path + " not found");
			}
		} catch (IOException e) {
			throw e;
		}
		if(!validateFormat(path)) throw new SGDKInvalidFormatException();
		init();
	}
	
	protected abstract void init() throws SGDKInvalidFormatException;
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	@Override
	public String toString() {
		return path.substring(path.lastIndexOf(File.separator)+1);
	}
	
	@JsonIgnore
	public String getName() {
		return path.substring(path.lastIndexOf(File.separator)+1,path.lastIndexOf("."));
	}

	public Icon getIcon() {
		if(icon == null) {
			try {
				icon = calculateIcon();
			} catch (TranscoderException | URISyntaxException e) {
				e.printStackTrace();
			}
		}
		return icon;
	}
	
	public void setIcon(Icon icon) {
		this.icon = icon;
	}
	
	public SGDKElement getParent() {
		return parent;
	}

	public void setParent(SGDKElement parent) {
		this.parent = parent;
	}
	
	public Type getType() {
		return type;
	}
	
	public void setType(Type type) {
		this.type = type;
	}
	
	@Override
	public boolean equals(Object other){
	    if (other == null) return false;
	    if (other == this) return true;
	    if (!(other instanceof SGDKElement))return false;
	    SGDKElement otherMyClass = (SGDKElement)other;
	    return getPath().equals(otherMyClass.getPath());
	}

	protected abstract boolean validateFormat(String path);
	
	protected abstract Icon calculateIcon() throws TranscoderException, URISyntaxException;
	
	public static String toString(String path) {
		return path.substring(path.lastIndexOf(File.separator)+1).replaceAll(" ", "_").toLowerCase();
	}

}
