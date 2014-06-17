/*
 * This file is part of CCAFS Planning and Reporting Platform.
 * CCAFS P&R is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * at your option) any later version.
 * CCAFS P&R is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with CCAFS P&R. If not, see <http://www.gnu.org/licenses/>.
 */

package org.cgiar.ccafs.ap.data.dao;

import org.cgiar.ccafs.ap.data.dao.mysql.MySQLActivityOtherSiteDAO;

import java.util.List;
import java.util.Map;

import com.google.inject.ImplementedBy;

@ImplementedBy(MySQLActivityOtherSiteDAO.class)
public interface ActivityOtherSiteDAO {

  /**
   * Delete all the other locations related with the activity given.
   * 
   * @param activityID - The activity identifier
   * @return true if it was successfully saved. False otherwise.
   */
  public boolean deleteActivityOtherSites(int activityID);

  /**
   * Get all the other sites related to the activity given
   * 
   * @param activityID - the activity identifier
   * @return a list of maps with the information
   */
  public List<Map<String, String>> getActivityOtherSites(int activityID);

  /**
   * Save the other location into the DAO
   * 
   * @param otherSite - Data to be saved
   * @param activityID - The activity identifier
   * @return true if it was successfully saved. False otherwise
   */
  public boolean saveActivityOtherSites(Map<String, String> otherSite, int activityID);
}
