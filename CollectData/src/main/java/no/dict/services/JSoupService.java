package no.dict.services;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import no.dict.data.DictItem;
import no.dict.data.DictItemFactory;
import no.dict.utils.Constants;

public class JSoupService {
	public static final String NEXT = "ul.searchPaging>li.next";
	public static final String ITEMS = "table.entriesTable";
	public static final String ITEM_START = "separatorTop";
	public static final String ITER = "tr";
	public static final String SEARCHED = "div.searchMatches > p";
	public static final Pattern SEARCHEDMATCHER = Pattern.compile(".* av ([0-9]*)");
	public static final Pattern LANGUAGE_START = Pattern.compile("(Bokmål|Engelsk|Dari) .*");
	public static final String DARI_START = "Dari ";

	public static final Predicate<String> FILTER = new Predicate<String>() {

		@Override
		public boolean test(String text) {
			return text.startsWith(DARI_START);
		}
	};

	/**
	 * Return whether a html file has next link
	 * 
	 * @param document
	 *            the html file
	 * @return
	 */
	public static boolean hasNext(Document document) {
		return !document.select(NEXT).isEmpty();
	}

	public static List<List<String>> getItems(Document document) {

		Elements tables = document.select(ITEMS);
		if (tables.size() == 0) {
			return new ArrayList<List<String>>();
		}
		Element table = tables.get(0);
		Elements iters = table.select(ITER);
		List<List<String>> items = new ArrayList<List<String>>();
		List<String> cur = null;
		for (Element iter : iters) {
			if (iter.classNames().isEmpty())
				continue;
			if (iter.hasClass(ITEM_START)) {
				if (cur != null) {
					cur.removeIf(FILTER);
					items.add(cur);
				}
				cur = new ArrayList<String>();
			}
			if (cur != null)
				complement(cur, iter);
		}
		if (cur != null) {
			cur.removeIf(FILTER);
			items.add(cur);
		}
		// for(List<String> item : items){
		// System.out.println(String.join(Constants.LINE, item));
		// System.out.println("-------------------------------------");
		// }
		return items;
	}

	private static void complement(List<String> cur, Element iter) {
		String text = iter.text();
		Matcher matcher = LANGUAGE_START.matcher(text);
		if (!matcher.matches() && cur.size() > 0) {
			text = cur.get(cur.size() - 1) + Constants.LINE + text;
			cur.set(cur.size() - 1, text);
		} else
			cur.add(text);
	}

	public static int getSearched(Document document) {
		Element searched = document.select(SEARCHED).get(0);
		String text = searched.text();
		Matcher match = SEARCHEDMATCHER.matcher(text);
		if (match.matches()) {
			return Integer.parseInt(match.group(1));
		}
		return 0;
	}

	public static List<DictItem> getDictItems(Document document) {
		List<List<String>> items = JSoupService.getItems(document);
		List<DictItem> results = new ArrayList<DictItem>();
		for (List<String> item : items) {
			DictItem cur = DictItemFactory.getDictItem(item);
			if (cur != null) {
				if (!cur.getError().isEmpty())
					cur.setURL(document.baseUri().toString());
				results.add(cur);
			}
		}
		return results;
	}

}
