package no.dict.threads;

import java.util.ArrayList;
import java.util.List;

public abstract class ParentThread extends AbstractThread {

	List<Thread> unfinished = new ArrayList<Thread>();

	public List<Thread> getChildren() {
		return unfinished;
	}

	public void waitChildrenFinish() {
		synchronized (unfinished) {
			while (!unfinished.isEmpty()) {
				try {
					threadMessage("has " + unfinished.size() + " children threads to finish!");
					unfinished.wait();
				} catch (InterruptedException e) {
				}
			}
		}
	}

	public void waitForLessChildren() {
		synchronized (unfinished) {
			while (unfinished.size() > QUEUE_SIZE) {
				try {
					unfinished.wait();
				} catch (InterruptedException e) {
					interrupted = true;
				}
			}
		}
	}
}
