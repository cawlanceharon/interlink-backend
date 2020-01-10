package com.interlink.index;

import org.json.simple.JSONObject;

import java.sql.SQLException;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.interlink.dashboard.IndexDashboard;
import com.interlink.list.ListPrice;
import com.interlink.map.Users;
import com.interlink.map.Prices;
import com.interlink.users.IndexUsers;

@RestController
public class IndexController {

	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String initialize() {
		return "Interlink API Libraries.";
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/users/{action}", method = RequestMethod.POST)
	public JSONObject index(@PathVariable String action, @RequestBody Users users) throws ClassNotFoundException, SQLException {
		IndexUsers indexUsers = new IndexUsers();

	    switch (action) {
	        case "insert":
	        	return indexUsers.insertUser(users);
	        case "update":
	        	return indexUsers.updateUser(users);
	        case "delete":
	        	return indexUsers.deleteUser(users);
	        default:
	    		return indexUsers.selectUsers();
	    }
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/list/price/{action}", method = RequestMethod.POST)
	public JSONObject listPrice(@PathVariable String action, @RequestBody Prices prices) throws ClassNotFoundException, SQLException {
		ListPrice listPrice = new ListPrice();

	    switch (action) {
        case "insert":
//        	return indexUsers.insertUser(users);
        case "update":
//        	return indexUsers.updateUser(users);
        case "delete":
        	return listPrice.deletePrice(prices);
        default:
    		return listPrice.getAllPrice(prices);
	    }
	}
	
	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/dashboard", method = RequestMethod.GET)
	public JSONObject home() throws ClassNotFoundException, SQLException {
		IndexDashboard dashboard = new IndexDashboard();

		return dashboard.getAllDashboard();
	}
}
