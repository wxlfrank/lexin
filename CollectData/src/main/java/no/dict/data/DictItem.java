/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.dict.data;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import no.dict.utils.Constants;

/**
 *
 * @author wxlfr_000
 */
public class DictItem {

//	private int id = 0;
	private String alternative = "";

	private String clazz = "";

	private String comment = "";

	private String composite = "";

	private String error = "";

	private String examples = "";

	private String explain = "";

	private String format = "";

	private String grammer = "";

	private String phrases = "";

	private String sound = "";

	private String syllabel = "";

	private String url = "";

	private String word = "";

	public String getAlternative() {
		return alternative;
	}

	public String getClazz() {
		return clazz;
	}

	public String getComment() {
		return comment;
	}

	public String getComposite() {
		return composite;
	}

	public String getError() {
		return error;
	}

	public String getExamples() {
		return examples;
	}

	public String getExplain() {
		return explain;
	}

	public String getFormat() {
		return format;
	}

	public String getGrammer() {
		return grammer;
	}

	public String getPhrases() {
		return phrases;
	}

	public String getSound() {
		return sound;
	}

	public String getSyllabel() {
		return syllabel;
	}

	public String getWord() {
		return word;
	}

	public void setAlternative(String value) {
		alternative = value;
	}

	public void setClazz(String clazzFromDocument) {
		clazz = clazzFromDocument;
	}

	public void setComment(String value) {
		comment = comment.isEmpty() ? value : Constants.LINE + value;
	}

	public void setComposite(String value) {
		composite = value;
	}

	public void setError(String error) {
		this.error = error;
	}

	public void setError(String key, String value) {
		error = error.isEmpty() ? value : Constants.LINE + value;
	}

	public void setExamples(String value) {
		examples = examples.isEmpty() ? value : Constants.LINE + value;
	}

	public void setExplain(String value) {
		explain = explain.isEmpty() ? value : Constants.LINE + value;
	}

	public void setFormat(String value) {
		format = value;
	}

	public void setGrammer(String value) {
		grammer = grammer.isEmpty() ? value : Constants.LINE + value;
	}

	public void setPhrases(String value) {
		phrases = phrases.isEmpty() ? value : Constants.LINE + value;
	}

	public void setSound(String string) {
		sound = string;
	}

	public void setSyllabel(String string) {
		syllabel = string;
	}

	public void setURL(String url) {
		this.setUrl(url);
	}

	public void setWord(String value) {
		word = word.isEmpty() ? value : Constants.LINE + value;
	}

	public String toString() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		mapper.setSerializationInclusion(Include.NON_EMPTY);
		try {
			return mapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

//	public int getId() {
//		return id;
//	}
//
//	public void setId(int id) {
//		this.id = id;
//	}

	
}
