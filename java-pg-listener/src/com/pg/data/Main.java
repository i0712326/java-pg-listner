package com.pg.data;

import java.util.concurrent.BlockingQueue;

public class Main {

	public static void main(String[] args) {
		ListenNotify ln = new ListenNotify();

		// Get the shared queue
		BlockingQueue<String> queue = ln.getQueue();

		// Loop forever pulling messages off the queue
		while (true) {
			try {
				// queue blocks until something is placed on it
				String msg = queue.take();

				// Do something with the event
				System.out.println(msg);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}

}
