package no.dict.threads;

import java.util.List;
import java.util.concurrent.BlockingQueue;

import org.jsoup.nodes.Document;

import no.dict.data.DictItem;
import no.dict.data.Dictionary;
import no.dict.data.VisitHash;
import no.dict.services.JSoupService;
import no.dict.utils.Constants;

public class Extractor extends ParentThread {
	private static class ExtractorThread extends AbstractThread {

		Dictionary dict;
		Document doc;
		VisitHash hash;
		List<Thread> unfinished;

		public ExtractorThread(Document doc, VisitHash hash, Dictionary dict, List<Thread> unfinished) {
			this.doc = doc;
			this.hash = hash;
			this.dict = dict;
			this.unfinished = unfinished;
			this.unfinished.add(ExtractorThread.this);
		}


		public void run() {
			try {
				List<DictItem> items = JSoupService.getDictItems(doc);
				for (DictItem item : items) {
					String word = item.getWord();
					if (word.isEmpty() || item.getExplain().isEmpty()) {
							dict.putIntoError(item);
					} else {
						int index = word.indexOf(Constants.LINE);
						if(index != -1)
							word = word.substring(0, index).trim();
						if (hash.addUnvisited(word))
							dict.putIntoWords(item);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				synchronized (unfinished) {
					unfinished.remove(ExtractorThread.this);
					unfinished.notifyAll();
				}
			}
		}

	}
	Dictionary dict;
	BlockingQueue<Document> forExtractor;

	VisitHash hash;

	public Extractor(VisitHash hash, Dictionary dict, BlockingQueue<Document> forExtractor) {
		this.setName(Extractor.class.getSimpleName());
		this.hash = hash;
		this.dict = dict;
		this.forExtractor = forExtractor;
	}

	public void run() {
		Document doc = null;
		while (true) {
			doc = takeFromQueueUntilSuccess(forExtractor);
			monitor();
			if (doc.baseUri().isEmpty()) {
				threadMessage("is going to stop");
				waitChildren();
				break;
			} else {
				waitForLessChildren();
				Thread child = new ExtractorThread(doc, hash, dict, running);
				child.start();
			}
		}
	}

}
