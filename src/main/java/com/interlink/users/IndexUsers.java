package com.interlink.users;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.interlink.index.IndexConnection;
import com.interlink.map.Users;

public class IndexUsers {

	String[] userPages = {
	   "Dashboard", 
	   "Price List", 
	   "Inventory List",
	   "Customer List", 
	   "Supplier List", 
	   "Purchase Order",
	   "Pending Order",
	   "Account Payable",
	   "Sales Invoice",
	   "Pending For Delivery",
	   "Sales Receivables",
	   "Sales Void",
	   "Sales Overall",
	   "History (Sales)",
	   "History (Orders)",
	   "Top Grosser (By Branch)",
	   "Top Grosser (By Item)",
	   "Backup Data",
	   "Bad Order",
	   "Bad Order List"
	};

	@SuppressWarnings("unchecked")
	public JSONObject insertUser(Users users) throws SQLException, ClassNotFoundException {
    	JSONObject jsonObject = new JSONObject();
    	IndexConnection indexConn = new IndexConnection();
    	Connection conn = indexConn.initialize();
        Statement stmt = conn.createStatement();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");  
        LocalDateTime now = LocalDateTime.now();  

        try {
            stmt.execute("INSERT INTO m_user (username, password, level, date_created) "
            		+ "VALUES ('" +
            		users.getUsername() + "', '" +
            		users.getPassword() + "', '" +
            		users.getLevel() + "', '" +
            		dtf.format(now) + "')");

            stmt.close();
            conn.close();

	        jsonObject.put("result", true);
        } catch (Exception e) { 
            e.printStackTrace();

	        jsonObject.put("result", false);
        }

		return jsonObject;
	}

	@SuppressWarnings("unchecked")
	public JSONObject updateUser(Users users) throws SQLException, ClassNotFoundException {
    	JSONObject jsonObject = new JSONObject();
    	IndexConnection indexConn = new IndexConnection();
    	Connection conn = indexConn.initialize();
        Statement stmt = conn.createStatement();

    	String query = "UPDATE m_user SET "
        		+ "username = '"+users.getUsername()+"', "
        		+ "password = '"+users.getPassword()+"', "
                + "level = '"+users.getLevel()+"' "
        		+ "WHERE id = '"+users.getId()+"'";

        try {
            stmt.execute(query);

            stmt.close();
            conn.close();

	        jsonObject.put("result", true);
        } catch (Exception e) { 
            e.printStackTrace();

	        jsonObject.put("result", false);
	        jsonObject.put("message", e.getMessage());
	        jsonObject.put("query", query);
        }

		return jsonObject;
	}

	@SuppressWarnings("unchecked")
	public JSONObject deleteUser(Users users) throws SQLException, ClassNotFoundException {
		int countAdminUser = getCountAdminUsers(users.getId());
    	JSONObject jsonObject = new JSONObject();

		if (countAdminUser == 1) {
	        jsonObject.put("result", false);
	        jsonObject.put("message", "Cannot delete last admin user.");

			return jsonObject;
		}

    	IndexConnection indexConn = new IndexConnection();
    	Connection conn = indexConn.initialize();
        Statement stmt = conn.createStatement();

    	String query = "DELETE FROM m_user WHERE id = "+users.getId()+"";

        try {
            stmt.execute(query);

            stmt.close();
            conn.close();

	        jsonObject.put("result", true);
        } catch (Exception e) { 
            e.printStackTrace();

	        jsonObject.put("result", false);
	        jsonObject.put("message", e.getMessage());
	        jsonObject.put("query", query);
        }

		return jsonObject;
	}

	@SuppressWarnings("unchecked")
	public JSONObject selectUsers() throws SQLException, ClassNotFoundException {
    	JSONObject jsonObject = new JSONObject();
    	JSONArray jsonArray = new JSONArray();
    	IndexConnection indexConn = new IndexConnection();
    	Connection conn = indexConn.initialize();
    	
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM m_user");

            while (rs.next()) {
            	JSONObject json = new JSONObject();
            	json.put("id", rs.getInt("id"));
            	json.put("username", rs.getString("username"));
            	json.put("password", rs.getString("password"));
            	json.put("level", rs.getInt("level"));
            	json.put("date_created", rs.getString("date_created"));
            	json.put("pages", getAllUserPageByUserId(rs.getInt("id")));

            	jsonArray.add(json);

		        jsonObject.put("m_user", jsonArray);
            }
            
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

		return jsonObject;
	}
	
	@SuppressWarnings("unchecked")
	public JSONArray getAllUserPageByUserId(int userId) throws SQLException, ClassNotFoundException {
    	JSONArray jsonArray = new JSONArray();
    	IndexConnection conn = new IndexConnection();

        try {
            Statement stmt = conn.initialize().createStatement();
            ResultSet rs = stmt.executeQuery("SELECT mup.* FROM m_user_pages mup WHERE mup.user_id="+userId);

            while (rs.next()) {
            	JSONObject json = new JSONObject();
            	json.put("user_id", rs.getInt("user_id"));
            	json.put("user_pages", userPages[rs.getInt("user_pages")]);

            	jsonArray.add(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

		return jsonArray;
	}

	@SuppressWarnings("unchecked")
	private int getCountAdminUsers(int userId) throws SQLException, ClassNotFoundException {
    	IndexConnection indexConn = new IndexConnection();
    	Connection conn = indexConn.initialize();

        int userAdminCount = 0;
        int userLevel = getLevelByUserId(userId);

        if (userLevel != 0) {
        	return userAdminCount;
        }

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM m_user WHERE level = '0'");

            while (rs.next()) {
            	userAdminCount++;
            }

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

		return userAdminCount;
	}

	@SuppressWarnings("unchecked")
	private int getLevelByUserId(int userId) throws SQLException, ClassNotFoundException {
    	IndexConnection indexConn = new IndexConnection();
    	Connection conn = indexConn.initialize();

        int userLevel = 0;

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT level FROM m_user WHERE id = "+userId+"");

            while (rs.next()) {
            	userLevel = rs.getInt("level");
            }

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

		return userLevel;
	}
}
