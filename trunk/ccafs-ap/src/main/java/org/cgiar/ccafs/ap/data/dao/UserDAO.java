package org.cgiar.ccafs.ap.data.dao;

import org.cgiar.ccafs.ap.data.dao.mysql.MySQLUserDAO;

import java.util.Map;

import com.google.inject.ImplementedBy;

@ImplementedBy(MySQLUserDAO.class)
public interface UserDAO {

  /**
   * Get a user with the given email.
   * 
   * @param email
   * @return a Map with the user information or null if no user found.
   */
  public Map<String, String> getUser(String email);

  /**
   * Save the user data into the database.
   * 
   * @param userData - Information to be saved.
   * @return true if the information was successfully saved. False otherwise.
   */
  public boolean saveUser(Map<String, String> userData);
}
