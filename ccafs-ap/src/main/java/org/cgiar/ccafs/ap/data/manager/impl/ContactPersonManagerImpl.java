package org.cgiar.ccafs.ap.data.manager.impl;

import org.cgiar.ccafs.ap.data.dao.ContactPersonDAO;
import org.cgiar.ccafs.ap.data.manager.ContactPersonManager;
import org.cgiar.ccafs.ap.data.model.ContactPerson;

import java.util.List;
import java.util.Map;

import com.google.inject.Inject;


public class ContactPersonManagerImpl implements ContactPersonManager {

  private ContactPersonDAO contactPersonDAO;

  @Inject
  public ContactPersonManagerImpl(ContactPersonDAO contactPersonDAO) {
    this.contactPersonDAO = contactPersonDAO;
  }

  @Override
  public ContactPerson[] getContactPersons(int activityID) {
    List<Map<String, String>> contactPersonsDB = contactPersonDAO.getContactPersons(activityID);
    ContactPerson[] contactPersons = new ContactPerson[contactPersonsDB.size()];

    for (int c = 0; c < contactPersons.length; c++) {
      ContactPerson cp = new ContactPerson();
      cp.setId(Integer.parseInt(contactPersonsDB.get(c).get("id")));
      cp.setEmail(contactPersonsDB.get(c).get("email"));
      cp.setName(contactPersonsDB.get(c).get("name"));
      contactPersons[c] = cp;
    }

    if (contactPersonsDB.size() == 0) {
      return null;
    }
    return contactPersons;
  }

}
