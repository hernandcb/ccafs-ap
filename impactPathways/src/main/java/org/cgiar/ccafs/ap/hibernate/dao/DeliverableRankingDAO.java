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


package org.cgiar.ccafs.ap.hibernate.dao;

import org.cgiar.ccafs.ap.hibernate.model.DeliverablesRanking;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;

public class DeliverableRankingDAO extends StandardDao {

  public DeliverablesRanking findDeliverableRanking(int deliverableId) {
    List<DeliverablesRanking> listRanking = new ArrayList<>();

    try {
      this.getSession();
      this.InitTransaction();
      this.CommitTransaction();
      Query query = this.getSession()
        .createQuery("from " + DeliverablesRanking.class.getName() + " where deliverable_id=" + deliverableId);
      listRanking.addAll(query.list());


      if (listRanking.size() > 0) {
        return listRanking.get(0);
      }
      return null;
    } catch (HibernateException e) {
      this.RollBackTransaction();
    } finally

    {
      this.closeSession();
    }
    return null;
  }


  public int save(DeliverablesRanking ranking) {
    this.saveOrUpdate(ranking);
    return ranking.getId();
  }
}
