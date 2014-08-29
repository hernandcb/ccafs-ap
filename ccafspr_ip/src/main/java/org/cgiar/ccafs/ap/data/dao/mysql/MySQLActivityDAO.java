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
 * ***************************************************************
 */
package org.cgiar.ccafs.ap.data.dao.mysql;

import org.cgiar.ccafs.ap.data.dao.ActivityDAO;
import org.cgiar.ccafs.ap.data.dao.DAOManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Javier Andrés Gallego B.
 * @author Hernán David Carvajal B.
 */
public class MySQLActivityDAO implements ActivityDAO {

  // Logger
  private static Logger LOG = LoggerFactory.getLogger(MySQLActivityDAO.class);

  private DAOManager databaseManager;

  @Inject
  public MySQLActivityDAO(DAOManager databaseManager) {
    this.databaseManager = databaseManager;
  }

  @Override
  public boolean deleteActivitiesByProject(int projectID) {
    LOG.debug(">> deleteActivitiesByProject(projectId={})", projectID);

    StringBuilder query = new StringBuilder();
    query.append("DELETE a FROM activities a ");
    query.append("WHERE a.project_id = ? ");

    int rowsDeleted = databaseManager.delete(query.toString(), new Object[] {projectID});
    if (rowsDeleted >= 0) {
      LOG.debug("<< deleteActivitiesByProject():{}", true);
      return true;
    }
    LOG.debug("<< deleteActivitiesByProject():{}", false);
    return false;
  }

  @Override
  public boolean deleteActivity(int activityId) {
    LOG.debug(">> deleteActivity(id={})", activityId);

    String query = "DELETE FROM activities WHERE id= ?";

    int rowsDeleted = databaseManager.delete(query, new Object[] {activityId});
    if (rowsDeleted >= 0) {
      LOG.debug("<< deleteActivity():{}", true);
      return true;
    }

    LOG.debug("<< deleteActivity:{}", false);
    return false;
  }

  @Override
  public boolean deleteActivityIndicator(int activityID, int indicatorID) {
    LOG.debug(">> deleteActivityIndicator(activityID={}, indicatorID={})", activityID, indicatorID);

    String query = "DELETE FROM ip_activity_indicators WHERE activity_id = ? AND id = ?";

    int rowsDeleted = databaseManager.delete(query, new Object[] {activityID, indicatorID});
    if (rowsDeleted >= 0) {
      LOG.debug("<< deleteActivityIndicator():{}", true);
      return true;
    }

    LOG.debug("<< deleteActivityIndicator:{}", false);
    return false;
  }

  @Override
  public boolean deleteActivityOutput(int activityID, int outputID) {
    LOG.debug(">> deleteActivityOutput(activityID={}, outputID={})", activityID, outputID);

    String query = "DELETE FROM ip_activity_contributions WHERE activity_id = ? AND mog_id = ?";

    int rowsDeleted = databaseManager.delete(query, new Object[] {activityID, outputID});
    if (rowsDeleted >= 0) {
      LOG.debug("<< deleteActivityOutput():{}", true);
      return true;
    }

    LOG.debug("<< deleteActivityOutput:{}", false);
    return false;
  }

  @Override
  public boolean existActivity(int activityID) {
    LOG.debug(">> existActivity activityID = {} )", activityID);
    StringBuilder query = new StringBuilder();
    query.append("SELECT COUNT(id) FROM activities WHERE id = ");
    query.append(activityID);
    boolean exists = false;
    try (Connection con = databaseManager.getConnection()) {
      ResultSet rs = databaseManager.makeQuery(query.toString(), con);
      if (rs.next()) {
        if (rs.getInt(1) > 0) {
          exists = true;
        }
      }
      con.close();
    } catch (SQLException e) {
      LOG.error("Exception arised getting the activity id.", activityID, e.getMessage());
    }
    return exists;
  }

  @Override
  public List<Map<String, String>> getActivitiesByProject(int projectID) {
    LOG.debug(">> getActivitiesByProject projectID = {} )", projectID);

    StringBuilder query = new StringBuilder();
    query.append("SELECT a.*   ");
    query.append("FROM activities as a ");
    query.append("INNER JOIN projects p ON a.project_id = p.id ");
    query.append("WHERE a.project_id=  ");
    query.append(projectID);

    LOG.debug("-- getActivitiesByProject() > Calling method executeQuery to get the results");
    return getData(query.toString());
  }

  @Override
  public Map<String, String> getActivityById(int activityID) {
    Map<String, String> activityData = new HashMap<String, String>();
    LOG.debug(">> getActivityById( activityID = {} )", activityID);
    StringBuilder query = new StringBuilder();
    query.append("SELECT a.*   ");
    query.append("FROM activities as a ");
    query.append("WHERE a.id=  ");
    query.append(activityID);
    try (Connection con = databaseManager.getConnection()) {
      ResultSet rs = databaseManager.makeQuery(query.toString(), con);
      if (rs.next()) {
        activityData.put("id", rs.getString("id"));
        activityData.put("title", rs.getString("title"));
        activityData.put("description", rs.getString("description"));
        if (rs.getDate("startDate") != null) {
          activityData.put("startDate", rs.getDate("startDate").toString());
        }
        if (rs.getDate("endDate") != null) {
          activityData.put("endDate", rs.getDate("endDate").toString());
        }
        activityData.put("leader_id", rs.getString("leader_id"));
        activityData.put("expected_leader_id", rs.getString("expected_leader_id"));
        activityData.put("created", rs.getTimestamp("created").getTime() + "");
        if (rs.getString("is_global") != null) {
          activityData.put("is_global", rs.getString("is_global"));
        }

      }
      con.close();
    } catch (SQLException e) {
      LOG.error("Exception arised getting the activity {}.", activityID, e);
    }
    LOG.debug("-- getActivityById() > Calling method executeQuery to get the results");
    return activityData;
  }

  @Override
  public List<Integer> getActivityIdsEditable(int programID) {
    LOG.debug(">> getActivityIdsEditable( programID={})", new Object[] {programID});
    List<Integer> activityIds = new ArrayList<>();
    try (Connection connection = databaseManager.getConnection()) {
      StringBuilder query = new StringBuilder();
      query.append("SELECT a.id ");
      query.append("FROM activities a ");
      query.append("INNER JOIN projects p ON a.project_id = p.id ");
      query.append("INNER JOIN ip_programs pr ON p.program_creator_id = pr.id ");
      query.append("WHERE p.program_creator_id = ");
      query.append(programID);
      ResultSet rs = databaseManager.makeQuery(query.toString(), connection);
      while (rs.next()) {
        activityIds.add(rs.getInt("id"));
      }
      rs.close();
    } catch (SQLException e) {
      LOG.error("-- getActivityIdsEditable() > There was an error getting the data for  programID={}.",
        new Object[] {programID}, e);
      return null;
    }
    LOG.debug("<< getActivityIdsEditable():{}", activityIds);
    return activityIds;
  }

  @Override
  public List<Map<String, String>> getActivityIndicators(int activityID) {
    LOG.debug(">> getActivityIndicators( activityID = {} )", activityID);
    List<Map<String, String>> indicatorsDataList = new ArrayList<>();

    StringBuilder query = new StringBuilder();
    query.append("SELECT ai.id, ai.description, ai.target, aip.id as 'parent_id', ");
    query.append("aip.description as 'parent_description', aip.target as 'parent_target' ");
    query.append("FROM ip_activity_indicators as ai ");
    query.append("INNER JOIN ip_indicators aip ON ai.parent_id = aip.id ");
    query.append("WHERE ai.activity_id=  ");
    query.append(activityID);

    try (Connection con = databaseManager.getConnection()) {
      ResultSet rs = databaseManager.makeQuery(query.toString(), con);
      while (rs.next()) {
        Map<String, String> indicatorData = new HashMap<String, String>();

        indicatorData.put("id", rs.getString("id"));
        indicatorData.put("description", rs.getString("description"));
        indicatorData.put("target", rs.getString("target"));
        indicatorData.put("parent_id", rs.getString("parent_id"));
        indicatorData.put("parent_description", rs.getString("parent_description"));
        indicatorData.put("parent_target", rs.getString("parent_target"));

        indicatorsDataList.add(indicatorData);
      }
      rs.close();
    } catch (SQLException e) {
      String exceptionMessage = "-- getActivityIndicators() > Exception raised trying ";
      exceptionMessage += "to get the activity indicators for activity  " + activityID;

      LOG.error(exceptionMessage, e);
      return null;
    }
    LOG.debug("<< getActivityIndicators():indicatorsDataList.size={}", indicatorsDataList.size());
    return indicatorsDataList;
  }

  @Override
  public int getActivityLeaderId(int activityID) {
    LOG.debug(">> getActivityLeaderId(activityID={})", new Object[] {activityID});
    int leaderID = -1;
    try (Connection connection = databaseManager.getConnection()) {
      StringBuilder query = new StringBuilder();
      query.append("SELECT a.leader_id FROM activities a WHERE a.id= ");
      query.append(activityID);
      ResultSet rs = databaseManager.makeQuery(query.toString(), connection);
      if (rs.next()) {
        leaderID = rs.getInt(1) == 0 ? -1 : rs.getInt(1);
      }
      rs.close();
    } catch (SQLException e) {
      LOG.error("-- getActivityLeaderId() > There was an error getting the data for  activityID={}.",
        new Object[] {activityID}, e.getMessage());
    }
    LOG.debug("<< getActivityIdsEditable(): leaderID={}", leaderID);
    return leaderID;
  }

  @Override
  public List<Map<String, String>> getActivityOutputs(int activityID) {
    LOG.debug(">> getActivityOutputs( activityID = {} )", activityID);
    List<Map<String, String>> outputsDataList = new ArrayList<>();

    StringBuilder query = new StringBuilder();
    query.append("SELECT ipe.id, ipe.description, pe.id as 'parent_id',  ");
    query.append("pe.description as 'parent_description' ");
    query.append("FROM ip_elements ipe ");
    query.append("INNER JOIN ip_activity_contributions ipc ON ipc.mog_id = ipe.id ");
    query.append("INNER JOIN ip_elements pe ON ipc.midOutcome_id = pe.id ");
    query.append("WHERE ipc.activity_id=  ");
    query.append(activityID);

    try (Connection con = databaseManager.getConnection()) {
      ResultSet rs = databaseManager.makeQuery(query.toString(), con);
      while (rs.next()) {
        Map<String, String> indicatorData = new HashMap<String, String>();

        indicatorData.put("id", rs.getString("id"));
        indicatorData.put("description", rs.getString("description"));
        indicatorData.put("parent_id", rs.getString("parent_id"));
        indicatorData.put("parent_description", rs.getString("parent_description"));

        outputsDataList.add(indicatorData);
      }
      rs.close();
    } catch (SQLException e) {
      String exceptionMessage = "-- getActivityOutputs() > Exception raised trying ";
      exceptionMessage += "to get the activity outputs for activity  " + activityID;

      LOG.error(exceptionMessage, e);
      return null;
    }
    LOG.debug("<< getActivityOutputs():outputsDataList.size={}", outputsDataList.size());
    return outputsDataList;
  }


  private List<Map<String, String>> getData(String query) {
    LOG.debug(">> executeQuery(query='{}')", query);
    List<Map<String, String>> activitiesList = new ArrayList<>();

    try (Connection con = databaseManager.getConnection()) {
      ResultSet rs = databaseManager.makeQuery(query, con);
      while (rs.next()) {
        Map<String, String> activityData = new HashMap<String, String>();
        activityData.put("id", rs.getString("id"));
        activityData.put("title", rs.getString("title"));
        activityData.put("description", rs.getString("description"));
        if (rs.getDate("startDate") != null) {
          activityData.put("startDate", rs.getDate("startDate").toString());
        }
        if (rs.getDate("endDate") != null) {
          activityData.put("endDate", rs.getDate("endDate").toString());
        }
        activityData.put("leader_id", rs.getString("leader_id"));
        activityData.put("expected_leader_id", rs.getString("expected_leader_id"));
        activityData.put("created", rs.getTimestamp("created").getTime() + "");
        if (rs.getString("is_global") != null) {
          activityData.put("is_global", rs.getString("is_global"));
        }

        activitiesList.add(activityData);
      }
      rs.close();
    } catch (SQLException e) {
      String exceptionMessage = "-- executeQuery() > Exception raised trying ";
      exceptionMessage += "to execute the following query " + query;

      LOG.error(exceptionMessage, e);
      return null;
    }
    LOG.debug("<< executeQuery():activitiesList.size={}", activitiesList.size());
    return activitiesList;
  }

  @Override
  public Map<String, String> getExpectedActivityLeader(int activityID) {
    Map<String, String> activityLeaderData = new HashMap<String, String>();
    StringBuilder query = new StringBuilder();
    query.append("SELECT eal.*   ");
    query.append("FROM expected_activity_leaders as eal ");
    query.append("INNER JOIN activities a ON eal.id=a.expected_leader_id ");
    query.append("WHERE a.id=  ");
    query.append(activityID);
    try (Connection con = databaseManager.getConnection()) {
      ResultSet rs = databaseManager.makeQuery(query.toString(), con);
      if (rs.next()) {
        activityLeaderData.put("id", rs.getString("id"));
        activityLeaderData.put("institution_id", rs.getString("institution_id"));
        activityLeaderData.put("name", rs.getString("name"));
        activityLeaderData.put("email", rs.getString("email"));
      }
      con.close();
    } catch (SQLException e) {
      LOG.error("Exception arised getting the activity {}.", activityID, e);
    }
    return activityLeaderData;
  }

  @Override
  public boolean isOfficialExpectedLeader(int activityID) {
    boolean isOfficialLeader = false;
    StringBuilder query = new StringBuilder();
    query.append("SELECT eal.is_official   ");
    query.append("FROM expected_activity_leaders as eal ");
    query.append("INNER JOIN activities a ON eal.id=a.expected_leader_id ");
    query.append("WHERE a.id=  ");
    query.append(activityID);
    try (Connection con = databaseManager.getConnection()) {
      ResultSet rs = databaseManager.makeQuery(query.toString(), con);
      if (rs.next()) {
        isOfficialLeader = rs.getBoolean("is_official");
      }
      con.close();
    } catch (SQLException e) {
      LOG.error("Exception arised getting the activity {}.", activityID, e);
    }
    return isOfficialLeader;
  }

  @Override
  public int saveActivity(int projectID, Map<String, Object> activityData) {
    LOG.debug(">> saveActivity(activityData={})", activityData);
    StringBuilder query = new StringBuilder();

    Object[] values;
    if (activityData.get("id") == null) {
      // Insert new activity record
      query.append("INSERT INTO activities (project_id) ");
      query.append("VALUES (?) ");
      values = new Object[1];
      values[0] = projectID;
      int newId = databaseManager.saveData(query.toString(), values);
      if (newId <= 0) {
        LOG.error("A problem happened trying to add a new activity with id={}", projectID);
        return -1;
      }
      return newId;
    } else {
      // update activity record
      query
        .append("UPDATE activities SET title = ?, description = ?, startDate = ?, endDate = ?, expected_leader_id = ?, is_global=? ");
      query.append("WHERE id = ? ");
      values = new Object[7];
      values[0] = activityData.get("title");
      values[1] = activityData.get("description");
      values[2] = activityData.get("startDate");
      values[3] = activityData.get("endDate");
      values[4] = activityData.get("expected_leader_id");
      values[5] = activityData.get("is_global");
      values[6] = activityData.get("id");
      int result = databaseManager.saveData(query.toString(), values);
      if (result == -1) {
        LOG.error("A problem happened trying to update the activity identified with the id = {}",
          activityData.get("id"));
        return -1;
      }
      return result;
    }
  }

  @Override
  public boolean saveActivityIndicators(Map<String, String> indicatorData) {
    LOG.debug(">> saveActivityIndicators(indicatorData={})", indicatorData);
    StringBuilder query = new StringBuilder();

    Object[] values;
    // Insert new activity indicator record
    query.append("INSERT INTO ip_activity_indicators (id, description, target, activity_id, parent_id) ");
    query.append("VALUES (?, ?, ?, ?, ?) ");
    values = new Object[5];
    values[0] = indicatorData.get("id");
    values[1] = indicatorData.get("description");
    values[2] = indicatorData.get("target");
    values[3] = indicatorData.get("activity_id");
    values[4] = indicatorData.get("parent_id");

    int newId = databaseManager.saveData(query.toString(), values);
    if (newId == -1) {
      LOG
        .warn(
          "-- saveActivityIndicators() > A problem happened trying to add a new activity indicator. Data tried to save was: {}",
          indicatorData);
      LOG.debug("<< saveActivityIndicators(): {}", false);
      return false;
    }

    LOG.debug("<< saveActivityIndicators(): {}", true);
    return true;
  }

  @Override
  public int saveActivityLeader(int activityID, int employeeID) {
    LOG.debug(">> saveActivityLeader(employeeID={}, activityID={})", new Object[] {employeeID, activityID});
    StringBuilder query = new StringBuilder();
    int result = -1;
    Object[] values;

    // update activity record
    query.append("UPDATE activities SET leader_id = ? ");
    query.append("WHERE id = ? ");
    values = new Object[2];
    values[0] = employeeID;
    values[1] = activityID;
    result = databaseManager.saveData(query.toString(), values);
    if (result == -1) {
      LOG.error("A problem happened trying to update the activity leader with the id = {}", activityID);
      return -1;
    }

    LOG.debug("<< saveActivityLeader():{}", result);
    return result;
  }

  @Override
  public int saveActivityOutput(Map<String, String> outputData) {
    LOG.debug(">> saveActivityOutput(outputData={})", outputData);
    StringBuilder query = new StringBuilder();

    Object[] values;
    // Insert new activity indicator record
    query.append("INSERT IGNORE INTO ip_activity_contributions (activity_id, mog_id, midOutcome_id) ");
    query.append("VALUES (?, ?, ?) ");
    values = new Object[3];
    values[0] = outputData.get("activity_id");
    values[1] = outputData.get("mog_id");
    values[2] = outputData.get("midOutcome_id");

    int newId = databaseManager.saveData(query.toString(), values);
    if (newId == -1) {
      LOG.warn(
        "-- saveActivityOutput() > A problem happened trying to add a new activity output. Data tried to save was: {}",
        outputData);
      LOG.debug("<< saveActivityIndicators(): {}", -1);
      return -1;
    }

    LOG.debug("<< saveActivityIndicators(): {}", newId);
    return newId;
  }

  @Override
  public int
    saveExpectedActivityLeader(int activityID, Map<String, Object> activityLeaderData, boolean isOfficialLeader) {
    LOG.debug(">> saveExpectedActivityLeader(activityLeaderData={})", activityLeaderData);
    StringBuilder query = new StringBuilder();
    int result = -1;
    Object[] values;
    if (activityLeaderData.get("id") == null) {
      // Insert new activity record
      query.append("INSERT INTO expected_activity_leaders (institution_id, name, email, is_official) ");
      query.append("VALUES (?,?,?,?) ");
      values = new Object[4];
      values[0] = activityLeaderData.get("institution_id");
      values[1] = activityLeaderData.get("name");
      values[2] = activityLeaderData.get("email");
      values[3] = activityLeaderData.get("is_official");
      result = databaseManager.saveData(query.toString(), values);
      if (result <= 0) {
        LOG.error("A problem happened trying to add a new activity Leader with id={}");
        return -1;
      }
    } else {
      // update activity record
      query.append("UPDATE expected_activity_leaders SET institution_id = ?, name = ?, email = ? , is_official=? ");
      query.append("WHERE id = ? ");
      values = new Object[5];
      values[0] = activityLeaderData.get("institution_id");
      values[1] = activityLeaderData.get("name");
      values[2] = activityLeaderData.get("email");
      values[3] = activityLeaderData.get("is_official");
      values[4] = activityLeaderData.get("id");
      result = databaseManager.saveData(query.toString(), values);
      if (result == -1) {
        LOG.error("A problem happened trying to update the activity leader with the id = {}",
          activityLeaderData.get("id"));
        return -1;
      }
    }
    LOG.debug("<< saveExpectedActivityLeader():{}", result);
    return result;
  }
}
