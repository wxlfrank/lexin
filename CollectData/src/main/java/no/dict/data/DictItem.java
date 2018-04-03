/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.dict.data;

import no.dict.utils.Constants;

/**
 *
 * @author wxlfr_000
 */
public class DictItem {

	private int id = 0;
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

	public DictItem() {
	}

	public String getAlternative() {
		return alternative;
	}

	public String getChange() {
		return format;
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
		if (alternative.isEmpty())
			alternative = value;
		else
			alternative = alternative + "|" + value;
	}

	public void setClazz(String clazzFromDocument) {
		clazz = clazzFromDocument;
	}

	public void setComment(String value) {
		comment = value;
	}

	public void setComposite(String value) {
		if (composite.isEmpty())
			composite = value;
		else
			composite = composite + "|" + value;
	}

	public void setError(String error) {
		this.error = error;
	}

	public void setError(String key, String value) {
		if (error.isEmpty())
			error = key + ":" + value;
		else
			error = error + Constants.LINE + value;
	}

	public void setExamples(String value) {
		if (examples.isEmpty())
			examples = value;
		else
			examples = examples + "|" + value;
	}

	public void setExplain(String value) {
		explain = value;
	}

	public void setFormat(String value) {
		format = value;
	}

	public void setGrammer(String value) {
		grammer = value;
	}

	public void setPhrases(String value) {
		if (phrases.isEmpty())
			phrases = value;
		else
			phrases = phrases + "|" + value;
	}

	public void setSound(String string) {
		sound = string;
	}

	public void setSyllabel(String string) {
		syllabel = string;
	}

	public void setURL(String url) {
		this.url = url;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("word: " + word).append(Constants.LINE);
		builder.append("syllabel: " + syllabel).append(Constants.LINE);
		builder.append("sound: " + sound).append(Constants.LINE);
		builder.append("clazz: " + clazz).append(Constants.LINE);
		if (!format.isEmpty())
			builder.append("format: " + format).append(Constants.LINE);
		if (!grammer.isEmpty())
			builder.append("grammer: " + grammer).append(Constants.LINE);
		if (!explain.isEmpty())
			builder.append("explain: " + explain).append(Constants.LINE);
		if (!examples.isEmpty())
			builder.append("examples: " + examples).append(Constants.LINE);
		if (!phrases.isEmpty())
			builder.append("phrases: " + phrases).append(Constants.LINE);
		if (!composite.isEmpty())
			builder.append("composite: " + composite).append(Constants.LINE);
		if (!error.isEmpty())
			builder.append("error: " + error).append(Constants.LINE);
		if (!alternative.isEmpty())
			builder.append("alternative: " + alternative).append(Constants.LINE);
		if (!comment.isEmpty())
			builder.append("comment: " + comment).append(Constants.LINE);
		if (!url.isEmpty())
			builder.append("url: " + url).append(Constants.LINE);
		return builder.toString();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
