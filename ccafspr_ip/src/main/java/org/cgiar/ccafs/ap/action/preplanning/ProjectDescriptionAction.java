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
import org.cgiar.ccafs.ap.data.manager.ProjectManager;
import org.cgiar.ccafs.ap.data.model.Project;
import com.google.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ProjectDescriptionAction extends BaseAction {


  private static final long serialVersionUID = 2845669913596494699L;

  // Manager
  private ProjectManager projectManager;

  private static Logger LOG = LoggerFactory.getLogger(ProjectDescriptionAction.class);

  // Model
  private Project project;
  private int projectId;

  @Inject
  public ProjectDescriptionAction(APConfig config, ProjectManager projectManager) {
    super(config);
    this.projectManager = projectManager;
  }


  @Override
  public String execute() throws Exception {
    /*
     * If there project Id is not in the parameter or if the is not a project with that id, we must redirect to a
     * NOT_FOUND page.
     */
    if (projectId == -1) {
      return NOT_FOUND;
    }
    return super.execute();
  }

  public Project getProject() {
    return project;
  }

  @Override
  public void prepare() throws Exception {
    super.prepare();

    try {
      projectId = Integer.parseInt(StringUtils.trim(this.getRequest().getParameter(APConstants.PROJECT_REQUEST_ID)));
    } catch (NumberFormatException e) {
      LOG.error("-- prepare() > There was an error parsing the project identifier '{}'.", projectId, e);
      projectId = -1;
      return; // Stop here and go to execute method.
    }

    // Getting project
    project = projectManager.getProject(projectId);
    System.out.println(project.getOwner());

    // System.out.println(projects);
  }
}
