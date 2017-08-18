package dao;

import com.google.gson.Gson;
import components.Utils;

import models.User;

/**
 * DAO class for users management.
 * 
 * @author Manuel Calzolari
 */
public class Users {
	private String access_token;

	public Users(String access_token) {
		this.access_token = access_token;
	}

	/**
	 * Returns data of the user with the given id.
	 */
	public User query(String id) {
		String fql_query_url = "https://graph.facebook.com/" + id + "?access_token=" + access_token;
		String fql_query_result = Utils.UrlGetContents(fql_query_url);
		Gson gson = new Gson();
		User fql_query_obj = gson.fromJson(fql_query_result, User.class);
		return fql_query_obj;
	}
}
