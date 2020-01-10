package com.interlink.list;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.interlink.index.IndexConnection;
import com.interlink.map.Users;
import com.interlink.map.Prices;

public class ListPrice {

	@SuppressWarnings("unchecked")
	public JSONObject deletePrice(Prices prices) throws SQLException, ClassNotFoundException {
    	JSONObject jsonObject = new JSONObject();

    	IndexConnection indexConn = new IndexConnection();
    	Connection conn = indexConn.initialize();
        Statement stmt = conn.createStatement();

    	String query = "DELETE FROM m_price_list WHERE id = "+prices.getId()+"";

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
	public JSONObject getAllPrice(Prices prices) throws SQLException, ClassNotFoundException {
    	JSONObject jsonObject = new JSONObject();
    	JSONArray jsonArray = new JSONArray();
    	IndexConnection indexConn = new IndexConnection();
    	Connection conn = indexConn.initialize();
    	String pageLimit = getPageLimit(prices.getPages());

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM m_price_list ORDER BY id ASC "+pageLimit);

            while (rs.next()) {
            	JSONObject json = new JSONObject();
            	json.put("id", rs.getInt("id"));
            	json.put("name", rs.getString("name"));
            	json.put("piece", rs.getString("piece"));
            	json.put("code", rs.getString("code"));
            	json.put("price", rs.getString("price"));
            	json.put("price_purchase", rs.getString("price_purchase"));
            	json.put("discount", rs.getString("discount"));
            	json.put("unit", rs.getString("unit"));

            	jsonArray.add(json);

		        jsonObject.put("m_price_list", jsonArray);
            }

            jsonObject.put("pages", getPages(prices.getPages()));
            
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

		return jsonObject;
	}

	private int getPages(int currentPage) throws SQLException, ClassNotFoundException {
    	IndexConnection indexConn = new IndexConnection();
    	Connection conn = indexConn.initialize();
    	int totalPages = 0;

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT count(*) as count FROM m_price_list");

            while (rs.next()) {
            	totalPages = Math.round(rs.getInt("count") / 10) + 1;
            }

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

		return totalPages;
	}
	
	private String getPageLimit(int page) {
		page = (page < 1) ? 1 : page;
		int pageCount = (page - 1) * 10;

		return "LIMIT "+pageCount+", 10";
	}
}
