package no.dict.threads;

import java.util.concurrent.BlockingQueue;

public abstract class AbstractThread extends Thread {

	public static int QUEUE_SIZE = 32;
	public static long WAIT_TIME = 2000;

	public static void threadMessage(String message) {
		String threadName = Thread.currentThread().getName();
		System.out.format("%s %s%n", threadName, message);
	}

	protected boolean interrupted = false;

	long time1 = System.currentTimeMillis();

	long time2 = System.currentTimeMillis();
	/**
	 * monitor which thread is running every 5 seconds
	 */
	public void monitor() {
		time2 = System.currentTimeMillis();
		if (time2 - time1 > 5000) {
			threadMessage("is running");
			time1 = time2;
		}
	}

	public boolean waitThreadFinish(ParentThread thread) {
		while (thread.isAlive()) {
			try {
				threadMessage("waiting " + thread.getName() + " to finish");
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
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
				interrupted = true;
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
				interrupted = false;
			}
		} while (!success);
		return result;
	}

}
