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
package org.cgiar.ccafs.ap.data.model;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * This class represents a Project.
 * 
 * @author Javier Andrés Gallego
 * @author Héctor Tobón
 */
public class Project {

  private int id;
  private String title;
  private String summary;
  private Date startDate;
  private Date endDate;
  private List<IPProgram> regions; // The list of regions in which this project works with.
  private List<IPProgram> flagships; // The list of flagships in which this project works with.
  private User leader; // Project leader will be a user too.
  private String leaderResponsabilities;
  private User owner;
  private List<ProjectPartner> projectPartners; // Project partners.


  public Project() {
    super();
  }

  public Date getEndDate() {
    return endDate;
  }

  public List<IPProgram> getFlagships() {
    return flagships;
  }

  public int getId() {
    return id;
  }

  public User getLeader() {
    return leader;
  }

  public String getLeaderResponsabilities() {
    return leaderResponsabilities;
  }

  public User getOwner() {
    return owner;
  }

  public List<ProjectPartner> getProjectPartners() {
    return projectPartners;
  }

  public List<IPProgram> getRegions() {
    return regions;
  }

  public Date getStartDate() {
    return startDate;
  }

  public String getSummary() {
    return summary;
  }

  public String getTitle() {
    return title;
  }

  public List<IPProgram> getTypes() {
    return regions;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  public void setFlagships(List<IPProgram> flagships) {
    this.flagships = flagships;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setLeader(User leader) {
    this.leader = leader;
  }

  public void setLeaderResponsabilities(String leaderResponsabilities) {
    this.leaderResponsabilities = leaderResponsabilities;
  }

  public void setOwner(User owner) {
    this.owner = owner;
  }

  public void setProjectPartners(List<ProjectPartner> projectPartners) {
    this.projectPartners = projectPartners;
  }

  public void setRegions(List<IPProgram> regions) {
    this.regions = regions;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  public void setSummary(String summary) {
    this.summary = summary;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setTypes(List<IPProgram> types) {
    this.regions = types;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}