/*
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
 */

package org.cgiar.ccafs.ap.data.manager.impl;

import org.cgiar.ccafs.ap.data.dao.ActivityKeywordDAO;
import org.cgiar.ccafs.ap.data.manager.ActivityKeywordManager;
import org.cgiar.ccafs.ap.data.model.ActivityKeyword;
import org.cgiar.ccafs.ap.data.model.Keyword;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ActivityKeywordManagerImpl implements ActivityKeywordManager {

  // Logger
  private static final Logger LOG = LoggerFactory.getLogger(ActivityKeywordManagerImpl.class);
  private ActivityKeywordDAO keywordDAO;

  @Inject
  public ActivityKeywordManagerImpl(ActivityKeywordDAO keywordDAO) {
    this.keywordDAO = keywordDAO;
  }

  @Override
  public List<ActivityKeyword> getKeywordList(int activityID) {
    List<ActivityKeyword> activityKeywords = new ArrayList<>();
    List<Map<String, String>> activityKeywordDataList = keywordDAO.getKeywordList(activityID);
    for (Map<String, String> activityKeywordData : activityKeywordDataList) {
      ActivityKeyword activityKeywordTemp = new ActivityKeyword();
      activityKeywordTemp.setId(Integer.parseInt(activityKeywordData.get("id")));
      activityKeywordTemp.setOther(activityKeywordData.get("other"));

      if (activityKeywordData.get("keyword_id") != null && activityKeywordData.get("keyword_name") != null) {
        Keyword keywordTemp = new Keyword();
        keywordTemp.setId(Integer.parseInt(activityKeywordData.get("keyword_id")));
        keywordTemp.setName(activityKeywordData.get("keyword_name"));
        activityKeywordTemp.setKeyword(keywordTemp);
      }

      activityKeywords.add(activityKeywordTemp);
    }
    return activityKeywords;
  }

  @Override
  public boolean removeActivityKeywords(int activityID) {
    return keywordDAO.removeActivityKeywords(activityID);
  }

  @Override
  public boolean saveKeywordList(List<ActivityKeyword> keywords, int activityID) {
    boolean saved = true;

    for (ActivityKeyword ak : keywords) {
      Map<String, String> keywordData = new HashMap<String, String>();

      keywordData.put("activity_id", String.valueOf(activityID));

      if (ak.getId() == -1) {
        keywordData.put("id", null);
      } else {
        keywordData.put("id", String.valueOf(ak.getId()));
      }

      if (ak.getKeyword() != null) {
        if (ak.getKeyword().getId() != -1) {
          keywordData.put("keyword_id", String.valueOf(ak.getKeyword().getId()));
        } else {
          keywordData.put("keyword_id", null);
        }
      } else {
        keywordData.put("keyword_id", null);
      }

      keywordData.put("other", ak.getOther());

      if (!keywordDAO.saveKeyword(keywordData)) {
        saved = false;
      }
    }
    return saved;
  }
}
