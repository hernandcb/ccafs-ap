package org.cgiar.ccafs.ap.data.manager.impl;

import org.cgiar.ccafs.ap.config.APConstants;
import org.cgiar.ccafs.ap.data.dao.IPElementDAO;
import org.cgiar.ccafs.ap.data.dao.IPIndicatorDAO;
import org.cgiar.ccafs.ap.data.dao.IPRelationshipDAO;
import org.cgiar.ccafs.ap.data.manager.IPElementManager;
import org.cgiar.ccafs.ap.data.manager.IPIndicatorManager;
import org.cgiar.ccafs.ap.data.model.IPElement;
import org.cgiar.ccafs.ap.data.model.IPElementType;
import org.cgiar.ccafs.ap.data.model.IPIndicator;
import org.cgiar.ccafs.ap.data.model.IPProgram;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.inject.Inject;


public class IPElementManagerImpl implements IPElementManager {

  // DAOs
  private IPElementDAO ipElementDAO;
  private IPIndicatorDAO ipIndicatorDAO;
  private IPRelationshipDAO ipRelationshipDAO;
  private IPIndicatorManager ipIndicatorManager;

  @Inject
  public IPElementManagerImpl(IPElementDAO ipElementDAO, IPIndicatorDAO ipIndicatorDAO,
    IPRelationshipDAO ipRelationshipDAO, IPIndicatorManager ipIndicatorManager) {
    this.ipElementDAO = ipElementDAO;
    this.ipIndicatorDAO = ipIndicatorDAO;
    this.ipRelationshipDAO = ipRelationshipDAO;
    this.ipIndicatorManager = ipIndicatorManager;
  }

  @Override
  public boolean deleteIPElement(IPElement element, IPProgram program) {
    boolean deleted = false;
    boolean isElementCreator = (element.getProgram().getId() == program.getId());

    if (isElementCreator) {
      // If the user is the creator, removes directly the ipElement.
      // if the element has children, the foreign key will prohibit the
      // deletion
      deleted = ipElementDAO.deleteIPElement(element.getId());
    } else {
      // If the user is not the creator, then remove only the relation
      // between the element and the program
      deleted = ipElementDAO.deleteProgramElement(element.getId());
    }

    return deleted;
  }

  @Override
  public boolean deleteIPElements(IPProgram program, IPElementType type) {
    return ipElementDAO.deleteIpElements(program.getId(), type.getId());
  }

  @Override
  public List<IPElement> getIPElementList() {
    List<Map<String, String>> elementDataList = ipElementDAO.getIPElementList();

    return setDataToIPElementObjects(elementDataList);
  }

  @Override
  public List<IPElement> getIPElementList(String[] ids) {
    List<IPElement> elements = new ArrayList<>();
    for (IPElement element : getIPElementList()) {
      for (String id : ids) {
        if (String.valueOf(element.getId()).equals(id)) {
          elements.add(element);
        }
      }
    }
    return elements;
  }

  @Override
  public List<IPElement> getIPElements(IPProgram program) {
    List<Map<String, String>> ipElementDataList = ipElementDAO.getIPElement(program.getId());
    return setDataToIPElementObjects(ipElementDataList);
  }

  @Override
  public List<IPElement> getIPElements(IPProgram program, IPElementType type) {
    List<Map<String, String>> ipElementDataList = ipElementDAO.getIPElement(program.getId(), type.getId());
    return setDataToIPElementObjects(ipElementDataList);
  }

  @Override
  public boolean saveIPElements(List<IPElement> elements) {
    Map<String, Object> ipElementData;
    boolean allSaved = true;
    int elementId, programElementID;

    for (IPElement element : elements) {
      ipElementData = new HashMap<String, Object>();

      if (element.getId() == -1) {
        ipElementData.put("id", null);
      } else {
        ipElementData.put("id", element.getId());
      }

      ipElementData.put("description", element.getDescription());
      ipElementData.put("creator_id", element.getProgram().getId());
      ipElementData.put("element_type_id", element.getType().getId());
      elementId = ipElementDAO.createIPElement(ipElementData);

      // If the ip_element was updated, createIPElement method returns 0, update the value
      elementId = (elementId == 0) ? element.getId() : elementId;
      // Add the relation of type creation
      programElementID =
        ipElementDAO.relateIPElement(elementId, element.getProgram().getId(), APConstants.PROGRAM_ELEMENT_CREATED_BY);

      // Add the connection of type 'Used by'
      programElementID =
        ipElementDAO.relateIPElement(elementId, element.getProgram().getId(), APConstants.PROGRAM_ELEMENT_USED_BY);

      if (programElementID == 0) {
        programElementID = ipElementDAO.getProgramElementID(elementId, element.getProgram().getId());
      }

      // If the result is 0 the element was updated and keep the same id
      elementId = (elementId == 0) ? element.getId() : elementId;

      if (elementId != -1) {
        // Save the indicators of the element if any
        if (element.getIndicators() != null) {
          for (IPIndicator indicator : element.getIndicators()) {
            Map<String, Object> indicatorData = new HashMap<>();
            if (indicator.getId() == -1) {
              indicatorData.put("id", null);
            } else {
              indicatorData.put("id", indicator.getId());
            }

            indicatorData.put("description", indicator.getDescription());
            indicatorData.put("target", indicator.getTarget());
            indicatorData.put("program_element_id", programElementID);

            if (indicator.getParent() != null) {
              indicatorData.put("parent_indicator_id", indicator.getParent().getId());
            } else {
              indicatorData.put("parent_indicator_id", null);
            }
            ipIndicatorDAO.saveIndicator(indicatorData);
          }
        }

        // Save the relations of type contribution added if any
        if (element.getContributesTo() != null) {
          for (IPElement parentElement : element.getContributesTo()) {
            ipRelationshipDAO.saveIPRelation(parentElement.getId(), elementId,
              APConstants.ELEMENT_RELATION_CONTRIBUTION);
          }
        }

        // Save the relations of type translation if any
        if (element.getTranslatedOf() != null) {
          for (IPElement parentElement : element.getTranslatedOf()) {
            ipRelationshipDAO
              .saveIPRelation(parentElement.getId(), elementId, APConstants.ELEMENT_RELATION_TRANSLATION);
          }
        }
      } else {
        // All elements should be saved correctly
        allSaved = (elementId != -1) && allSaved;
      }

    }

    return allSaved;
  }

  /**
   * This function takes the information of IPElements stored in a list of maps
   * to organize it in a list of IPElement objects
   * 
   * @param ipElementDataList
   * @return
   */
  private List<IPElement> setDataToIPElementObjects(List<Map<String, String>> ipElementDataList) {
    List<IPElement> elementsList = new ArrayList<>();

    for (Map<String, String> elementData : ipElementDataList) {
      IPElement element = new IPElement();
      element.setId(Integer.parseInt(elementData.get("id")));
      element.setDescription(elementData.get("description"));

      // Create an object program
      IPProgram programTemp = new IPProgram();
      programTemp.setId(Integer.parseInt(elementData.get("program_id")));
      programTemp.setAcronym(elementData.get("program_acronym"));
      element.setProgram(programTemp);

      // Create an object IPElementType
      IPElementType type = new IPElementType();
      type.setId(Integer.parseInt(elementData.get("element_type_id")));
      type.setName(elementData.get("element_type_name"));
      element.setType(type);

      // Set element indicators if exists
      List<IPIndicator> indicators = new ArrayList<>();
      int programElementID = Integer.parseInt(elementData.get("program_element_id"));
      List<Map<String, String>> indicatorsData = ipIndicatorDAO.getIndicatorsByIpProgramElementID(programElementID);
      for (Map<String, String> indicatorData : indicatorsData) {
        IPIndicator indicator = new IPIndicator();
        indicator.setId(Integer.parseInt(indicatorData.get("id")));
        indicator.setDescription(indicatorData.get("description"));
        indicator.setTarget(indicatorData.get("target"));
        indicators.add(indicator);
      }
      element.setIndicators(indicators);

      // Set elements 'contributesTo' if exists
      List<IPElement> elementsRelated = new ArrayList<>();
      List<Map<String, String>> elementsData =
        ipElementDAO.getIPElementsRelated(element.getId(), APConstants.ELEMENT_RELATION_CONTRIBUTION);
      for (Map<String, String> parentData : elementsData) {
        IPElement parentElement = new IPElement();
        parentElement.setId(Integer.parseInt(parentData.get("id")));
        parentElement.setDescription(parentData.get("description"));
        elementsRelated.add(parentElement);
      }
      element.setContributesTo(elementsRelated);

      // Set elements 'translatedOf' if exists
      elementsRelated = new ArrayList<>();
      elementsData = ipElementDAO.getIPElementsRelated(element.getId(), APConstants.ELEMENT_RELATION_TRANSLATION);
      for (Map<String, String> parentData : elementsData) {
        IPElement parentElement = new IPElement();
        parentElement.setId(Integer.parseInt(parentData.get("id")));
        parentElement.setDescription(parentData.get("description"));

        // To get the indicators of the parent we need to know which program created it
        Map<String, String> programCreatorData = ipElementDAO.getElementCreator(parentElement.getId());
        IPProgram parentProgram = new IPProgram();
        parentProgram.setId(Integer.parseInt(programCreatorData.get("id")));
        parentProgram.setName(programCreatorData.get("name"));
        parentElement.setProgram(parentProgram);

        parentElement.setIndicators(ipIndicatorManager.getElementIndicators(parentElement));

        elementsRelated.add(parentElement);
      }
      element.setTranslatedOf(elementsRelated);


      elementsList.add(element);
    }

    return elementsList;
  }
}
