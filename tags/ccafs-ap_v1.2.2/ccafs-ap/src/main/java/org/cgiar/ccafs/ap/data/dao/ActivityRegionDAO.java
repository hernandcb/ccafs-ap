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

import org.cgiar.ccafs.ap.data.dao.mysql.MySQLActivityRegionDAO;

import java.util.List;
import java.util.Map;

import com.google.inject.ImplementedBy;

@ImplementedBy(MySQLActivityRegionDAO.class)
public interface ActivityRegionDAO {

  /**
   * Delete all the regions related to the activity given
   * 
   * @param activityID - activity identifier
   * @return true if the regions was successfully deleted. False otherwise.
   */
  public boolean deleteActivityRegions(int activityID);

  /**
   * Get all the region locations related to the activity given
   * 
   * @param activityID - Activity identifier
   * @return a List of maps with the information
   */
  public List<Map<String, String>> getActivityRegions(int activityID);

  /**
   * Save the activity region given into the database.
   * 
   * @param activityID - The activity identifier.
   * @param countryID - The region identifier.
   * @return true if the data was successfully saved. False otherwise
   */
  public boolean saveActivityRegion(int activityID, String regionID);
}
