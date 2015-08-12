[#ftl]
[#assign title = "Project Deliverables List" /]
[#assign globalLibs = ["jquery", "noty", "dataTable", "autoSave", "chosen"] /]
[#assign customCSS = ["${baseUrl}/css/libs/dataTables/jquery.dataTables-1.9.4.css", "${baseUrl}/css/global/customDataTable.css"] /]
[#assign customJS = ["${baseUrl}/js/global/utils.js", "${baseUrl}/js/planning/projectDeliverablesListPlanning.js"] /]
[#assign currentSection = "planning" /]
[#assign currentPlanningSection = "projects" /]
[#assign currentStage = "outputs" /]
[#assign currentSubStage = "deliverables" /]

[#assign breadCrumb = [
  {"label":"planning", "nameSpace":"planning", "action":"projectsList"},
  {"label":"projects", "nameSpace":"planning", "action":"projectsList"},
  {"label":"projectOutputs", "nameSpace":"planning/projects", "action":"outputs", "param":"projectID=${project.id}"},
  {"label":"projectDeliverables", "nameSpace":"planning/projects", "action":"deliverablesList", "param":"projectID=${project.id}"}
]/]

[#include "/WEB-INF/global/pages/header.ftl" /]
[#include "/WEB-INF/global/pages/main-menu.ftl" /]
[#import "/WEB-INF/global/macros/forms.ftl" as customForm/]
[#import "/WEB-INF/global/macros/logHistory.ftl" as log/]
[#import "/WEB-INF/planning/macros/projectDeliverablesTemplate.ftl" as deliverableTemplate/]
    
<section class="content">
  <div class="helpMessage">
    <img src="${baseUrl}/images/global/icon-help.png" />
    <p> [@s.text name="planning.deliverables.list.help" /] </p>
  </div>
  [#include "/WEB-INF/planning/planningProjectsSubMenu.ftl" /]
  
  <article class="halfContent" id="mainInformation"> 
    [#include "/WEB-INF/planning/projectOutputs-sub-menu.ftl" /]
    [#include "/WEB-INF/planning/planningDataSheet.ftl" /]
    [#-- Informing user that he/she doesn't have enough privileges to edit. See GrantProjectPlanningAccessInterceptor--]
    [#if !canEdit]
      <p class="readPrivileges">
        [@s.text name="saving.read.privileges"][@s.param][@s.text name=title/][/@s.param][/@s.text]
      </p>
    [/#if]
    <div id="projectDeliverables" class="clearfix">
      <h1 class="contentTitle">[@s.text name="planning.projectDeliverables.title" /]</h1> 
      [#if allYears?has_content]
      [#-- Planned Deliverables --]
      <div class="fullBlock clearfix">
        <h3 class="projectSubTitle">[@s.text name="planning.projectDeliverables.plannedDeliverables" /]</h3>
        [#if project.deliverables?has_content]
          [@deliverableTemplate.deliverablesList deliverables=project.deliverables canEdit=canEdit /]
        [#else]
          [#-- Just show this empty message to those users who are not able to modify this section --]
          <p class="simpleBox center">[@s.text name="planning.deliverables.empty"/]</p> 
        [/#if]   
        [#if canEdit]
        <div class="buttons"> 
          <a class="addButton" href="[@s.url namespace="/planning/projects" action='addNewDeliverable'] [@s.param name="${projectRequestID}"]${projectID}[/@s.param][/@s.url]">
            [@s.text name="planning.projectDeliverables.addNewDeliverable" /]
          </a>
        </div>
        [/#if]
      </div>
      [#else]
        [#-- If the project has not an start date and/or end date defined --]
        <p class="simpleBox center">[@s.text name="preplanning.projectBudget.message.dateUndefined" /]</p>
      [/#if]
    </div>     
  </article>
  
</section>
[@customForm.confirmJustification action="deleteDeliverable" namespace="/planning/projects" nameId="deliverableID" projectID="${projectID}" title="Remove Deliverable" /]
 
[#include "/WEB-INF/global/pages/footer.ftl"]