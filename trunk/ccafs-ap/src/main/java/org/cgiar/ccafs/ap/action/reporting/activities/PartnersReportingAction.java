package org.cgiar.ccafs.ap.action.reporting.activities;

import org.cgiar.ccafs.ap.action.BaseAction;
import org.cgiar.ccafs.ap.config.APConfig;
import org.cgiar.ccafs.ap.config.APConstants;
import org.cgiar.ccafs.ap.data.manager.ActivityManager;
import org.cgiar.ccafs.ap.data.manager.ActivityPartnerManager;
import org.cgiar.ccafs.ap.data.manager.LogframeManager;
import org.cgiar.ccafs.ap.data.manager.PartnerManager;
import org.cgiar.ccafs.ap.data.manager.PartnerTypeManager;
import org.cgiar.ccafs.ap.data.model.Activity;
import org.cgiar.ccafs.ap.data.model.Partner;
import org.cgiar.ccafs.ap.data.model.PartnerType;

import com.google.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PartnersReportingAction extends BaseAction {

  private static final long serialVersionUID = 1380416261959252826L;

  // Logger
  private static final Logger LOG = LoggerFactory.getLogger(PartnersReportingAction.class);

  // Managers
  private ActivityManager activityManager;
  private ActivityPartnerManager activityPartnerManager;
  private PartnerManager partnerManager;
  private PartnerTypeManager partnerTypeManager;

  // Model
  private PartnerType[] partnerTypes;
  private Partner[] partners;
  private Activity activity;


  private int activityID;

  @Inject
  public PartnersReportingAction(APConfig config, LogframeManager logframeManager,
    ActivityPartnerManager activityPartnerManager, ActivityManager activityManager, PartnerManager partnerManager,
    PartnerTypeManager partnerTypeManager) {

    super(config, logframeManager);
    this.activityManager = activityManager;
    this.activityPartnerManager = activityPartnerManager;
    this.partnerManager = partnerManager;
    this.partnerTypeManager = partnerTypeManager;
  }

  public Activity getActivity() {
    return activity;
  }

  public String getActivityRequestParameter() {
    return APConstants.ACTIVITY_REQUEST_ID;
  }


  public Partner[] getPartners() {
    return partners;
  }


  public PartnerType[] getPartnerTypes() {
    return partnerTypes;
  }

  @Override
  public void prepare() throws Exception {
    super.prepare();
    activityID = Integer.parseInt(StringUtils.trim(this.getRequest().getParameter(APConstants.ACTIVITY_REQUEST_ID)));
    activity = activityManager.getSimpleActivity(activityID);
    activity.setActivityPartners(activityPartnerManager.getActivityPartners(activityID));
    partnerTypes = partnerTypeManager.getPartnerTypeList();
    partners = partnerManager.getAllPartners();
    // Remove all partners so they can be added again in the save method.
    if (this.getRequest().getMethod().equalsIgnoreCase("post")) {
      activity.getActivityPartners().clear();
    }
  }

  @Override
  public String save() {
    if (activity.getActivityPartners().size() > 0) {
      // Remove all activity partners from the database.
      boolean removed = activityPartnerManager.removeActivityPartners(activityID);
      if (removed) {
        boolean added = activityPartnerManager.saveActivityPartners(activity.getActivityPartners(), activityID);
        if (added) {
          return SUCCESS;
        }
      }
    }

    return INPUT;
  }

  public void setActivity(Activity activity) {
    this.activity = activity;
  }

  public void setPartnerTypes(PartnerType[] partnerTypes) {
    this.partnerTypes = partnerTypes;
  }

}
