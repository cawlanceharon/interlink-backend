package com.interlink.dashboard;

import java.sql.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.mariadb.jdbc.*;

import com.interlink.index.IndexConnection;

public class IndexDashboard {

	public JSONObject getAllDashboard() throws SQLException, ClassNotFoundException {
		IndexConnection conn = new IndexConnection();

    	JSONObject jsonObject = new JSONObject();
        Statement stmt = conn.initialize().createStatement();

        try {
            ResultSet rs = stmt.executeQuery(
            		"SELECT "
            		+ "(SELECT count(id) AS count FROM m_branch_list WHERE MONTH(date) = MONTH(CURRENT_DATE()) AND YEAR(date) = YEAR(CURRENT_DATE())) as this_month,"
            		+ "(SELECT count(id) AS count FROM m_branch_list WHERE DATE(date) = CURDATE()) as today,"
            		+ "(SELECT count(id) AS count FROM m_branch_list WHERE DATE(date) = NOW() - INTERVAL 1 DAY) as yesterday,"
            		+ "(SELECT count(id) AS count FROM m_branch_list) as total");

            while (rs.next()) {
            	JSONObject json = new JSONObject();
            	json.put("this_month", rs.getInt("this_month"));
            	json.put("today", rs.getInt("today"));
            	json.put("yesterday", rs.getInt("yesterday"));
            	json.put("total", rs.getInt("total"));

		        jsonObject.put("items", json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            ResultSet rs = stmt.executeQuery(
            		"SELECT "
            		+ "(SELECT count(id) AS count FROM m_sales_invoice WHERE status='unpaid' AND delivery_date IS NULL AND delete_flg = 0 AND MONTH(date) = MONTH(CURRENT_DATE()) AND YEAR(date) = YEAR(CURRENT_DATE())) as this_month,"
            		+ "(SELECT count(id) AS count FROM m_sales_invoice WHERE status='unpaid' AND delivery_date IS NULL AND delete_flg = 0 AND DATE(date) = CURDATE()) as today,"
            		+ "(SELECT count(id) AS count FROM m_sales_invoice WHERE status='unpaid' AND delivery_date IS NULL AND delete_flg = 0 AND DATE(date) = NOW() - INTERVAL 1 DAY) as yesterday,"
            		+ "(SELECT count(id) AS count FROM m_sales_invoice WHERE status='unpaid' AND delivery_date IS NULL AND delete_flg = 0) as total");

            while (rs.next()) {
            	JSONObject json = new JSONObject();
            	json.put("this_month", rs.getInt("this_month"));
            	json.put("today", rs.getInt("today"));
            	json.put("yesterday", rs.getInt("yesterday"));
            	json.put("total", rs.getInt("total"));

		        jsonObject.put("delivery", json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        try {
            ResultSet rs = stmt.executeQuery(
            		"SELECT "
            		+ "(SELECT count(id) AS count FROM m_sales_invoice WHERE status='unpaid' AND delivery_date IS NOT NULL AND delete_flg = 0 AND MONTH(date) = MONTH(CURRENT_DATE()) AND YEAR(date) = YEAR(CURRENT_DATE())) as this_month,"
            		+ "(SELECT count(id) AS count FROM m_sales_invoice WHERE status='unpaid' AND delivery_date IS NOT NULL AND delete_flg = 0 AND DATE(date) = CURDATE()) as today,"
            		+ "(SELECT count(id) AS count FROM m_sales_invoice WHERE status='unpaid' AND delivery_date IS NOT NULL AND delete_flg = 0 AND DATE(date) = NOW() - INTERVAL 1 DAY) as yesterday,"
            		+ "(SELECT count(id) AS count FROM m_sales_invoice WHERE status='unpaid' AND delivery_date IS NOT NULL AND delete_flg = 0) as total");

            while (rs.next()) {
            	JSONObject json = new JSONObject();
            	json.put("this_month", rs.getInt("this_month"));
            	json.put("today", rs.getInt("today"));
            	json.put("yesterday", rs.getInt("yesterday"));
            	json.put("total", rs.getInt("total"));

		        jsonObject.put("sales", json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        try {
            ResultSet rs = stmt.executeQuery(
            		"SELECT "
            		+ "(SELECT count(id) AS count FROM m_sales_invoice WHERE delete_flg=1 AND MONTH(date) = MONTH(CURRENT_DATE()) AND YEAR(date) = YEAR(CURRENT_DATE())) as this_month,"
            		+ "(SELECT count(id) AS count FROM m_sales_invoice WHERE delete_flg=1 AND DATE(date) = CURDATE()) as today,"
            		+ "(SELECT count(id) AS count FROM m_sales_invoice WHERE delete_flg=1 AND DATE(date) = NOW() - INTERVAL 1 DAY) as yesterday,"
            		+ "(SELECT count(id) AS count FROM m_sales_invoice WHERE delete_flg=1) as total");

            while (rs.next()) {
            	JSONObject json = new JSONObject();
            	json.put("this_month", rs.getInt("this_month"));
            	json.put("today", rs.getInt("today"));
            	json.put("yesterday", rs.getInt("yesterday"));
            	json.put("total", rs.getInt("total"));

		        jsonObject.put("cancelled_reports", json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
		return jsonObject;
	}
}
