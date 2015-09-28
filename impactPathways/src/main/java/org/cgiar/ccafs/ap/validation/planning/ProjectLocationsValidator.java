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

package org.cgiar.ccafs.ap.validation.planning;

import org.cgiar.ccafs.ap.action.BaseAction;
import org.cgiar.ccafs.ap.data.model.Location;
import org.cgiar.ccafs.ap.data.model.Project;
import org.cgiar.ccafs.ap.validation.BaseValidator;
import org.cgiar.ccafs.ap.validation.model.ProjectValidator;

import com.google.inject.Inject;


/**
 * @author Carlos Alberto Martínez. - CIAT/CCAFS
 */

public class ProjectLocationsValidator extends BaseValidator {

  private static final long serialVersionUID = -4871185832403702671L;
  private ProjectValidator projectValidator;
  boolean problem = false;
  boolean fields = false;

  @Inject
  public ProjectLocationsValidator(ProjectValidator projectValidator) {
    super();
    this.projectValidator = projectValidator;
  }

  public boolean isValidLocation(Location location) {
    boolean result = true;
    if (location.getName().isEmpty()) {
      result = false;
    }
    return result;
  }


  public void validate(BaseAction action, Project project) {
    if (project != null) {

      this.validateProjectJustification(action, project);
      // Projects are validated checking if they are not global and their locations are valid ones.
      if ((!project.isGlobal()) && (!projectValidator.isValidListLocations(project.getLocations()))) {
        problem = true;
      }
      if (!project.isGlobal()) {
        this.validateLocations(action, project);
      }

      if (problem) {
        action.addActionError(action.getText("planning.projectLocations.type"));
      }
      if (fields) {
        action.addActionError(action.getText("saving.fields.required"));
      }
    }
  }

  public void validateLocations(BaseAction action, Project project) {
    for (int i = 0; i < project.getLocations().size(); i++) {
      if (!this.isValidLocation(project.getLocations().get(i))) {
        fields = true;
      }
    }
  }
}
