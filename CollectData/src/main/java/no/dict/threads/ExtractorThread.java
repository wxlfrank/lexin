package no.dict.threads;

import java.util.List;

import org.jsoup.nodes.Document;

import no.dict.data.DictItem;
import no.dict.data.Dictionary;
import no.dict.data.VisitHash;
import no.dict.services.JSoupService;
import no.dict.utils.Constants;

class ExtractorThread extends GroupItemThread {

	Dictionary dict;
	Document doc;
	VisitHash hash;

	public ExtractorThread(GroupThread parent, Document doc, VisitHash hash, Dictionary dict) {
		super(parent);
		this.doc = doc;
		this.hash = hash;
		this.dict = dict;
	}

	public void execute() {
		List<DictItem> items = JSoupService.getDictItems(doc);
		for (DictItem item : items) {
			String word = item.getWord();
			if (word.isEmpty() && item.getExplain().isEmpty()) {
				dict.putIntoError(item);
			} else {
				int index = word.indexOf(Constants.LINE);
				if (index != -1)
					word = word.substring(0, index).trim();
				if (hash.addUnvisited(word))
					dict.putIntoWords(item);
			}
		}
	}

}