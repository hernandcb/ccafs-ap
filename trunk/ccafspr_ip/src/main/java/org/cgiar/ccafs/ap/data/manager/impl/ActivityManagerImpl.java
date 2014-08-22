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
package org.cgiar.ccafs.ap.data.manager.impl;

import org.cgiar.ccafs.ap.config.APConstants;
import org.cgiar.ccafs.ap.config.APModule;
import org.cgiar.ccafs.ap.data.dao.ActivityDAO;
import org.cgiar.ccafs.ap.data.manager.ActivityManager;
import org.cgiar.ccafs.ap.data.manager.InstitutionManager;
import org.cgiar.ccafs.ap.data.manager.UserManager;
import org.cgiar.ccafs.ap.data.model.Activity;
import org.cgiar.ccafs.ap.data.model.ExpectedActivityLeader;
import org.cgiar.ccafs.ap.data.model.IPProgram;
import org.cgiar.ccafs.ap.data.model.Institution;
import org.cgiar.ccafs.ap.data.model.User;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Javier Andrés Gallego
 * @author Héctor Fabio Tobón R.
 */
public class ActivityManagerImpl implements ActivityManager {

  // LOG
  private static Logger LOG = LoggerFactory.getLogger(ActivityManagerImpl.class);

  // DAO's
  private ActivityDAO activityDAO;

  // Managers
  private InstitutionManager institutionManager;
  private UserManager userManager;

  @Inject
  public ActivityManagerImpl(ActivityDAO activityDAO, InstitutionManager institutionManager, UserManager userManager) {
    this.activityDAO = activityDAO;
    this.institutionManager = institutionManager;
    this.userManager = userManager;
  }

  public static void main(String[] args) {
    Injector in = Guice.createInjector(new APModule());
    ActivityManager activityManager = in.getInstance(ActivityManager.class);
    User user = new User();
    Institution currentInstitution = new Institution();
    currentInstitution.setId(1024);
    IPProgram program = new IPProgram();
    program.setId(1);
    currentInstitution.setProgram(program);
    user.setEmployeeId(23);
    user.setCurrentInstitution(currentInstitution);
    Activity activity = new Activity();
    activity.setTitle("Test11111");
    activity.setDescription("Test1111111");
    activity.setLeader(user);
    System.out.println(activityManager.saveActivityLeader(2009, user));
  }

  @Override
  public boolean deleteActivitiesByProject(int projectID) {
    return activityDAO.deleteActivitiesByProject(projectID);
  }

  @Override
  public boolean deleteActivity(int activityId) {
    return activityDAO.deleteActivity(activityId);
  }

  @Override
  public boolean existActivity(int activityID) {
    return activityDAO.existActivity(activityID);
  }

  @Override
  public List<Activity> getActivitiesByProject(int projectID) {
    DateFormat dateformatter = new SimpleDateFormat(APConstants.DATE_FORMAT);
    List<Activity> activityList = new ArrayList<>();
    List<Map<String, String>> activityDataList = activityDAO.getActivitiesByProject(projectID);
    for (Map<String, String> activityData : activityDataList) {
      Activity activity = new Activity();
      activity.setId(Integer.parseInt(activityData.get("id")));
      activity.setTitle(activityData.get("title"));
      activity.setDescription(activityData.get("description"));

      // Format the date of the activity
      if (activityData.get("startDate") != null) {
        try {
          Date startDate = dateformatter.parse(activityData.get("startDate"));
          activity.setStart(startDate);
        } catch (ParseException e) {
          LOG.error("There was an error formatting the start date", e);
        }
      }
      if (activityData.get("endDate") != null) {
        try {
          Date endDate = dateformatter.parse(activityData.get("endDate"));
          activity.setEnd(endDate);
        } catch (ParseException e) {
          LOG.error("There was an error formatting the end date", e);
        }
        if (activityData.get("expected_leader_id") != null) {
          activity.setExpectedLeader(this.getExpectedActivityLeader(Integer.parseInt(activityData.get("id"))));
        }
        if (activityData.get("leader_id") != null) {
          activity.setLeader(userManager.getOwner(Integer.parseInt(activityData.get("leader_id"))));
        }
        activity.setCreated(Long.parseLong(activityData.get("created")));

        // adding information of the object to the array
        activityList.add(activity);
      }
    }
    return activityList;
  }

  @Override
  public Activity getActivityById(int activityID) {
    DateFormat dateformatter = new SimpleDateFormat(APConstants.DATE_FORMAT);
    Map<String, String> activityData = activityDAO.getActivityById(activityID);
    if (!activityData.isEmpty()) {
      Activity activity = new Activity();
      activity.setId(Integer.parseInt(activityData.get("id")));
      activity.setTitle(activityData.get("title"));
      activity.setDescription(activityData.get("description"));

      // Format the date of the activity
      if (activityData.get("startDate") != null) {
        try {
          Date startDate = dateformatter.parse(activityData.get("startDate"));
          activity.setStart(startDate);
        } catch (ParseException e) {
          LOG.error("There was an error formatting the start date", e);
        }
      }
      if (activityData.get("endDate") != null) {
        try {
          Date endDate = dateformatter.parse(activityData.get("endDate"));
          activity.setEnd(endDate);
        } catch (ParseException e) {
          LOG.error("There was an error formatting the end date", e);
        }
      }
      if (activityData.get("expected_leader_id") != null) {
        activity.setExpectedLeader(this.getExpectedActivityLeader(activityID));
      }
      if (activityData.get("leader_id") != null) {
        activity.setLeader(userManager.getOwner(Integer.parseInt(activityData.get("leader_id"))));
      }
      activity.setCreated(Long.parseLong(activityData.get("created")));
      return activity;
    }
    return null;
  }

  @Override
  public List<Integer> getActivityIdsEditable(User user) {
    return activityDAO.getActivityIdsEditable(user.getCurrentInstitution().getProgram().getId());
  }

  @Override
  public User getActivityLeader(int activityID) {
    int activityLeaderId = activityDAO.getActivityLeaderId(activityID);
    if (activityLeaderId != -1) {
      User activityLeader = userManager.getOwner(activityLeaderId);
      return activityLeader;
    }
    return null;
  }

  @Override
  public ExpectedActivityLeader getExpectedActivityLeader(int activityID) {
    Map<String, String> expectedActivityLeaderData = activityDAO.getExpectedActivityLeader(activityID);
    if (!expectedActivityLeaderData.isEmpty()) {
      ExpectedActivityLeader expectedActivityLeader = new ExpectedActivityLeader();
      expectedActivityLeader.setId(Integer.parseInt(expectedActivityLeaderData.get("id")));
      expectedActivityLeader.setName(expectedActivityLeaderData.get("name"));
      expectedActivityLeader.setEmail(expectedActivityLeaderData.get("email"));
      if (expectedActivityLeaderData.get("institution_id") != null) {
        expectedActivityLeader.setInstitution(institutionManager.getInstitution(Integer
          .parseInt(expectedActivityLeaderData.get("institution_id"))));
      }
      return expectedActivityLeader;
    }
    return null;
  }

  @Override
  public int saveActivity(int projectID, Activity activity) {
    Map<String, Object> activityData = new HashMap<>();
    if (activity.getId() > 0) {
      activityData.put("id", activity.getId());
    }
    activityData.put("title", activity.getTitle());
    activityData.put("description", activity.getDescription());
    activityData.put("startDate", activity.getStart());
    activityData.put("endDate", activity.getEnd());
    activityData.put("leader_id", activity.getLeader().getEmployeeId());

    return activityDAO.saveActivity(projectID, activityData);
  }

  @Override
  public boolean saveActivityLeader(int activityID, User user) {
    boolean allSaved = true;

    int result = activityDAO.saveActivityLeader(activityID, user.getEmployeeId());

    if (result > 0) {
      LOG.debug("saveExpectedActivityLeader > New Activity added with id {}", result);
    } else if (result == 0) {
      LOG.debug("saveExpectedActivityLeader > Activity with id={} was updated", user.getEmployeeId());
    } else {
      LOG.error("saveExpectedActivityLeader > There was an error trying to save/update a Activity from projectId={}",
        activityID);
      allSaved = false;
    }

    return allSaved;
  }

  @Override
  public boolean saveExpectedActivityLeader(int activityID, ExpectedActivityLeader expectedActivityLeader) {
    boolean allSaved = true;
    Map<String, Object> activityData = new HashMap<>();
    if (expectedActivityLeader.getId() > 0) {
      activityData.put("id", expectedActivityLeader.getId());
    }
    activityData.put("institution_id", expectedActivityLeader.getInstitution().getId());
    activityData.put("name", expectedActivityLeader.getName());
    activityData.put("email", expectedActivityLeader.getName());

    int result = activityDAO.saveExpectedActivityLeader(activityID, activityData);

    if (result > 0) {
      LOG.debug("saveExpectedActivityLeader > New Activity added with id {}", result);
    } else if (result == 0) {
      LOG.debug("saveExpectedActivityLeader > Activity with id={} was updated", expectedActivityLeader.getId());
    } else {
      LOG.error("saveExpectedActivityLeader > There was an error trying to save/update a Activity from projectId={}",
        activityID);
      allSaved = false;
    }

    return allSaved;
  }
}
