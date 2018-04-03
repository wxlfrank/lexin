package no.dict.threads;

import java.util.ArrayList;
import java.util.List;

public abstract class ParentThread extends AbstractThread {

	protected List<Thread> children = new ArrayList<Thread>();

	public List<Thread> getChildren() {
		return children;
	}

	public void waitChildren() {
		synchronized (children) {
			while (!children.isEmpty()) {
				try {
					threadMessage("has " + children.size() + " children threads running!");
					children.wait();
				} catch (InterruptedException e) {
				}
			}
		}
	}

	public void waitForLessChildren() {
		synchronized (children) {
			while (children.size() > QUEUE_SIZE) {
				try {
					children.wait();
				} catch (InterruptedException e) {
					interrupted = true;
				}
			}
		}
	}
}
