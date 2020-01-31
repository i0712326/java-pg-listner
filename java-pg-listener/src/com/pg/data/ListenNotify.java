package com.pg.data;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.impossibl.postgres.api.jdbc.PGConnection;
import com.impossibl.postgres.api.jdbc.PGNotificationListener;
import com.impossibl.postgres.jdbc.PGDataSource;

public class ListenNotify {
	// Create the queue that will be shared by the producer and consumer
	private BlockingQueue<String> queue = new ArrayBlockingQueue<String>(10);

	// Database connection
	PGConnection connection;

	public ListenNotify() {
		// Get database info from environment variables
		String DBHost = "pgdbsvr.itstikk.pro";
		String DBName = "usr01";
		String DBUserName = "usr01";
		String DBPassword = "qf48d8uv";

		// Create the listener callback
		PGNotificationListener listener = new PGNotificationListener() {
			@Override
			public void notification(int processId, String channelName, String payload) {
				// Add event and payload to the queue
				queue.add("/channels/" + channelName + " " + payload);
			}
		};

		try {
			// Create a data source for logging into the db
			PGDataSource dataSource = new PGDataSource();
			dataSource.setHost(DBHost);
			dataSource.setPort(5432);
			dataSource.setDatabase(DBName);
			dataSource.setUser(DBUserName);
			dataSource.setPassword(DBPassword);

			// Log into the db
			connection = (PGConnection) dataSource.getConnection();

			// add the callback listener created earlier to the connection
			connection.addNotificationListener(listener);

			// Tell Postgres to send NOTIFY q_event to our connection and listener
			Statement statement = connection.createStatement();
			statement.execute("LISTEN q_event");
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return shared queue
	 */
	public BlockingQueue<String> getQueue() {
		return queue;
	}

}
