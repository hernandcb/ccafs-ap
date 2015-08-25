[#ftl]
[#macro projectPartner projectPartner={} projectPartnerName="" projectPartnerIndex="-1" template=false ]
  <div id="projectPartner-${template?string('template',(projectPartner.id)!)}" class="projectPartner borderBox ${(projectPartner.leader?string('leader',''))!} ${(projectPartner.coordinator?string('coordinator',''))!}" style="display:${template?string('none','block')}">
    <div class="loading" style="display:none"></div>
    [#-- Edit Button  --]
    [#if (!editable && canEdit)]
      <div class="editButton"><a href="[@s.url][@s.param name ="projectID"]${project.id}[/@s.param][@s.param name="edit"]true[/@s.param][/@s.url]#projectPartner-${(projectPartner.id)!}">[@s.text name="form.buttons.edit" /]</a></div>
    [/#if]
    
    [#-- Remove link for all partners --]
    [#if editable]
      <div class="removeLink"><div id="removePartner" class="removePartner removeElement removeLink" title="[@s.text name="preplanning.projectPartners.removePartner" /]"></div></div>
    [/#if]
    
    <div class="leftHead">
      <span class="index">${projectPartnerIndex?number+1}</span>
      <span class="elementId">Project Partner  <strong class="type"> ${(projectPartner.leader?string('(Leader)',''))!} ${(projectPartner.coordinator?string('(Coordinator)',''))!}</strong></span>
    </div>
    <input id="id" class="partnerId" type="hidden" name="${projectPartnerName}[${projectPartnerIndex}].id" value="${(projectPartner.id)!}" />

    [#-- Filters --]
    [#if editable]
      <div class="filters-link">[@s.text name="preplanning.projectPartners.filters" /]</div>
      <div class="filters-content">
        [#-- Partner type list --]
        <div class="halfPartBlock partnerTypeName chosen">
          [#-- Name attribute is not needed, we just need to load the value, not save it it. --]
          [@customForm.select name="" label="" disabled=!editable i18nkey="preplanning.projectPartners.partnerType" listName="partnerTypes" keyFieldName="id"  displayFieldName="name" className="partnerTypes" value="${(projectPartner.institution.type.id)!-1}" /]
        </div>
        [#-- Country list --]
        <div class="halfPartBlock countryListBlock chosen">
          [#-- Name attribute is not needed, we just need to load the value, not save it it. --]
          [@customForm.select name="" label="" disabled=!editable i18nkey="preplanning.projectPartners.country" listName="countries" keyFieldName="id"  displayFieldName="name" className="countryList" value="'${(projectPartner.institution.country.id)!-1}'" /]
        </div>
      </div> 
    [/#if]
    
    [#-- Organization  --]
    <div class="fullPartBlock partnerName chosen">
      [@customForm.select name="${projectPartnerName}[${projectPartnerIndex}].institution" value="${(projectPartner.institution.id)!'-1'}" className="institutionsList" required=true  disabled=!editable i18nkey="preplanning.projectPartners.partner.name" listName="allInstitutions" keyFieldName="id"  displayFieldName="getComposedName()" editable=editable /]
    </div>
    
    [#-- Indicate which PPA Partners for second level partners --]
    [#if projectPartner.institution??]
      [#if !projectPartner.institution.PPA]
        <div class="ppaPartnersList panel tertiary" style="display:block">
      [#else]
        <div class="ppaPartnersList panel tertiary" style="display:none">
      [/#if]
    [#else]
    <div class="ppaPartnersList panel tertiary" style="display:none">
    [/#if]
      <div class="panel-head">[@customForm.text name="preplanning.projectPartners.indicatePpaPartners" readText=!editable /]</div> 
      <div class="panel-body">
        [#if !(projectPartner.contributeInstitutions?has_content) && !editable]
          <p>[@s.text name="planning.projectPartners.noSelectedCCAFSPartners" /] </p>
        [/#if]
        <ul class="list"> 
        [#if projectPartner.contributeInstitutions?has_content]
          [#list projectPartner.contributeInstitutions as ppaPartner]
            <li class="clearfix [#if !ppaPartner_has_next]last[/#if]">
              <input class="id" type="hidden" name="${projectPartnerName}[${projectPartnerIndex}].contributeInstitutions[${ppaPartner_index}].id" value="${ppaPartner.id}" />
              <span class="name">${(ppaPartner.composedName)!}</span> 
              [#if editable]<span class="listButton remove">[@s.text name="form.buttons.remove" /]</span>[/#if]
            </li>
          [/#list]
        [/#if]
        </ul>
        [#if editable]
          [@customForm.select name="" label="" disabled=!canEdit i18nkey="" listName="" keyFieldName="id"  displayFieldName="getComposedName()" className="ppaPartnersSelect" value="" /]
        [/#if] 
      </div>
    </div>
    
    [#-- Contacts person  --]
    <div class="contactsPerson panel tertiary">
      <div class="panel-head">[@s.text name="planning.projectPartners.projectPartnerContacts" /]</div>
      <div class="fullPartBlock">
      [#if projectPartner.partnerPersons?has_content]
        [#list projectPartner.partnerPersons as partnerPerson]
          [@contactPerson contact=partnerPerson contactName="${projectPartnerName}[${projectPartnerIndex}].partnerPersons" contactIndex="${partnerPerson_index}" /]
        [/#list]
      [#else]
        [@contactPerson contactName="${projectPartnerName}[${projectPartnerIndex}].partnerPersons" contactIndex="0" /]
      [/#if]  
      [#if (editable && canEdit)]
        <div class="addContact"><a href="" class="addLink">[@s.text name="planning.projectPartners.addContact"/]</a></div> 
      [/#if]
      </div>
    </div>
    
  </div>
[/#macro]

[#macro contactPerson contact={} contactName="" contactIndex="-1" template=false]
  <div id="contactPerson-${template?string('template',(contact.id)!)}" class="contactPerson simpleBox ${(contact.type)!}" style="display:${template?string('none','block')}">
    [#-- Remove link for all partners --]
    [#if editable]
      <div class="removePerson removeElement" title="[@s.text name="planning.projectPartners.removePerson" /]"></div>
    [/#if]
    <div class="leftHead">
      <span class="index">${contactIndex?number+1}</span>
    </div>
    <input id="id" class="partnerPersonId" type="hidden" name="${contactName}[${contactIndex}].id" value="${(contact.id)!-1}" />
    
    [#-- Partner Person type and email--]
    <div class="fullPartBlock"> 
      <div class="partnerPerson-type halfPartBlock clearfix">
      [@customForm.select name="${contactName}[${contactIndex}].type" className="partnerPersonType" disabled=!canEdit i18nkey="planning.projectPartners.personType" listName="partnerPersonTypes" value="'${(contact.type)!-1}'" editable=editable /]
      [#if !editable][@s.text name="planning.projectPartners.types.${(contact.type)!}"/][/#if]
      </div>
      <div class="partnerPerson-email userField halfPartBlock clearfix">
        [#-- Contact Person information is going to come from the users table, not from project_partner table (refer to the table project_partners in the database) --] 
        [@customForm.input name="contact-person-${contactIndex}" value="${(contact.user.composedName?html)!}" className="userName" type="text" disabled=!canEdit i18nkey="preplanning.projectPartners.contactPersonEmail" required=!project.bilateralProject readOnly=true editable=editable/]
        [#if editable]<div class="searchUser">[@s.text name="form.buttons.searchUser" /]</div>[/#if]
        <input class="userId" type="hidden" name="${contactName}[${contactIndex}].user" value="${(contact.user.id)!'-1'}">   
      </div>
    </div>
    
    [#-- Responsibilities --] 
    <div class="fullPartBlock partnerResponsabilities chosen"> 
      [@customForm.textArea name="${contactName}[${contactIndex}].responsibilities" className="resp" i18nkey="preplanning.projectPartners.responsabilities" required=!project.bilateralProject editable=editable /]
    </div>
    
    [#if !template]
    [#-- Activities leading and Deliverables with responsibilities --]
    <div class="fullPartBlock clearfix"> 
      <div class="tag">[@s.text name="planning.projectPartners.personActivities"][@s.param]3[/@s.param][/@s.text]</div>
      <div class="tag">[@s.text name="planning.projectPartners.personDeliverables"][@s.param]1[/@s.param][/@s.text]</div>
    </div>
    [/#if]
  </div>
[/#macro]
