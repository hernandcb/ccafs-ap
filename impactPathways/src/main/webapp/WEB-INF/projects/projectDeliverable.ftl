[#ftl]
[#assign title = "Project Deliverable" /]
[#assign globalLibs = ["jquery", "noty", "autoSave", "chosen", "star-rating"] /]
[#assign customJS = ["${baseUrl}/js/global/utils.js", "${baseUrl}/js/global/usersManagement.js", "${baseUrl}/js/projects/projectDeliverables.js"] /]
[#assign currentSection = "planning" /]
[#assign currentPlanningSection = "projects" /]
[#assign currentStage = "outputs" /]
[#assign currentSubStage = "deliverables" /]

[#assign breadCrumb = [
  {"label":"planning", "nameSpace":"planning", "action":"projectsList"},
  {"label":"projects", "nameSpace":"planning", "action":"projectsList"},
  {"label":"projectOutputs", "nameSpace":"planning/projects", "action":"outputs", "param":"projectID=${project.id}"},
  {"label":"projectDeliverables", "nameSpace":"planning/projects", "action":"deliverablesList", "param":"projectID=${project.id}"}
  {"label":"projectDeliverable", "nameSpace":"planning/projects", "action":"deliverable", "param":"deliverableID=${deliverable.id}"}
]/]

[#assign params = {
  "deliverable":  {"id":"deliverableName", "name":"deliverable"},
  "responsiblePartner":  {"id":"responsiblePartnerName", "name":"deliverable.responsiblePartner"},
  "nextUsers":    {"id":"nextUsersName",   "name":"deliverable.nextUsers"},
  "partners":     {"id":"partnersName",    "name":"deliverable.otherPartners"} 
}
/] 

[#include "/WEB-INF/global/pages/header.ftl" /]
[#include "/WEB-INF/global/pages/main-menu.ftl" /]
[#import "/WEB-INF/global/macros/forms.ftl" as customForm/]
[#import "/WEB-INF/global/macros/usersPopup.ftl" as usersForm/]
[#import "/WEB-INF/global/macros/logHistory.ftl" as log/]
[#import "/WEB-INF/projects/macros/projectDeliverablesTemplate.ftl" as deliverableTemplate/]
    
<section class="content">
  <div class="helpMessage">
    <img src="${baseUrl}/images/global/icon-help.png" />
    <p> [@s.text name="planning.deliverables.help" /] 
    <a href = [@s.url namespace="/" action='glossary'][/@s.url]>[@s.text name="planning.deliverables.help2" /]</a>
    </p>
  </div>
  [#include "/WEB-INF/projects/projectsSubMenu.ftl" /]
  
  [@s.form action="deliverable" cssClass="pure-form"]
  <article class="halfContent" id="projectDeliverable">  
    [#include "/WEB-INF/projects/dataSheet.ftl" /]
    <br />
    
    [#-- Informing user that he-she does not have enough privileges to edit. See GrantProjectPlanningAccessInterceptor --]  
    [#if submission?has_content]
      <p class="projectSubmitted">[@s.text name="submit.projectSubmitted" ][@s.param]${(submission.dateTime?date)?string.full}[/@s.param][/@s.text]</p>
    [#elseif !canEdit ]
      <p class="readPrivileges">
        [@s.text name="saving.read.privileges"][@s.param][@s.text name=title /][/@s.param][/@s.text]
      </p>
    [/#if]
     
    [#--  Deliverable Menu --] 
    <ul> 
      <li class=""><a href="#deliverable-information">Information</a></li>
      <li class=""><a href="#deliverable-nextUsers">Next Users</a></li> 
      <li class=""><a href="#deliverable-partnership">Partners </a></li>
      [#if reportingCycle] 
        <li class=""><a href="#deliverable-ranking">Ranking</a></li> 
        <li class=""><a href="#deliverable-dissemination">Dissemination</a></li> 
        <li class=""><a href="#deliverable-metadata">Metadata</a></li> 
      [/#if]
    </ul> 
    [#-- Deliverable Information --] 
    [#include "/WEB-INF/projects/deliverable/deliverableInformation.ftl" /]
    [#-- Deliverable Next Users block  --]
    [#include "/WEB-INF/projects/deliverable/deliverableNextUsers.ftl" /]
    [#-- Deliverable partnership  --]
    [#include "/WEB-INF/projects/deliverable/deliverablePartners.ftl" /]
    
    [#-- -- -- REPORTING BLOCK -- -- --]
    [#if reportingCycle]
      [#-- Deliverable ranking --]
      [#include "/WEB-INF/projects/deliverable/deliverableRanking.ftl" /]
      [#-- Deliverable Dissemination --]
      [#include "/WEB-INF/projects/deliverable/deliverableDissemination.ftl" /]
      [#-- Deliverable Metadata --]
      [#include "/WEB-INF/projects/deliverable/deliverableMetadata.ftl" /]
    [/#if]
    
    [#if editable] 
      <input name="projectID" type="hidden" value="${project.id?c}" />
      <input name="deliverableID"type="hidden" value="${deliverable.id}">
      <div class="[#if !newProject]borderBox[/#if]" >
        [#if !newProject] [@customForm.textArea name="justification" i18nkey="saving.justification" required=true className="justification"/][/#if]
        <div class="buttons">
          [@s.submit type="button" name="save"][@s.text name="form.buttons.save" /][/@s.submit]
          [@s.submit type="button" name="next"][@s.text name="form.buttons.next" /][/@s.submit]
          [@s.submit type="button" name="cancel"][@s.text name="form.buttons.cancel" /][/@s.submit]
        </div>
      </div>
    [#else]
        [#-- Display Log History --]
        [#if history??][@log.logList list=history /][/#if]   
    [/#if]
  </article>
  [/@s.form] 
   
</section> 

[#-- Internal parameters --]   
[#list params?keys as prop]
  <input id="${params[prop].id}" type="hidden" value="${params[prop].name}" /> 
[/#list]

[#-- Project deliverable Next user Template--]
[@deliverableTemplate.nextUserTemplate template=true /]

[#-- Deliverable Partner Template--]
[@deliverableTemplate.deliverablePartner dp={} dp_name=params.partners.name dp_index=dp_index template=true /]

[#include "/WEB-INF/global/pages/footer.ftl"]