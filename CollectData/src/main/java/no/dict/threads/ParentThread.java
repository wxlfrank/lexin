package no.dict.threads;

import java.util.ArrayList;
import java.util.List;

public abstract class ParentThread extends AbstractThread {

	protected List<Thread> running = new ArrayList<Thread>();

	public List<Thread> getChildren() {
		return running;
	}

	public void waitChildren() {
		synchronized (running) {
			while (!running.isEmpty()) {
				try {
					threadMessage("has " + running.size() + " children threads running!");
					running.wait();
				} catch (InterruptedException e) {
				}
			}
		}
	}

	public void waitForLessChildren() {
		synchronized (running) {
			while (running.size() > QUEUE_SIZE) {
				try {
					running.wait();
				} catch (InterruptedException e) {
					interrupted = true;
				}
			}
		}
	}
}
