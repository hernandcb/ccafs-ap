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

import org.cgiar.ccafs.ap.data.dao.mysql.MySQLOutcomeDAO;

import java.util.List;
import java.util.Map;

import com.google.inject.ImplementedBy;

@ImplementedBy(MySQLOutcomeDAO.class)
public interface OutcomeDAO {

  /**
   * Add a list of outcomes into the database.
   * 
   * @param newOutcomes - List of Maps of outcomes to be added.
   * @return true if all the outcomes were successfully added into the database, or false if any problem occurred.
   */
  public boolean addOutcomes(List<Map<String, String>> newOutcomes);

  /**
   * Get a list with information of all outcomes that belong to a given leader and logframe identifier.
   * 
   * @param activity_leader_id - Leader identifier.
   * @param logframe_id - logframe identifier.
   * @return a list of Maps with the information of each outcome.
   */
  public List<Map<String, String>> getOutcomes(int leader_id, int logframe_id);

  /**
   * Get a list with information of all outcomes that belong to a given leader and logframe identifier to display them
   * in a summary.
   * 
   * @param activity_leader_id - Leader identifier or -1 to indicate all.
   * @param logframe_id - logframe identifier or -1 to indicate all.
   * @return a list of Maps with the information of each outcome.
   */
  public List<Map<String, String>> getOutcomesListForSummary(int leader_id, int logframe_id);

  /**
   * Remove a list of outcomes that belong to a specific leader and logframe.
   * 
   * @param leader_id - leader identifier.
   * @param logframe_id - logframe identifier.
   * @return true if all the outcomes were successfully removed, or false otherwise.
   */
  public boolean removeOutcomes(int leader_id, int logframe_id);
}
