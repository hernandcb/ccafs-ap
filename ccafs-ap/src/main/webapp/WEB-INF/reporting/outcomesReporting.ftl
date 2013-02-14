[#ftl]
[#assign title = "Outcomes Report" /]
[#assign globalLibs = ["jquery", "noty"] /]
[#assign customJS = ["${baseUrl}/js/reporting/outcomesReporting.js", "${baseUrl}/js/global/utils.js"] /]
[#-- assign customCSS = ["${baseUrl}/css/reporting/partnersReporting.css"] / --]
[#assign currentSection = "reporting" /]
[#assign currentReportingSection = "outcomes" /]
[#assign userRole = "${currentUser.role}"]

[#include "/WEB-INF/global/pages/header.ftl" /]
[#include "/WEB-INF/global/pages/main-menu.ftl" /]
[#import "/WEB-INF/global/macros/forms.ftl" as customForm /]

[#macro outcomeSection]
  [#list outcomes as outcome]
    <div id="outcome-${outcome_index}" class="outcome">
      
      [#-- Item index --]
      <div class="itemIndex">
        [@s.text name="reporting.outcomes.outcomeTitle" /] ${publication_index +1}
      </div>
      
      [#-- Remove link for an outcome --]
      <div class="removeLink">
        <img src="${baseUrl}/images/global/icon-remove.png" />
        <a id="removeOutcome-${outcome_index}" href="" class="removeOutcome">[@s.text name="reporting.outcomes.removeOutcome" /]</a>
      </div>
      
      [#-- Identifier --]
      <input type="hidden" name="outcomes[${outcome_index}].id" value="${outcome.id}" />
      
      [#-- Outcome --]
      <div class="fullBlock">
        [@customForm.textArea name="outcomes[${outcome_index}].outcome" i18nkey="reporting.outcomes.outcome" /]
      </div>     
      
      [#-- Output --]
      <div class="fullBlock">
        [@customForm.textArea name="outcomes[${outcome_index}].outputs" i18nkey="reporting.outcomes.outputs" /]
      </div>
      
      [#-- partners --]
      <div class="fullBlock">
        [@customForm.textArea name="outcomes[${outcome_index}].partners" i18nkey="reporting.outcomes.partners" /]
      </div>
      
      [#-- Output User --]
      <div class="fullBlock">
        [@customForm.textArea name="outcomes[${outcome_index}].outputUser" i18nkey="reporting.outcomes.outputUser" /]
      </div>
      
      [#-- How Used --]
      <div class="fullBlock">
        [@customForm.textArea name="outcomes[${outcome_index}].howUsed" i18nkey="reporting.outcomes.howUsed" /]
      </div>
      
      [#-- Evidence --]
      <div class="fullBlock">
        [@customForm.textArea name="outcomes[${outcome_index}].evidence" i18nkey="reporting.outcomes.evidence" /]
      </div>
      
    </div> <!-- End outcomes-${outcome_index} -->
    <hr />
  [/#list]
[/#macro]

<section class="content">
  [#include "/WEB-INF/global/pages/reporting-secondary-menu.ftl" /]
  [@s.form action="outcomes"]
    <article class="halfContent">
      <h1 class="contentTitle">
        [@s.text name="reporting.outcomes" /] - ${currentUser.leader.acronym} 
      </h1>
      <div id="items">      
        <fieldset id="outcomeGroup">
          
          [@outcomeSection /]
          <div class="addLink">
            <img src="${baseUrl}/images/global/icon-add.png" />
            <a href="" class="addOutcome">[@s.text name="reporting.outcomes.addNewOutcome" /]</a>
          </div>
        </fieldset>
      </div>
      
      <!-- OUTCOME TEMPLATE -->
      <div id="template">
        <div id="outcome-9999" class="outcome" style="display: none;">
          
          [#-- Item index --]
          <div class="itemIndex">
            [@s.text name="reporting.outcomes.outcomeTitle" /]
          </div>
          
          [#-- Remove link for an outcome --]
          <div class="removeLink">
            <img src="${baseUrl}/images/global/icon-remove.png" />
            <a id="removeOutcome-9999" href="" class="removeOutcome">[@s.text name="reporting.outcomes.removeOutcome" /]</a>
          </div>
          
          [#-- Identifier --]
          <input type="hidden" name="id" value="-1" />
          
          [#-- Outcome --]
          <div class="fullBlock">
            [@customForm.textArea name="outcome" i18nkey="reporting.outcomes.outcome" /]
          </div>
          
          [#-- Output --]
          <div class="fullBlock">
            [@customForm.textArea name="outputs" i18nkey="reporting.outcomes.outputs" /]
          </div>
          
          [#-- partners --]
          <div class="fullBlock">
            [@customForm.textArea name="partners" i18nkey="reporting.outcomes.partners" /]
          </div>
          
          [#-- Output User --]
          <div class="fullBlock">
            [@customForm.textArea name="outputUser" i18nkey="reporting.outcomes.outputUser" /]
          </div>
          
          [#-- How Used --]
          <div class="fullBlock">
            [@customForm.textArea name="howUsed" i18nkey="reporting.outcomes.howUsed" /]
          </div>
          
          [#-- Evidence --]
          <div class="fullBlock">
            [@customForm.textArea name="evidence" i18nkey="reporting.outcomes.evidence" /]
          </div>
        </div> <!-- End outcome template -->
      </div> <!-- End template -->
      
      <div class="buttons">
        [@s.submit type="button" name="save"][@s.text name="form.buttons.save" /][/@s.submit]
        [@s.submit type="button" name="cancel"][@s.text name="form.buttons.cancel" /][/@s.submit]
      </div>
    </article>
  [/@s.form]
 
  </section>
  
[#include "/WEB-INF/global/pages/footer.ftl"]