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
package org.cgiar.ccafs.ap.action.preplanning;

import org.cgiar.ccafs.ap.action.BaseAction;
import org.cgiar.ccafs.ap.config.APConfig;
import org.cgiar.ccafs.ap.config.APConstants;
import org.cgiar.ccafs.ap.data.manager.IPElementManager;
import org.cgiar.ccafs.ap.data.manager.IPElementRelationManager;
import org.cgiar.ccafs.ap.data.manager.IPIndicatorManager;
import org.cgiar.ccafs.ap.data.model.IPElement;
import org.cgiar.ccafs.ap.data.model.IPElementType;
import org.cgiar.ccafs.ap.data.model.IPProgram;
import org.cgiar.ccafs.ap.validation.preplanning.OutcomesValidation;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OutcomesPreplanningAction extends BaseAction {

  // Logger
  private static Logger LOG = LoggerFactory.getLogger(OutcomesPreplanningAction.class);
  private static final long serialVersionUID = -4683268058827119069L;

  // Managers
  private IPElementManager ipElementManager;
  private IPIndicatorManager ipIndicatorManager;
  private IPElementRelationManager ipElementRelationManager;

  // Model
  private List<IPElement> idos;
  private List<IPElement> outcomes;
  private StringBuilder validationMessages;
  private List<IPElement> outcomesFromDatabase;

  // Validator
  private OutcomesValidation validator;

  @Inject
  public OutcomesPreplanningAction(APConfig config, IPElementManager ipElementManager, OutcomesValidation validator,
    IPIndicatorManager ipIndicatorManager, IPElementRelationManager ipElementRelationManager) {
    super(config);
    this.ipElementManager = ipElementManager;
    this.ipIndicatorManager = ipIndicatorManager;
    this.validator = validator;
    this.ipElementRelationManager = ipElementRelationManager;
  }

  @Override
  public String execute() throws Exception {
    return super.execute();
  }

  public int getElementTypeID() {
    return APConstants.ELEMENT_TYPE_OUTCOME2025;
  }

  public List<IPElement> getIdos() {
    return idos;
  }

  public List<IPElement> getOutcomes() {
    return outcomes;
  }

  @Override
  public void prepare() throws Exception {
    IPElementType type = new IPElementType(APConstants.ELEMENT_TYPE_OUTCOME2025);
    validationMessages = new StringBuilder(); // TODO HC - This variable is never used.

    // The IDOs are created by the coordinating unit
    IPProgram cuProgram = new IPProgram();
    cuProgram.setId(APConstants.COORDINATING_UNIT_PROGRAM);
    // Set the element type for IDOs
    IPElementType idoType = new IPElementType();
    idoType.setId(APConstants.ELEMENT_TYPE_IDOS);
    // Get all the IDOs
    idos = ipElementManager.getIPElements(cuProgram, idoType);

    // TODO HC - Add an interceptor to verify that if the user is not related to a program, then DON'T have
    // permissions to access this action
    outcomes = ipElementManager.getIPElements(getCurrentUser().getCurrentInstitution().getProgram(), type);

    // Keep the id of all outcomes which come from the database
    outcomesFromDatabase = new ArrayList<>();
    outcomesFromDatabase.addAll(outcomes);

    if (getRequest().getMethod().equalsIgnoreCase("post")) {
      // Clear out the list if it has some element
      if (outcomes != null) {
        for (IPElement outcome : outcomes) {
          outcome.getIndicators().clear();
          outcome.getContributesTo().clear();
        }
      }
    }
  }

  @Override
  public String save() {

    for (int i = 0; i < outcomesFromDatabase.size(); i++) {
      IPElement outcome = outcomesFromDatabase.get(i);
      // If all the outcomes were removed, we should remove all the records
      // brought from the database
      if (outcomes.isEmpty()) {
        ipElementManager.deleteIPElement(outcome, getCurrentUser().getCurrentInstitution().getProgram());
        continue;
      }

      // Check if the user delete a midOutcome in the interface
      if (!outcomes.contains(outcome)) {
        ipElementManager.deleteIPElement(outcome, getCurrentUser().getCurrentInstitution().getProgram());
      } else {
        // Remove the relations and indicators of the midOutcome
        ipIndicatorManager.removeElementIndicators(outcome, getCurrentUser().getCurrentInstitution().getProgram());
        ipElementRelationManager.deleteRelationsByChildElement(outcome);
      }
    }

    if (ipElementManager.saveIPElements(outcomes)) {
      return SUCCESS;
    } else {
      return INPUT;
    }
  }

  public void setOutcomes(List<IPElement> outcomes) {
    this.outcomes = outcomes;
  }

  public String validateForm() {
    String messages = validator.validateForm(outcomes);

    if (messages.isEmpty()) {
      addActionMessage(getText("validation.success"));
    } else {
      String validationResult = getText("validation.fail") + messages;
      addActionWarning(validationResult);
    }
    return SUCCESS;
  }
}
