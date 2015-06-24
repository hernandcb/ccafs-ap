/*****************************************************************
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
 *****************************************************************/

package org.cgiar.ccafs.ap.data.manager;

import org.cgiar.ccafs.ap.data.manager.impl.HistoryManagerImpl;
import org.cgiar.ccafs.ap.data.model.LogHistory;

import java.util.List;

import com.google.inject.ImplementedBy;


/**
 * @author Hernán David Carvajal B. - CIAT/CCAFS
 */

@ImplementedBy(HistoryManagerImpl.class)
public interface HistoryManager {

  /**
   * This method return the last five changes made in the interface of
   * project description to the project identified by the value received by parameter.
   * 
   * @param projectID - Project identifier
   * @return a list of logHistory objects that contains the information.
   */
  public <T> List<LogHistory> getProjectDescriptionHistory(int projectID);

  /**
   * This method return the last five changes made in the interface of project partners (Partner lead) to the project
   * identified by the value received by parameter.
   * 
   * @param projectID - Project identifier
   * @param partnerType - Array of string with the type of partners (ex. 'PL', 'PPA')
   * @return a list of maps with the information
   */
  public List<LogHistory> getProjectPartnersHistory(int projectID, String[] partnerTypes);

}
