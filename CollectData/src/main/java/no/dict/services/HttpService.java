/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.dict.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import no.dict.data.DictItem;

/**
 *
 * @author wxlfr_000
 */
public class HttpService {
	public static final String NL = String.format("%n");
	static Object lock = new ReentrantLock();

	/**
	 * Return the dictionary items from a html file
	 * 
	 * @param doc
	 *            a html file
	 * @return
	 */
	public static List<DictItem> getDictItems(Document doc) {
		List<Map<String, List<String>>> raw = getRawData(doc);
		List<DictItem> results = new ArrayList<DictItem>();
		for (Map<String, List<String>> iter : raw) {
			DictItem cur = getDictItem(iter);
			if (cur != null) {
				if (!cur.getError().isEmpty())
					cur.setURL(doc.baseUri().toString());
				results.add(cur);
			}
		}
		return results;
	}

	/**
	 * Return a parsed document from a downloaded html file for a url
	 * 
	 * @param url
	 * @return
	 */
	public static Document getDocument(String url) {
		Document obj = null;
		String html = HttpService.getDocumentFromWeb(url);
		if (html != null)
			obj = Jsoup.parse(html);
		if (obj != null)
			obj.setBaseUri(url);
		return obj;
	}

	/**
	 * Return a downloaded html file for a url
	 * 
	 * @param url
	 * @return
	 */
	private static String getDocumentFromWeb(String url) {
		try {
			URLConnection con = new URL(url).openConnection();
			con.addRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36 Edge/12.0");
			StringBuilder stringBuilder = new StringBuilder();
			BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				stringBuilder.append(line);
				stringBuilder.append(NL);
			}
			reader.close();
			if (stringBuilder.length() == 0)
				return null;
			return stringBuilder.toString();
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * Return the main div which contains the words a html file doc
	 * 
	 * @param doc
	 *            the html file
	 * @return
	 */
	public static Elements getExplainDivs(Document doc) {
		return doc.select("#tblHitsTable tr");
	}

	public static String getURL(String word, int from) {
		return "http://lexin.udir.no/?search=" + word + "&dict=nbo-prs-maxi&ui-lang=NBO&startingfrom=" + from
				+ "&count=10&search-type=search-both&checked-languages=NBO&checked-languages=PRS&checked-languages=B";
	}

	/**
	 * Return whether a html file has next link
	 * 
	 * @param document
	 *            the html file
	 * @return
	 */
	public static boolean hasNext(Document document) {
		return !document.select("ul.searchPaging>li.next").isEmpty();
	}

	/**
	 * append a new value
	 * 
	 * @param pre
	 * @param add
	 * @return
	 */
	private static String append(String pre, String add) {
		if (pre.isEmpty()) {
			return add;
		} else {
			return pre + "|" + add;
		}
	}

	/**
	 * Return a dictionary from a raw data
	 * 
	 * @param item
	 * @return
	 */
	private static DictItem getDictItem(Map<String, List<String>> item) {
		DictItem result = new DictItem();
		result.setError(toString(item.get("error")));
		String word = toString(item.get("oppslagsord"));
		if (word.isEmpty()) {
			result.setError(append(result.getError(), "oppslagsord->"));
		}
		String[] headers = word.split("\\|");
		result.setSyllabel(headers[0].replaceAll("&#124;", "|"));
		result.setWord(headers[0].replaceAll("&#124;", ""));
		for (int i = 1; i < headers.length; i++) {
			if (headers[i].indexOf("/") != -1)
				result.setSound(headers[i].replaceFirst(" /", "/"));
			else
				result.setClazz(headers[i]);
		}

		String explain = toString(item.get("forklaring"));
		if (explain.isEmpty()) {
			result.setError(append(result.getError(), "forklaring->"));
		}
		result.setExplain(explain);
		String value = toString(item.get("tilleggsforklaring"));
		if (!value.isEmpty()) {
			if (result.getExplain().isEmpty()) {
				result.setError(append(result.getError(), "tilleggsforklaring->" + value));
			} else
				result.setExplain(append(result.getExplain(), value));
		}
		for (Entry<String, List<String>> iter : item.entrySet()) {
			String label = iter.getKey();
			value = toString(iter.getValue());
			if (label.equals("oppslagsord")) {
			} else if (label.equals("grammatikk")) {
				result.setGrammer(value.replaceAll("\\^", ""));
			} else if (label.equals("forklaring")) {
			} else if (label.equals("tilleggsforklaring")) {
			} else if (label.endsWith("yning")) {
				result.setFormat(value);
			} else if (label.equals("kommentar")) {
				result.setComment(value);
			} else if (label.equals("sammensetning")) {
				result.setComposite(value);
			} else if (label.equals("uttrykk")) {
				result.setPhrases(value);
			} else if (label.equals("alternativ")) {
				result.setAlternative(value);
			} else if (label.equals("eksempel")) {
				result.setExamples(value);
			} else if (label.equals("error")) {
				result.setError(value);
			} else {
				System.out.println(("new label found: \"" + label + "\" for " + result.getWord()));
			}
		}
		if (result.getWord().isEmpty() || result.getExplain().isEmpty()) {
			if (result.getSound().isEmpty() && result.getClazz().startsWith("se")) {
				result.setExplain(result.getClazz());
				result.setClazz("");
			}
		}
		return result;
	}

	private static String getLabel(Element div) {
		return getString(div.children().get(0), ">span");
	}

	/**
	 * Return the raw data: string -> list of string from a html file
	 * 
	 * @param doc
	 *            the html file
	 * @return
	 */
	private static List<Map<String, List<String>>> getRawData(Document doc) {
		List<Map<String, List<String>>> results = new ArrayList<Map<String, List<String>>>();
		Elements divs = getExplainDivs(doc);
		if (divs.isEmpty())
			return results;

		Map<String, List<String>> cur = null;
		Iterator<Element> iter = divs.iterator();
		Element element = null;
		String key = "", value = "";
		List<String> preValues = null, values = null;
		;
		while (iter.hasNext()) {
			element = iter.next();
			if (element.children().size() < 2)
				continue;
			if (element.classNames().contains("separatorTop")) {
				cur = new HashMap<String, List<String>>();
				results.add(cur);
			}
			key = getLabel(element);
			value = getValue(element);
			if (key != null) {
				if (cur == null) {
					cur = new HashMap<String, List<String>>();
					results.add(cur);
					value = key + "->" + value;
					key = "error";
				} else {
					values = cur.get(key);
				}
				if (values == null) {
					values = new ArrayList<String>();
				} else {
					value = key + "->" + value;
					key = "error";
				}
				cur.put(key, values);
				preValues = values;
			}
			preValues.add(value);
		}
		return results;
	}

	private static String getString(Element div, String selector) {
		Element first = div;
		if (!selector.isEmpty()) {
			Elements elements = div.select(selector);
			if (elements.isEmpty())
				return null;
			first = elements.first();
		}
		String str = first.text().trim();
		return str.replaceAll(" *([,;\\]])", "$1");
	}

	private static String getValue(Element div) {
		return getString(div.children().get(1), "");
	}

	/**
	 * Reform a list of string
	 * 
	 * @param list
	 * @return
	 */
	private static String toString(List<String> list) {
		if (list == null)
			return "";
		for (String iter : list) {
			iter = iter.replaceAll("\\|", "&#124;");
		}
		return list.stream().collect(Collectors.joining("|"));
	}

	public static String getURL(String word, int from, int count) {
		try {
			word = URLEncoder.encode(word, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			System.out.println(word);
		}
		return "http://lexin.udir.no/?search=" + word + "&dict=nbo-prs-maxi" + "&ui-lang=NBO" + "&startingfrom=" + from
				+ "&count=" + count + "&search-type=search-both" + "&checked-languages=NBO" + "&checked-languages=PRS"
				+ "&checked-languages=B";
	}
}
