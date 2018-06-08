package pl.tscript3r.notify2.server.connectivity;

import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import pl.tscript3r.notify2.server.dataflow.TaskContainer;
import pl.tscript3r.notify2.server.domain.Recipient;
import pl.tscript3r.notify2.server.domain.TaskPackage;
import pl.tscript3r.notify2.server.factory.TaskFactory;

public class DBCommunicator {
	private static Logger log = Logger.getLogger(DBCommunicator.class.getName());
	private static final String SQL_GET_ALL_LINKS = "SELECT * FROM tasks_list";
	private static final String SQL_GET_USER_DATA = "SELECT email, name FROM users WHERE user_id=%d";
	private static final String SQL_REMOVE_TASK = "DELETE FROM tasks_list WHERE task_id=%d";
	private Connection connection;
	private String username;
	private String password;
	private String hostname;
	private String dbName;
	private Integer port;

	private static DBCommunicator dbConnection = new DBCommunicator();

	private DBCommunicator() {
	}

	public synchronized static DBCommunicator getInstance() {
		return dbConnection;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String conUrl) {
		this.hostname = conUrl;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String connect() throws ClassNotFoundException, SQLException {
		String url = String.format("jdbc:mysql://%s/%s", hostname + ":" + port.toString(), dbName);
		Class.forName("com.mysql.jdbc.Driver");
		connection = DriverManager.getConnection(url, username, password);
		String result = connection.getCatalog();
		return result;
	}

	private ResultSet sqlQuery(String query) throws SQLException {
		Statement statement = connection.createStatement();
		ResultSet result = statement.executeQuery(query);
		return result;
	}

	private void sqlUpdate(String query) throws SQLException {
		Statement statement = connection.createStatement();
		statement.executeUpdate(query);
	}

	private Recipient getRecipient(int userId) throws SQLException {
		ResultSet resultsSet = sqlQuery(String.format(SQL_GET_USER_DATA, userId));
		if (!resultsSet.next())
			throw new SQLException(String.format("User with the id: %d was not found in the DB", userId));
		Recipient result = new Recipient();
		result.setEmail(resultsSet.getString("email"));
		result.setName(resultsSet.getString("name"));
		result.setUserId(userId);
		return result;
	}

	private void removeTask(int taskId) throws SQLException {
		sqlUpdate(String.format(SQL_REMOVE_TASK, taskId));
		TaskContainer.getInstance().deleteTask(taskId);
		log.info(String.format("Removed task. ID: %d", taskId));
	}

	public void updateTasks() throws SQLException, URISyntaxException {
		ResultSet resultsSet = sqlQuery(SQL_GET_ALL_LINKS);
		TaskContainer container = TaskContainer.getInstance();
		while (resultsSet.next()) {
			Boolean active = resultsSet.getBoolean("active");
			if (!active)
				removeTask(resultsSet.getInt("task_id"));
			else {
				TaskPackage task = TaskFactory.getTaskPackageInstance(resultsSet.getInt("task_id"),
						resultsSet.getString("link_url"), getRecipient(resultsSet.getInt("user_id")));
				if (!container.isTask(task.getId())) {
					container.addTask(task);
					log.info(String.format("Added new task. ID: %d", task.getId()));
				}
			}
		}
	}

	public void close() throws SQLException {
		connection.close();
	}

}
