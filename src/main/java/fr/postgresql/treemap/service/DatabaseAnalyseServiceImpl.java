package fr.postgresql.treemap.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.postgresql.treemap.api.DatabaseAnalyseService;

public class DatabaseAnalyseServiceImpl implements DatabaseAnalyseService {

	private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseAnalyseServiceImpl.class);

	private final static String DB_URL = "jdbc:postgresql://127.0.0.1:5432/sample";
	private final static String DB_USER = "postgres";
	private final static String DB_PASS = "postgres";
	
	private String query;

	public DatabaseAnalyseServiceImpl() {
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		URL url = cl.getResource("query.sql");
		try {
			this.query = this.readFile(url);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object> analyse() throws Exception {
		Map<String, Object> map = new HashMap<>();
		List<Object> result = new LinkedList<>();

		try {
			Connection connection = this.getConnection();
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(this.query);
			while (rs.next()) {
				if (!rs.getBigDecimal(4).equals(new BigDecimal(0))) {
					String tablename = rs.getString(1);
					Map<String, Object> currentTable = (Map<String, Object>) map.get(tablename);
					List<Object> currentIndex = null;

					if (currentTable == null) {
						Map<String, Object> leafMap = new HashMap<String, Object>();
						leafMap.put("name", tablename);
						leafMap.put("children", new LinkedList<Object>());
						map.put(tablename, leafMap);
						result.add(leafMap);
						currentTable = (Map<String, Object>) map.get(tablename);
					}

					if (((List<Object>) currentTable.get("children")).size() == 0) {
						Map<String, Object> leafMap = new HashMap<String, Object>();
						leafMap.put("size", rs.getBigDecimal(4));
						leafMap.put("name", tablename);
						((List<Object>) currentTable.get("children")).add(leafMap);
					}

					if (((List<Object>) currentTable.get("children")).size() < 2) {
						Map<String, Object> leafMap = new HashMap<String, Object>();
						leafMap.put("name", tablename);
						leafMap.put("children", new LinkedList<Object>());
						((List<Object>) currentTable.get("children")).add(leafMap);
					}

					currentIndex = (List<Object>) ((Map<String, Object>) ((List<Object>) currentTable.get("children")).get(1)).get("children");
					Map<String, Object> leafMap = new HashMap<String, Object>();
					leafMap.put("size", rs.getBigDecimal(5));
					leafMap.put("name", "ix (" + rs.getString(2) + ")");
					currentIndex.add(leafMap);
				}
			}
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(), e);
			throw new Exception("Impossible d'obtenir l'analyse des tables et index", e.getCause());
		}
		return result;
	}

	private Connection getConnection() throws ClassNotFoundException, SQLException {
		Class.forName("org.postgresql.Driver");
		Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
		return conn;
	}

	private String readFile(URL url) throws IOException {
		BufferedReader bf = new BufferedReader(new InputStreamReader(url.openStream()));
		StringBuffer sb = new StringBuffer();
		String inputLine;
		while ((inputLine = bf.readLine()) != null)
			sb.append(inputLine);
		bf.close();
		return sb.toString();
	}

}