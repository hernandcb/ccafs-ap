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

package org.cgiar.ccafs.ap.action.reporting.activities.deliverables;

import org.cgiar.ccafs.ap.action.BaseAction;
import org.cgiar.ccafs.ap.config.APConfig;
import org.cgiar.ccafs.ap.config.APConstants;
import org.cgiar.ccafs.ap.data.manager.DeliverableManager;
import org.cgiar.ccafs.ap.data.manager.DeliverableStatusManager;
import org.cgiar.ccafs.ap.data.manager.DeliverableTrafficLightManager;
import org.cgiar.ccafs.ap.data.manager.DeliverableTypeManager;
import org.cgiar.ccafs.ap.data.manager.LogframeManager;
import org.cgiar.ccafs.ap.data.model.Deliverable;
import org.cgiar.ccafs.ap.data.model.DeliverableStatus;
import org.cgiar.ccafs.ap.data.model.DeliverableType;

import java.util.LinkedHashMap;
import java.util.Map;

import com.google.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Hernán David Carvajal
 */

public class DeliverableInformationReportingAction extends BaseAction {

  // Logger
  public static Logger LOG = LoggerFactory.getLogger(DeliverableInformationReportingAction.class);
  private static final long serialVersionUID = 6180698449849134459L;

  // Managers
  DeliverableManager deliverableManager;
  DeliverableTypeManager deliverableTypeManager;
  DeliverableStatusManager deliverableStatusManager;
  DeliverableTrafficLightManager trafficLightManager;

  // Model
  private Deliverable deliverable;
  private int deliverableID;
  private int activityID;
  private boolean canSubmit;
  private DeliverableType[] deliverableTypes;
  private DeliverableType[] deliverableSubTypes;
  private DeliverableStatus[] deliverableStatusList;
  private Map<Boolean, String> yesNoRadio;

  @Inject
  public DeliverableInformationReportingAction(APConfig config, LogframeManager logframeManager,
    DeliverableManager deliverableManager, DeliverableTypeManager deliverableTypeManager,
    DeliverableStatusManager deliverableStatusManager, DeliverableTrafficLightManager trafficLightManager) {
    super(config, logframeManager);
    this.deliverableManager = deliverableManager;
    this.deliverableTypeManager = deliverableTypeManager;
    this.deliverableStatusManager = deliverableStatusManager;
    this.trafficLightManager = trafficLightManager;
  }

  public int getActivityID() {
    return activityID;
  }

  public Deliverable getDeliverable() {
    return deliverable;
  }

  public int getDeliverableID() {
    return deliverableID;
  }

  public DeliverableStatus[] getDeliverableStatusList() {
    return deliverableStatusList;
  }

  public DeliverableType[] getDeliverableSubTypes() {
    return deliverableSubTypes;
  }

  public DeliverableType[] getDeliverableTypes() {
    return deliverableTypes;
  }

  public Map<Boolean, String> getYesNoRadio() {
    return yesNoRadio;
  }

  public boolean isCanSubmit() {
    return true;
  }

  @Override
  public void prepare() throws Exception {
    activityID = Integer.parseInt(StringUtils.trim(this.getRequest().getParameter(APConstants.ACTIVITY_REQUEST_ID)));
    deliverableID =
      Integer.parseInt(StringUtils.trim(this.getRequest().getParameter(APConstants.DELIVERABLE_REQUEST_ID)));

    // TODO - Create an interceptor that verifies if the deliverable belongs to the activity identified by the value
    // received as parameter
    deliverable = deliverableManager.getDeliverable(deliverableID);
    deliverableTypes = deliverableTypeManager.getDeliverableTypes();
    deliverableSubTypes = deliverableTypeManager.getDeliverableSubTypes();
    deliverableStatusList = deliverableStatusManager.getDeliverableStatus();
    deliverable.setTrafficLight(trafficLightManager.getDeliverableTrafficLight(deliverableID));

    // Create options for the yes/no radio buttons
    yesNoRadio = new LinkedHashMap<>();
    yesNoRadio.put(true, getText("reporting.activityDeliverables.yes"));
    yesNoRadio.put(false, getText("reporting.activityDeliverables.no"));

  }

  @Override
  public String save() {
    deliverableManager.addDeliverable(deliverable, activityID);
    trafficLightManager.saveDeliverableTrafficLight(deliverable.getTrafficLight(), deliverable.getId());
    System.out.println("--------- Guardando ----------------");
    return super.save();
  }

  public void setActivityID(int activityID) {
    this.activityID = activityID;
  }

  public void setDeliverable(Deliverable deliverable) {
    this.deliverable = deliverable;
  }

  public void setYesNoRadio(Map<Boolean, String> yesNoRadio) {
    this.yesNoRadio = yesNoRadio;
  }
}