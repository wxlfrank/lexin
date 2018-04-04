package no.dict.threads;

import java.util.concurrent.BlockingQueue;

import org.jsoup.nodes.Document;

import no.dict.data.Dictionary;
import no.dict.data.VisitHash;

public class Extractor extends GroupThread {
	Dictionary dict;
	BlockingQueue<Document> downloader_extractor;

	VisitHash hash;

	public Extractor(VisitHash hash, Dictionary dict, BlockingQueue<Document> downloader_extractor) {
		this.setName(Extractor.class.getSimpleName());
		this.hash = hash;
		this.dict = dict;
		this.downloader_extractor = downloader_extractor;
	}

	public void run() {
		Document doc = null;
		while (true) {
			doc = takeFromQueueUntilSuccess(downloader_extractor);
			monitor();
			if (doc.baseUri().isEmpty()) {
				threadMessage("is going to stop");
				waitChildren();
				break;
			} else {
				waitForLessChildren();
				Thread child = new ExtractorThread(this, doc, hash, dict);
				child.start();
			}
		}
	}
}
