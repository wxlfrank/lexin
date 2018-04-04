package no.dict.threads;

import java.util.concurrent.BlockingQueue;

import org.jsoup.nodes.Document;

import no.dict.services.HttpService;
import no.dict.services.JSoupService;

class DownloadThread extends GroupItemThread {

	BlockingQueue<Document> forExtractor;
	String word;

	public DownloadThread(GroupThread parent, String word, BlockingQueue<Document> forExtractor) {
		super(parent);
		this.word = word;
		this.forExtractor = forExtractor;
	}

	private static final int count = 10;

	public void execute() {
		Document document = null;
		int from = 0;
		do {
			document = HttpService.getDocument(HttpService.getURL(word, from, count));
			putToQueueUntilSuccess(forExtractor, document);
			if (JSoupService.hasNext(document))
				from += count;
			else
				break;
		} while (true);

	}
}