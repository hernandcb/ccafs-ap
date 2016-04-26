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


package org.cgiar.ccafs.ap.data.dao.mysqlhiberate;


import org.cgiar.ccafs.ap.data.dao.ProjectEvaluationDAO;
import org.cgiar.ccafs.ap.data.model.ProjectEvaluation;

import java.util.List;

import com.google.inject.Inject;

public class ProjectEvaluationMySQLDAO implements ProjectEvaluationDAO {


  private StandardDAO dao;

  @Inject
  public ProjectEvaluationMySQLDAO(StandardDAO dao) {
    this.dao = dao;
  }

  @Override
  public ProjectEvaluation getEvaluationProjectByUser(int projectId, int userId) {
    String sql =
      "from " + ProjectEvaluation.class.getName() + " where project_id=" + projectId + " and user_id=" + userId;
    List<ProjectEvaluation> list = dao.findAll(sql);
    if (list.size() > 0) {
      return list.get(0);
    }
    return null;
  }

  @Override
  public List<ProjectEvaluation> getEvaluationsProject(int projectId) {
    String sql = "from " + ProjectEvaluation.class.getName() + " where project_id=" + projectId;
    List<ProjectEvaluation> list = dao.findAll(sql);
    return list;
  }

  @Override
  public int save(ProjectEvaluation projectEvaluation) {
    dao.saveOrUpdate(projectEvaluation);
    return projectEvaluation.getId().intValue();
  }


}