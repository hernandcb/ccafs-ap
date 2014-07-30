[#ftl]
[#assign title = "Project Budget" /]
[#assign globalLibs = ["jquery", "noty","autoSave"] /]
[#assign customJS = ["${baseUrl}/js/global/utils.js", "${baseUrl}/js/planning/mainInformation.js"] /]
[#assign currentSection = "preplanning" /]
[#assign currentPrePlanningSection = "projects" /]
[#assign currentStage = "budget" /]

[#include "/WEB-INF/global/pages/header.ftl" /]
[#include "/WEB-INF/global/pages/main-menu.ftl" /]
[#import "/WEB-INF/global/macros/forms.ftl" as customForm/]
    
<section class="content">
  <div class="helpMessage">
    <img src="${baseUrl}/images/global/icon-help.png" />
    <p> [@s.text name="planning.mainInformation.help" /] </p>
  </div>
  [#include "/WEB-INF/global/pages/pre-planning-secondary-menu.ftl" /]
  
  [@s.form action="projectBudget" cssClass="pure-form"]  
  <article class="halfContent" id="mainInformation">
  	[#include "/WEB-INF/preplanning/projectPreplanningSubMenu.ftl" /]
  	[#-- Tertiary Menu - All years --]
  	<nav id="tertiaryMenu">  
      <div class="container">
        <ul>
        [#list allYears as year]        
          <a class="yearLink" href=" [@s.url action='budget' includeParams='get'][@s.param name='projectID']${project.id?c}[/@s.param][@s.param name='year']${year?c}[/@s.param][/@s.url]" >
            <li>${year?c}</li>
          </a>  
        [/#list]
        </ul>
      </div> <!-- End tertiaryMenu -->
    </nav> <!-- End container-->
    
    [#-- Title --]
    <h1 class="contentTitle">
    [@s.text name="preplanning.projectBudget.title" /]  
    </h1>
    
       
    
    
  </article>
  [/@s.form]  
</section>
[#include "/WEB-INF/global/pages/footer.ftl"]