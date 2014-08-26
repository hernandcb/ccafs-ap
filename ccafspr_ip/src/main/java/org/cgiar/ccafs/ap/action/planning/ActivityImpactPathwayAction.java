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
package org.cgiar.ccafs.ap.action.planning;

import org.cgiar.ccafs.ap.action.BaseAction;
import org.cgiar.ccafs.ap.config.APConfig;
import org.cgiar.ccafs.ap.config.APConstants;
import org.cgiar.ccafs.ap.data.manager.ActivityManager;
import org.cgiar.ccafs.ap.data.manager.IPElementManager;
import org.cgiar.ccafs.ap.data.manager.IPProgramManager;
import org.cgiar.ccafs.ap.data.manager.ProjectManager;
import org.cgiar.ccafs.ap.data.model.Activity;
import org.cgiar.ccafs.ap.data.model.IPElement;
import org.cgiar.ccafs.ap.data.model.IPElementType;
import org.cgiar.ccafs.ap.data.model.IPProgram;
import org.cgiar.ccafs.ap.data.model.Project;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.google.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Javier Andrés Galllego B.
 */
public class ActivityImpactPathwayAction extends BaseAction {

  private static final long serialVersionUID = -5073068363009363496L;

  // LOG
  private static Logger LOG = LoggerFactory.getLogger(ActivityImpactPathwayAction.class);

  // Manager
  private ActivityManager activityManager;
  private IPProgramManager programManager;
  private IPElementManager ipElementManager;
  private ProjectManager projectManager;

  // Model
  private List<IPElement> midOutcomes;
  private List<IPElement> mogs;
  private List<IPProgram> projectFocusList;
  private Activity activity;

  private int activityID;

  @Inject
  public ActivityImpactPathwayAction(APConfig config, IPProgramManager programManager,
    IPElementManager ipElementManager, ProjectManager projectManager, ActivityManager activityManager) {
    super(config);
    this.programManager = programManager;
    this.ipElementManager = ipElementManager;
    this.projectManager = projectManager;
    this.activityManager = activityManager;
  }

  public Activity getActivity() {
    return activity;
  }

  public int getActivityID() {
    return activityID;
  }

  /**
   * In order to get the indicators and the mogs associated to
   * each midOutcome, we need the midOutcome identifier and the
   * identifier of the program corresponding to the midOutcome.
   * To send both values, we are going to send a composed key in
   * the map:
   * < "midOutcome.id-midoutcome.program.id", midoutcome.description >
   * 
   * @return
   */
  public Map<String, String> getMidOutcomes() {
    Map<String, String> midOutcomesMap = new TreeMap<>();

    for (IPElement midOutcome : midOutcomes) {

      // The first value of the list is the placeHolder,
      // therefore we should be omit the key concatenation
      if (midOutcome.getId() == -1) {
        midOutcomesMap.put(String.valueOf(midOutcome.getId()), midOutcome.getDescription());
        continue;
      }

      String id = String.valueOf(midOutcome.getId()) + "-" + String.valueOf(midOutcome.getProgram().getId());
      midOutcomesMap.put(id, midOutcome.getDescription());
    }

    return midOutcomesMap;
  }

  public List<IPElement> getMogs() {
    return mogs;
  }

  public List<IPProgram> getProjectFocusList() {
    return projectFocusList;
  }

  @Override
  public void prepare() throws Exception {
    super.prepare();
    try {
      activityID = Integer.parseInt(StringUtils.trim(this.getRequest().getParameter(APConstants.ACTIVITY_REQUEST_ID)));
    } catch (NumberFormatException e) {
      LOG.error("-- prepare() > There was an error parsing the activity identifier '{}'.", activityID, e);
      return; // Stop here and go to execute method.
    }

    activity = activityManager.getActivityById(activityID);

    // First, get the project to which the activity belongs to
    Project project = projectManager.getProjectFromActivityId(activityID);

    // Get the programs to which the project contributes
    projectFocusList = new ArrayList<>();
    projectFocusList.addAll(programManager.getProjectFocuses(project.getId(), APConstants.FLAGSHIP_PROGRAM_TYPE));
    projectFocusList.addAll(programManager.getProjectFocuses(project.getId(), APConstants.REGION_PROGRAM_TYPE));

    // Then, we have to get all the midOutcomes that belongs to the project focuses
    midOutcomes = new ArrayList<>();
    IPElement placeHolder = new IPElement(-1);
    placeHolder.setDescription(getText("planning.activityImpactPathways.outcome.placeholder"));
    midOutcomes.add(placeHolder);

    IPElementType midOutcomeType = new IPElementType(APConstants.ELEMENT_TYPE_OUTCOME2019);
    for (IPProgram program : projectFocusList) {
      midOutcomes.addAll(ipElementManager.getIPElements(program, midOutcomeType));
    }

    if (getRequest().getMethod().equalsIgnoreCase("post")) {
      // Clear out the list if it has some element
      if (activity.getIndicators() != null) {
        activity.getIndicators().clear();
      }

      if (activity.getOutputs() != null) {
        activity.getOutputs().clear();
      }
    }

  }

  @Override
  public String save() {
    System.out.println("Indicators: ");
    System.out.println(activity.getIndicators());
    System.out.println("Outptus: ");
    System.out.println(activity.getOutputs());
    return SUCCESS;
  }

  public void setActivity(Activity activity) {
    this.activity = activity;
  }

  public void setActivityID(int activityID) {
    this.activityID = activityID;
  }

}
