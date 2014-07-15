[#ftl]
[#assign title = "Main information" /]
[#assign globalLibs = ["jquery", "noty", "autoSave"] /]
[#assign customJS = ["${baseUrl}/js/global/utils.js", "${baseUrl}/js/preplanning/outputsPreplanning.js"] /]
[#assign currentSection = "preplanning" /]
[#assign currentPrePlanningSection = "impactPathways" /]
[#assign currentStage = "outputs" /]

[#include "/WEB-INF/global/pages/header.ftl" /]
[#include "/WEB-INF/global/pages/main-menu.ftl" /]
[#import "/WEB-INF/global/macros/forms.ftl" as customForm/]
[#import "/WEB-INF/global/macros/indicatorTemplate.ftl" as indicatorTemplate/]
[#import "/WEB-INF/preplanning/contributeTemplate.ftl" as contributeTemplate/]
    
<section class="content">
  <div class="helpMessage">
    <img src="${baseUrl}/images/global/icon-help.png" />
    <p> [@s.text name="planning.mainInformation.help" /] </p>
  </div>
  [#include "/WEB-INF/global/pages/pre-planning-secondary-menu.ftl" /]
  
  [@s.form action="outputs" cssClass="pure-form"]  
  <article class="halfContent" id="outputs">
    [#include "/WEB-INF/preplanning/ipPreplanningSubMenu.ftl" /]
    <h1 class="contentTitle">
    [@s.text name="preplanning.outputs.title" /]  
    </h1>
    <div id="outputBlocks"> 
        [#if outputs?has_content]
          [#list outputs as output]
          <div class="output" id="output-${output_index}">
            [#-- output identifier --] 
            <input type="hidden" name="outputs[${output_index}].id" value="${output.id}" />
            [#-- Remove output --]
            <div class="removeOutputBlock removeLink">              
              <img src="${baseUrl}/images/global/icon-remove.png" />
              <a id="removeOutput" href="" class="">[@s.text name="preplanning.outputs.removeOutput" /]</a>
            </div>  
            [#-- Title --]
            [@customForm.textArea name="outputs[${output_index}].description" i18nkey="preplanning.outputs.output" required=true /] 
            <div id="contributesBlock" class="contentElements parentsBlock">
              <div class="itemIndex">[@s.text name="preplanning.outputs.contributes" /] </div>
              [#-- output's parents --] 
              [#if output.contributesTo?has_content]
                [#list output.contributesTo as parent] 
                  [@contributeTemplate.outputs output_index="${output_index}" parent_index="${parent_index}" value="${parent.id}" description="${parent.description}" canRemove=true /]
                [/#list]
              [/#if]
              [#-- Add contribute --]
              <div class="fullBlock addContributeBlock">
                [@customForm.select name="contributionId" value="" showTitle=false listName="midOutcomesList" keyFieldName="id"  displayFieldName="description" addButton=true className="contributes" /]
              </div> 
            </div>   
          </div>  
          [/#list]
        [#else]
          <div class="output" id="output-0">
          [#-- output identifier --]
          <input type="hidden" name="outputs[0].id" value="-1" />
          [#-- Remove output --]      
          <div class="removeOutputBlock removeLink">            
            <img src="${baseUrl}/images/global/icon-remove.png" />
            <a id="removeOutput" href="" class="removeContribute">[@s.text name="preplanning.outputs.removeOutput" /]</a>
          </div> 
          [#-- Title --]
          [@customForm.textArea name="outputs[0].description" i18nkey="preplanning.outputs.output" required=true /] 
          <div id="contributesBlock" class="contentElements parentsBlock">
            <div class="itemIndex">[@s.text name="preplanning.outputs.contributes" /] </div>
              [#-- output's parents --]
               
              [#-- Add contribute --]
              <div class="fullBlock addContributeBlock"">
                [@customForm.select name="contribution" showTitle=false listName="midOutcomesList" keyFieldName="id"  displayFieldName="description" addButton=true className="contributes" /]
              </div> 
            </div>
        </div>
        [/#if]
      
    
    </div>
    <div id="addOutputBlock" class="addLink">
      <img src="${baseUrl}/images/global/icon-add.png" />
      <a href="" class="addOutput" >[@s.text name="preplanning.outputs.addOutput" /]</a>
    </div>
    
    [#----  Output TEMPLATE hidden ----]
    <div id="outputTemplate" class="output" style="display:none">
      [#-- Objective identifier --]
      <input id="outputId" type="hidden" value="-1" />
      [#-- Remove Output --]      
      <div class="removeLink removeOutputBlock">            
        <img src="${baseUrl}/images/global/icon-remove.png" />
        <a id="removeOutput" href="" class="removeContribute">[@s.text name="preplanning.outputs.removeOutput" /]</a>
      </div> 
      [#-- Title --]
      [@customForm.textArea name="outputDescription" i18nkey="preplanning.outputs.output" required=true /] 
      <div id="contributesBlock" class="contentElements">
        <div class="itemIndex">[@s.text name="preplanning.outputs.contributes" /] </div>
        [#-- Contribute area --]
         
        [#-- Add contribute --]
         <div class="fullBlock addContributeBlock">
          [@customForm.select name="contributions" showTitle=false listName="midOutcomesList" keyFieldName="id"  displayFieldName="description" addButton=true className="contributes" /]
        </div> 
      </div>  
    </div> 
    [#-- End Output template  --]
    [#-- Contribute template --]
    [@contributeTemplate.outputs template=true canRemove=true /]
    [#-- End Contribute template --] 
    
     <div class="buttons">
      [@s.submit type="button" name="save"][@s.text name="form.buttons.save" /][/@s.submit]
      [@s.submit type="button" name="next"][@s.text name="form.buttons.next" /][/@s.submit]
      [@s.submit type="button" name="cancel"][@s.text name="form.buttons.cancel" /][/@s.submit]
    </div>
  </article>
  [/@s.form]  
</section>
[#include "/WEB-INF/global/pages/footer.ftl"]







  