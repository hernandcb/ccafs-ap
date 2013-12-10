package org.cgiar.ccafs.ap.data.dao;

import org.cgiar.ccafs.ap.data.dao.mysql.MySQLTLOutputSummaryDAO;

import java.util.List;
import java.util.Map;

import com.google.inject.ImplementedBy;

@ImplementedBy(MySQLTLOutputSummaryDAO.class)
public interface TLOutputSummaryDAO {

  /**
   * Get a list of Summaries by Output that belong to a specific leader and a specific logframe.
   * 
   * @param leader - Leader identifier.
   * @param logframe - Logframe identifier.
   * @return a List of Maps with all the summary by outputs information.
   */
  public List<Map<String, Object>> getTLOutputSummaries(int leader_id, int logframe_id);

  /**
   * Save a list of Summaries by Output.
   * 
   * @param outputs - List of Maps with the summaries by output information.
   * @return true if all the outputs were successfully saved. False if any problem appear.
   */
  public boolean saveTLOutputSummaries(List<Map<String, Object>> outputs);
}
