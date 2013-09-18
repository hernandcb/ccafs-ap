package org.cgiar.ccafs.ap.action.planning;

import org.cgiar.ccafs.ap.action.BaseAction;
import org.cgiar.ccafs.ap.config.APConfig;
import org.cgiar.ccafs.ap.config.APConstants;
import org.cgiar.ccafs.ap.data.manager.ActivityManager;
import org.cgiar.ccafs.ap.data.manager.ActivityObjectiveManager;
import org.cgiar.ccafs.ap.data.manager.LogframeManager;
import org.cgiar.ccafs.ap.data.model.Activity;

import com.google.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ObjectivesPlanningAction extends BaseAction {

  // Logger
  private static final Logger LOG = LoggerFactory.getLogger(ObjectivesPlanningAction.class);
  private static final long serialVersionUID = -8764463530270764535L;

  // Managers
  private ActivityManager activityManager;
  private ActivityObjectiveManager activityObjectiveManager;

  // Model
  private int activityID;
  private Activity activity;
  private StringBuilder validationMessage;


  @Inject
  public ObjectivesPlanningAction(APConfig config, LogframeManager logframeManager, ActivityManager activityManager,
    ActivityObjectiveManager activityObjectiveManager) {
    super(config, logframeManager);
    this.activityManager = activityManager;
    this.activityObjectiveManager = activityObjectiveManager;
  }

  public Activity getActivity() {
    return activity;
  }

  public String getActivityRequestParameter() {
    return APConstants.ACTIVITY_REQUEST_ID;
  }

  @Override
  public void prepare() throws Exception {
    super.prepare();

    validationMessage = new StringBuilder();

    String activityStringID = StringUtils.trim(this.getRequest().getParameter(APConstants.ACTIVITY_REQUEST_ID));
    try {
      activityID = Integer.parseInt(activityStringID);
    } catch (NumberFormatException e) {
      LOG.error("-- prepare() > There was an error parsing the activity identifier '{}'.", activityStringID, e);
    }

    LOG.info("-- prepare() > User {} load the objectives for activity {} in planing section", getCurrentUser()
      .getEmail(), activityID);

    // Get the basic information about the activity
    activity = activityManager.getSimpleActivity(activityID);

    // Get the activity objectives
    activity.setObjectives(activityObjectiveManager.getActivityObjectives(activityID));

    if (getRequest().getMethod().equalsIgnoreCase("post")) {
      activity.getObjectives().clear();
    }
  }

  @Override
  public String save() {
    boolean saved = false;
    // First delete all the objectives from the DAO
    activityObjectiveManager.deleteActivityObjectives(activityID);

    // Save the new objectives
    saved = activityObjectiveManager.saveActivityObjectives(activity.getObjectives(), activityID);
    if (saved) {
      if (validationMessage.toString().isEmpty()) {
        addActionMessage(getText("saving.success", new String[] {getText("planning.objectives")}));
      } else {
        String finalMessage = getText("saving.success", new String[] {getText("planning.objectives")});
        finalMessage += " " + getText("savind.fields.however") + " ";
        finalMessage += validationMessage.toString();
        AddActionWarning(finalMessage);
      }

      LOG.info("-- save() > User {} save successfully the objectives for activity {}",
        this.getCurrentUser().getEmail(), activityID);
      return SUCCESS;
    } else {
      LOG.info("-- save() > User {} had problems to save the objectives for activity {}", this.getCurrentUser()
        .getEmail(), activityID);
      addActionError(getText("saving.problem"));
      return INPUT;
    }
  }

  public void setActivity(Activity activity) {
    this.activity = activity;
  }

  @Override
  public void validate() {
    if (save) {
      // Remove the empty objectives
      if (!activity.getObjectives().isEmpty()) {
        for (int c = 0; c < activity.getObjectives().size(); c++) {
          if (activity.getObjectives().get(c).getDescription().isEmpty()) {
            activity.getObjectives().remove(c);
            c--;
          }
        }
      } else {
        // Activity must have at least one objective
        if (activity.getObjectives().isEmpty()) {
          validationMessage.append(getText("planning.objectives.validation.atLeastOne"));
        }
      }
    }
  }

}
