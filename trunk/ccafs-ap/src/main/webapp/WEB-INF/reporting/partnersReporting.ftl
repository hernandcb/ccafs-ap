[#ftl]
[#assign title = "Activity Partners Report" /]
[#assign globalLibs = ["jquery", "noty"] /]
[#assign customJS = ["${baseUrl}/js/reporting/partnersReporting.js", "${baseUrl}/js/global/utils.js"] /]
[#assign customCSS = [""] /]
[#assign currentSection = "reporting" /]
[#assign currentReportingSection = "activities" /]
[#assign currentStage = "partners" /]
[#assign userRole = "${currentUser.role}"]

[#include "/WEB-INF/global/pages/header.ftl" /]
[#include "/WEB-INF/global/pages/main-menu.ftl" /]
[#import "/WEB-INF/global/macros/forms.ftl" as customForm /]

[#macro partnerSection]
  [#list activity.activityPartners as ap]
    <div id="activityPartner-${ap_index}" class="activityPartner">
      [#-- Partner identifier --]
      <input type="hidden" name="activity.activityPartners[${ap_index}].id" value="${ap.id?c}" />
      
      [#-- Remove link for all partners --]
      <div class="removeLink">
        <img src="${baseUrl}/images/global/icon-remove.png" />
        <a id="removeActivityPartner-${ap_index}" href="" class="removeActivityPartner">Remove partner</a>
      </div>
      
      [#-- Partner Name --]
      <div class="fullBlock partnerName">
        [@customForm.select name="activity.activityPartners[${ap_index}].partner" label="" i18nkey="reporting.activityPartners.partner.name" listName="partners" keyFieldName="id"  displayFieldName="name" /]
      </div>
      
      [#-- Contact Name --]
      <div class="halfPartBlock">
        [@customForm.input name="activity.activityPartners[${ap_index}].contactName" type="text" i18nkey="reporting.activityPartners.contactPersonName" /]
      </div>
      
      [#-- Contact Email --]
      <div class="halfPartBlock">
        [@customForm.input name="activity.activityPartners[${ap_index}].contactEmail" type="text" i18nkey="reporting.activityPartners.contactPersonEmail" /]
      </div> 
    </div> <!-- End activityPartner-${ap_index} -->
    <hr />
  [/#list]
[/#macro]

<section class="content">
  [#include "/WEB-INF/global/pages/reporting-secondary-menu.ftl" /]
  [@s.form action="partners"]
  <article class="halfContent">
    <h1 class="contentTitle">
      ${activity.leader.acronym} - [@s.text name="reporting.activityPartners.activity" /] ${activity.id}      
    </h1>
    
    <h6>[@s.text name="reporting.activityPartners.title" /]</h6>
    <p> ${activity.title} </p>
    
    [#assign typeSelectHeadValue ] [@s.text name="reporting.activityPartners.selectPartnerType" /] [/#assign]
    
    <div id="items">
      <fieldset id="activityPartnerGroup" class="group">
        <legend>[@s.text name="reporting.activityPartners.partners" /]</legend>
        [@partnerSection /]
      <div class="addLink">
        <img src="${baseUrl}/images/global/icon-add.png" />
        <a href="" class="addActivityPartner">Add new partner</a>
      </div>   
      </fieldset>
    </div>
    
    <p id="addPartnerText">
      If you don't find the partner what are you looking for 
      <a class="popup" href="[@s.url action='partnerSave'][@s.param name='${activityRequestParameter}']${activityID}[/@s.param][/@s.url]">
        add it here.
      </a>       
    </p>
    
    <!-- PARTNERS TEMPLATE -->
    <div id="template">
      <div id="activityPartner-9999" class="activityPartner" style="display: none;">      
        [#-- remove link --]
        <div class="removeLink">
          <img src="${baseUrl}/images/global/icon-remove.png" />
          <a id="removeActivityPartner-9999" href="" class="removeActivityPartner">Remove partner</a>
        </div>
        
        [#-- Partner identifier --]
        <input type="hidden" name="id" value="-1">
      
        [#-- Partner Name --]
        <div class="fullBlock partnerName">
          [@customForm.select name="__partner" label="" i18nkey="reporting.activityPartners.partner.name" listName="partners" keyFieldName="id"  displayFieldName="name" /]
        </div>
        
        [#-- Contact Name --]
        <div class="halfPartBlock">
          [@customForm.input name="contactName" type="text" i18nkey="reporting.activityPartners.contactPersonName" /]
        </div>
      
        [#-- Contact Email --]
        <div class="halfPartBlock">
          [@customForm.input name="contactEmail" type="text" i18nkey="reporting.activityPartners.contactPersonEmail" /]
        </div>
      </div> <!-- End partner template -->
    </div> <!-- End template -->
    
    <!-- internal parameter -->
    <input name="activityID" type="hidden" value="${activity.id}" />
    <div class="buttons">
      [@s.submit type="button" name="save"]SAVE[/@s.submit]
      [@s.submit type="button" name="cancel"]CANCEL[/@s.submit]
    </div>
         
    [#include "/WEB-INF/reporting/activitiesReportingSubMenu.ftl" /]  
    </article>
  [/@s.form]
 
  </section>
  
[#include "/WEB-INF/global/pages/footer.ftl"]