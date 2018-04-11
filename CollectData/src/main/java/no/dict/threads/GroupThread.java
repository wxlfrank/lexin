package no.dict.threads;

import java.util.ArrayList;
import java.util.List;

public abstract class GroupThread extends AbstractThread {

	protected List<Thread> children = new ArrayList<Thread>();
	private boolean finish = false;
	public synchronized void setFinished() {
		finish = true;
	}
	public synchronized boolean isFinished() {
		return finish;
	}

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

	public void addThread(Thread thread) {
		synchronized (children) {
			children.add(thread);
			children.notifyAll();
		}
	}

	public void removeThread(Thread thread) {
		synchronized (children) {
			children.remove(thread);
			children.notifyAll();
		}
	}

	public void waitForLessChildren() {
		synchronized (children) {
			while (children.size() > QUEUE_SIZE) {
				try {
					children.wait();
				} catch (InterruptedException e) {
				}
			}
		}
	}
}
