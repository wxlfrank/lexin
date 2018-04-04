package no.dict.threads;

import java.util.concurrent.BlockingQueue;

public abstract class AbstractThread extends Thread {

	public static int QUEUE_SIZE = 32;
	public static long WAIT_TIME = 2000;

	public void threadMessage(String message) {
		System.out.format("%s: %s%n", getName(), message);
	}


	long time1 = System.currentTimeMillis();

	long time2 = System.currentTimeMillis();
	/**
	 * monitor which thread is running every 5 seconds
	 */
	public synchronized void  monitor() {
		time2 = System.currentTimeMillis();
		if (time2 - time1 > 5000) {
			threadMessage("is running");
			time1 = time2;
		}
	}

	public boolean waitThreadFinish(GroupThread thread) {
		while (thread.isAlive()) {
			try {
				threadMessage("waiting " + thread.getName() + " to finish");
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		threadMessage(thread.getName() + " has finished");
		return true;
	}

	/**
	 * Put an item s into the queue q with success guaranteed
	 */
	protected <T> void putToQueueUntilSuccess(BlockingQueue<T> q, T s) {
		boolean success = false;
		do {
			try {
				q.put(s);
				success = true;
			} catch (InterruptedException e) {
			}
		} while (!success);
	}

	/**
	 * Take an item from the queue q with success guaranteed
	 * @param q
	 * @return
	 */
	protected <T> T takeFromQueueUntilSuccess(BlockingQueue<T> q) {
		T result = null;
		boolean success = false;
		do {
			try {
				result = q.take();
				success = true;
			} catch (InterruptedException e) {
			}
		} while (!success);
		return result;
	}

}
