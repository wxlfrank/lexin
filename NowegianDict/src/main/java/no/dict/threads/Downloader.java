package no.dict.threads;

import java.util.List;
import java.util.concurrent.BlockingQueue;

import org.jsoup.nodes.Document;

import no.dict.data.VisitHash;
import no.dict.services.HttpService;

/**
 * The thread to download html files from internet
 * @author wxlfr_000
 *
 */
public class Downloader extends ParentThread {
	private static class DownloadThread extends AbstractThread {

		BlockingQueue<Document> forExtractor;
		List<Thread> unfinished;
		String word;

		public DownloadThread(String word, BlockingQueue<Document> forExtractor, List<Thread> unfinished) {
			this.word = word;
			this.forExtractor = forExtractor;
			this.unfinished = unfinished;
			this.unfinished.add(DownloadThread.this);
		}

		public void run() {
			Document obj = null;
			try {
				int from = 0;
				do {
					obj = HttpService.getDocument(HttpService.getURL(word, from));
					putToQueueUntilSuccess(forExtractor, obj);
					if(HttpService.hasNext(obj))
						from += 5;
					else
						break;
				}while (true);

			} catch (Exception e) {
			} finally {
				synchronized (unfinished) {
					unfinished.remove(DownloadThread.this);
					unfinished.notifyAll();
				}
			}
		}

	}
	/**
	 * the queue of downloaded html files
	 */
	BlockingQueue<Document> downloaded;
	ParentThread extractor;

	VisitHash hash;

	public Downloader(VisitHash hash, BlockingQueue<Document> forExtractor, ParentThread extractor) {
		this.setName("Downloader");
		this.hash = hash;
		this.downloaded = forExtractor;
		this.extractor = extractor;
	}

	public void run() {
		String word = "";
		while (true) {
			do{
				word = hash.nextUnvisited();
				if(!word.isEmpty()) {
					hash.setVisited(word);
					break;
				}
				synchronized (hash) {
					try {
						if (extractor.getState() == Thread.State.WAITING || extractor.getChildren().size() != 0)
							wait(WAIT_TIME);
						else
							break;
					}catch (InterruptedException e){
						interrupted = true;
						break;
					}
				}
			}while(true);

			monitor();
			if(!word.isEmpty()){
				waitForLessChildren();
				Thread child = new DownloadThread(word, downloaded, unfinished);
				child.start();
			}
			/**
			 * When the thread is interrupted, put a empty document into the queue
			 */
			if(interrupted || Thread.interrupted() || word.isEmpty()) {
				threadMessage("is interrupted!");
				putToQueueUntilSuccess(downloaded, new Document(""));
				break;
			}
		}
	}

}
