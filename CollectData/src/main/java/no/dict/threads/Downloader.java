package no.dict.threads;

import java.util.concurrent.BlockingQueue;

import org.jsoup.nodes.Document;

import no.dict.data.VisitHash;

/**
 * The thread to download html files from internet
 * @author wxlfr_000
 *
 */
public class Downloader extends GroupThread {
	/**
	 * the queue of downloaded html files
	 */
	BlockingQueue<Document> downloaded;
	Thread extractor;

	VisitHash hash;

	public Downloader(VisitHash hash, BlockingQueue<Document> downloadBuffer, Thread extractor) {
		this.setName(Downloader.class.getSimpleName());
		this.hash = hash;
		this.downloaded = downloadBuffer;
		this.extractor = extractor;
	}

	public void run() {
		String word = "";
		while (true) {
			// do{
			word = hash.nextUnvisited();
			if (!word.isEmpty()) {
				hash.setVisited(word);
			}

			monitor();
			if (!word.isEmpty()) {
				waitForLessChildren();
				Thread child = new DownloadThread(this, word, downloaded);
				child.start();
			}
			/**
			 * When the thread is interrupted, put a empty document into the queue
			 */
			if (isFinished()) {
				threadMessage("is interrupted!");
				putToQueueUntilSuccess(downloaded, new Document(""));
				break;
			}
		}
	}

	

}
