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
package org.cgiar.ccafs.ap.config;

/**
 * All Constants should be here.
 * 
 * @author Héctor Fabio Tobón R.
 */
public final class APConstants {

  public static final String SESSION_USER = "current_user";
  public static final String ACTIVITY_REQUEST_ID = "activityID";
  public static final String PUBLIC_ACTIVITY_ID = "id";
  public static final String ACTIVITY_YEAR_REQUEST = "year";
  public static final String ACTIVITY_LIMIT_REQUEST = "limit";
  public static final String MILESTONE_REQUEST_ID = "milestoneID";
  public static final String REGION_REQUEST_ID = "regionID";
  public static final String COUNTRY_REQUEST_ID = "countryID";
  public static final String PARTNER_TYPE_REQUEST_ID = "partnerTypeID";
  public static final String PLANNING_SECTION = "Planning";
  public static final String REPORTING_SECTION = "Reporting";
  public static final String PROGRAM_REQUEST_ID = "programID";
  public static final String IP_ELEMENT_TYPE_REQUEST_ID = "elementTypeId";
  public static final String IP_ELEMENT_REQUEST_ID = "elementID";
  public static final String PROJECT_REQUEST_ID = "projectID";

  // Identifiers for element types which come from the database
  public static final int ELEMENT_TYPE_IDOS = 1;
  public static final int ELEMENT_TYPE_OUTCOME2025 = 2;
  public static final int ELEMENT_TYPE_OUTCOME2019 = 3;
  public static final int ELEMENT_TYPE_OUTPUTS = 4;

  // Identifiers for programs types which come from the database
  public static final int FLAGSHIP_PROGRAM_TYPE = 1;
  public static final int REGION_PROGRAM_TYPE = 2;

  // Identifier for types of program element relations
  public static final int PROGRAM_ELEMENT_CREATED_BY = 1;
  public static final int PROGRAM_ELEMENT_USED_BY = 2;

  // Identifiers for types of ip elements relationships
  public static final int ELEMENT_RELATION_CONTRIBUTION = 1;
  public static final int ELEMENT_RELATION_TRANSLATION = 2;

  // Identifier for types of relations between programs and elements
  public static final int PROGRAM_ELEMENT_RELATION_CREATION = 1;
  public static final int PROGRAM_ELEMENT_RELATION_USE = 2;

  // Identifiers for Location Elements Type which come from the database
  public static final int LOCATION_ELEMENT_TYPE_REGION = 1;
  public static final int LOCATION_ELEMENT_TYPE_COUNTRY = 2;

  // Identifier for Format Date
  public static final String DATE_FORMAT = "yyyy-MM-dd";

}
