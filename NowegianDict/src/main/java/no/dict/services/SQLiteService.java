/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.dict.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import no.dict.data.DictItem;
import no.dict.data.VisitHash;

/**
 * The sqlite service to load and save data into database
 * @author wxlfr_000
 */
public class SQLiteService {
	/**
	 * Interface of updating a data in database
	 * @author wxlfr_000
	 *
	 * @param <T>
	 */
	private static abstract class UpdateDatabase<T>{
		T data;
		public UpdateDatabase(T data){
			this.data = data;
		}
		public abstract List<String> getSQLSs(Statement stat) throws SQLException;

		public void update(){
			Connection connection = null;
			try {
				connection = getConnection();
				Statement stat = connection.createStatement();
				List<String> sqls = getSQLSs(stat);
				stat.close();
				connection.close();
				connection = SQLiteService.getConnection();
				connection.setAutoCommit(false);
				stat = connection.createStatement();
				for (String iter : sqls) {
					stat.addBatch(iter);
				}
				stat.executeBatch();
				connection.commit();
				connection.close();
			} catch (ClassNotFoundException e) {
			} catch (SQLException e) {
			} finally {
				if (connection != null) {
					try {
						connection.close();
					} catch (SQLException e) {
					}
				}
			}
		}
	}
	
	/**
	 * Interface of obtaining an object from a query record
	 * @author wxlfr_000
	 *
	 */
	static abstract class SQLResultHandler{
		public abstract <T> T handle(ResultSet result) throws SQLException;
	}

	private static final String DATABASE_URL = "jdbc:sqlite:dict.db";
	
	/**
	 * Handler of obtaining a dictionary item from a query record
	 */
	private static SQLResultHandler handler1 = new SQLResultHandler() {
		@SuppressWarnings("unchecked")
		@Override
		public DictItem handle(ResultSet result) throws SQLException {
			return getDictItem(result);
		}
	};
	
	private static final String JDBC_DRIVER = "org.sqlite.JDBC";

	private static boolean registered = false;
	
	/**
	 * Load the visit hash from database
	 * @param hash the visit hash
	 */
	public static void loadVisitHash(VisitHash hash){
		Connection connection = null;
		try {
			connection = getConnection();
			Statement stat = connection.createStatement();
			String sql = null;
			for (int index = 0; index < 2; ++index) {
				sql = "create table if not exists hash" + index + " (word text primary key not null);";
				stat.executeUpdate(sql);
			}
			for (int index = 0; index < 2; ++index) {
				// Query the old hosts and compare them with the current hosts
				sql = "select word from hash" + index;
				List<String> old_words = getList(stat, sql, "word");
				hash.addAll(index, old_words);
			}
		}catch(SQLException e){
		}catch(ClassNotFoundException e) { }
	}

	/**
	 * Load dictionary items from database
	 * @param words
	 */
	public static void loadWords(Map<String, DictItem> words){
		Connection connection = null;
		try {
			connection = getConnection();
			Statement stat = connection.createStatement();
			String sql = "select name from sqlite_master where type = 'table';";
			List<String> tables = getList(stat, sql, "name");
			for(String table : tables){
				if(table.length() != 1) continue;
				sql =  "select * from '" + table + "';";
				List<DictItem> list = getList(stat, sql, handler1);
				for(DictItem iter : list){
					words.put(iter.getWord() + "|" + iter.getExplain(), iter);
				}
			}
		}catch(SQLException e){
		}catch (ClassNotFoundException e) { }
	}

	/**
	 * Save dictionary items to database
	 * @param words
	 */
	public static void saveDictItem(Map<String, DictItem> words){
		UpdateDatabase<Map<String, DictItem>> update = new UpdateDatabase<Map<String, DictItem>>(words){
			public List<String> getSQLSs(Statement stat) throws SQLException{
				List<String> sqls = new ArrayList<String>();
				String sql = "";
				Map<String, DictItem> new_words = new HashMap<String, DictItem>();
				new_words.putAll(data);

				sql = "select name from sqlite_master where type = 'table';";
				List<String> tables = getList(stat, sql, "name");
				for(String table : tables){
					if(table.length() != 1) continue;
					sql =  "select * from '" + table + "';";
					List<DictItem> list = getList(stat, sql, handler1);
					for(DictItem iter : list){
						new_words.remove(iter.getWord() + "|" + iter.getExplain());
					}
				}

				for(DictItem iter : new_words.values()){
					String table = iter.getWord().substring(0, 1);
					if(!tables.contains(table)){
						sql = createTable(table);
						sqls.add(sql);
						tables.add(table);
					}
					sql = insert(iter);
					sqls.add(sql);
				}
				return sqls;
			}
		};
		update.update();
	}

	/**
	 * Save visit hash into database
	 * @param hash the visit hash
	 */
	public static void saveVisitHash(VisitHash hash){
		UpdateDatabase<VisitHash> update = new UpdateDatabase<VisitHash>(hash){
			public List<String> getSQLSs(Statement stat) throws SQLException{
				List<String> sqls = new ArrayList<String>();
				String sql = "";

				/**
				 * Create tables for visited words and unvisited words respectively.
				 * new_words used to store the current version of visited words and unvisited words.
				 */
				@SuppressWarnings("unchecked")
				Set<String>[] new_words = new HashSet[2];
				for (int index = 0; index < 2; ++index) {
					sql = "create table if not exists hash" + index + " (word text primary key not null);";
					new_words[index] = new HashSet<String>();
					new_words[index].addAll(data.get(index));
					sqls.add(sql);
				}

				/**
				 * Get the old version of visited words and unvisited words respectively.
				 * Update them according to comparison with the current version of visited words and unvisited words
				 * 1. Delete a word if a word is missed in the current version
				 * 2. Add a word if a word is new in the current version.
				 */
				for (int index = 0; index < 2; ++index) {
					// Query the old hosts and compare them with the current hosts
					sql = "select word from hash" + index;
					List<String> old_words = getList(stat, sql, "word");

					for (String word : old_words) {
						if (!new_words[index].contains(word)) {
							// If a host is kept, update its urls in database
							sqls.add("delete from hash" + index + " where word='" + toSQLTextValue(word) + "';");
						} 
						new_words[index].remove(word);
					}
				}
				for (int index = 0; index < 2; ++index) {
					for (String word : new_words[index]) {
						sql = "insert into hash" + index + " values ('" + toSQLTextValue(word) + "');";
						sqls.add(sql);
					}
				}
				return sqls;
			}
		};
		update.update();
	}
	/**
	 * Return the sql expression for creating a table to record words
	 * @param table
	 * @return the sql expression
	 */
	private static String createTable(String table){
		return "CREATE TABLE if not exists '" + table  
				+ "' (word TEXT NOT NULL,"
				+ " explain TEXT NOT NULL,"
				+ " sound TEXT,"
				+ " syllabel TEXT,"
				+ " clazz TEXT,"
				+ " format TEXT,"
				+ " composite TEXT,"
				+ " alternatives TEXT,"
				+ " comment TEXT,"
				+ " examples TEXT,"
				+ " phrases TEXT,"
				+ " error TEXT,"
				+ " audio BLOB,"
				+ " grammer TEXT, PRIMARY KEY(word, explain))";
	}
	private static Connection getConnection() throws ClassNotFoundException, SQLException {
		if (!registered) {
			Class.forName(JDBC_DRIVER);
			registered = true;
		}
		return DriverManager.getConnection(DATABASE_URL);
	}

	/**
	 * return a dictionary item from a record
	 * @param rs
	 * @return
	 */
	private static DictItem getDictItem(ResultSet rs) {
		DictItem result = new DictItem();
		try {
			result.setWord(rs.getString("word"));
			result.setExplain(rs.getString("explain"));
			result.setSound(rs.getString("sound"));
			result.setSyllabel(rs.getString("syllabel"));
			result.setClazz(rs.getString("clazz"));
			result.setFormat(rs.getString("format"));
			result.setComposite(rs.getString("composite"));
			result.setAlternative(rs.getString("alternatives"));
			result.setComment(rs.getString("comment"));
			result.setExamples(rs.getString("examples"));
			result.setPhrases(rs.getString("phrases"));
			result.setError(rs.getString("error"));
			result.setGrammer(rs.getString("grammer"));
		} catch (SQLException e) {
			return null;
		} 
		return result;
	}

	/**
	 * Return a list of element from query results 
	 * @param stat statement
	 * @param sql the sql expression to perform a query
	 * @param handler the handler to obtain an object from a query record
	 * @return a list of element
	 * @throws SQLException
	 */
	private static <T> List<T> getList(Statement stat, String sql, SQLResultHandler handler) throws SQLException{
		ResultSet result = null;
		List<T> list = new ArrayList<T>();
		try {
			result = stat.executeQuery(sql);
		} catch (SQLException e) {
			return list;
		}
		if (result != null)
			while (result.next()) {
				T item = handler.handle(result);
				if(item != null)
					list.add(item);
			}
		return list;
	}


//	public static ResultSet query(Statement stmt, String query) {
//		ResultSet rs = null;
//		try {
//			rs = stmt.executeQuery(query);
//		} catch (SQLException ex) {
//			Logger.getLogger(SQLiteService.class.getName()).log(Level.SEVERE, null, ex);
//			return null;
//		}
//		return rs;
//	}
	
	/**
	 * Get values of column from query result of sql expression
	 * @param stat
	 * @param sql
	 * @param column
	 * @return a list of strings for a certain column
	 * @throws SQLException
	 */
	private static List<String> getList(Statement stat, String sql, final String column) throws SQLException {
		SQLResultHandler handler = new SQLResultHandler() {

			@SuppressWarnings("unchecked")
			@Override
			public String handle(ResultSet result) throws SQLException {
				return result.getString(column);
			}
		};
		return getList(stat, sql, handler);
	}

	/**
	 * Return the sql expression to insert a record into a word table
	 * @param item
	 * @return the sql expression
	 */
	private static String insert(DictItem item){
		return "insert into '" + item.getWord().substring(0, 1) + "' values ('" 
				+ item.getWord().replaceAll("'", "''") + "','"
				+ item.getExplain().replaceAll("'", "''") + "','"
				+ item.getSound().replaceAll("'", "''") + "','"
				+ item.getSyllabel().replaceAll("'", "''") + "','"
				+ item.getClazz().replaceAll("'", "''") + "','"
				+ item.getFormat().replaceAll("'", "''") + "','"
				+ item.getComposite().replaceAll("'", "''") + "','"
				+ item.getAlternative().replaceAll("'", "''") + "','"
				+ item.getComment().replaceAll("'", "''") + "','"
				+ item.getExamples().toString().replaceAll("'", "''") + "','"
				+ item.getPhrases().toString().replaceAll("'", "''") + "','"
				+ item.getError().toString().replaceAll("'", "''") + "',"
				+ null + ",'"
				+ item.getGrammer().replaceAll("'", "''") + "')";
	}

	/**
	 * Change str to text value in sql expression
	 * @param str
	 * @return
	 */
	private static String toSQLTextValue(String str) {
		return str.replaceAll("'", "''");
	}
}
