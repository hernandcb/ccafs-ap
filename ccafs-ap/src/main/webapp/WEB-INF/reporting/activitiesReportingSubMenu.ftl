[#ftl]
[#--
 
 * This file is part of CCAFS Planning and Reporting Platform.
 *
 * CCAFS P&R is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * at your option) any later version.
 *
 * CCAFS P&R is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CCAFS P&R. If not, see 
 * <http://www.gnu.org/licenses/>
  
--]

<nav id="stageMenu">
  <ul>
    <a [#if currentStage == "status"] class="currentReportingSection" [/#if] href="
        [@s.url action='status' includeParams='get'][/@s.url]
      "><li>[@s.text name="menu.activitiesReporting.submenu.status" /]</li>
    </a>
    <a [#if currentStage == "deliverables"] class="currentReportingSection" [/#if] href="
        [@s.url action='deliverables' includeParams='get'][/@s.url]
      "><li>[@s.text name="menu.activitiesReporting.submenu.deliverables" /]</li>
    </a>
    <a [#if currentStage == "partners"] class="currentReportingSection" [/#if] href="
        [@s.url action='partners' includeParams='get'][/@s.url]
      "><li>[@s.text name="menu.activitiesReporting.submenu.partners" /]</li>
    </a>
  </ul>
</nav>