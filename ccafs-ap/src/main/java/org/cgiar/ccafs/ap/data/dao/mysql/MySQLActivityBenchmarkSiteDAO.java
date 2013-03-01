package org.cgiar.ccafs.ap.data.dao.mysql;

import org.cgiar.ccafs.ap.data.dao.ActivityBenchmarkSiteDAO;
import org.cgiar.ccafs.ap.data.dao.DAOManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.inject.Inject;


public class MySQLActivityBenchmarkSiteDAO implements ActivityBenchmarkSiteDAO {

  private DAOManager databaseManager;

  @Inject
  public MySQLActivityBenchmarkSiteDAO(DAOManager databaseManager) {
    this.databaseManager = databaseManager;
  }

  @Override
  public List<Map<String, String>> getActivityBenchmarkSites(int activityID) {
    List<Map<String, String>> bsDataList = new ArrayList<>();
    String query =
      "SELECT bsl.id, bs.bs_id, bs.name, bs.longitude, bs.latitude, bsl.details, "
        + "co.iso2 as 'country_iso2', co.name as 'country_name' FROM benchmark_sites bs "
        + "INNER JOIN bs_locations bsl ON bs.id = bsl.bs_id " + "INNER JOIN countries co ON bs.country_iso2 = co.iso2 "
        + "INNER JOIN activities ac ON bsl.activity_id = ac.id " + "WHERE ac.id = " + activityID;
    try (Connection con = databaseManager.getConnection()) {
      ResultSet rs = databaseManager.makeQuery(query, con);
      while (rs.next()) {
        Map<String, String> bsData = new HashMap<String, String>();
        bsData.put("id", rs.getString("id"));
        bsData.put("bs_id", rs.getString("bs_id"));
        bsData.put("name", rs.getString("name"));
        bsData.put("longitude", rs.getString("longitude"));
        bsData.put("latitude", rs.getString("latitude"));
        bsData.put("country_iso2", rs.getString("country_iso2"));
        bsData.put("country_name", rs.getString("country_name"));
        bsData.put("details", rs.getString("details"));
        bsDataList.add(bsData);
      }
      rs.close();
    } catch (SQLException e) {
      // TODO: handle exception
      e.printStackTrace();
    }
    return bsDataList;
  }

}
