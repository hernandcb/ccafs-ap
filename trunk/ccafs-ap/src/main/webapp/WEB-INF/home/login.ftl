[#ftl]
[#assign title = "Welcome to CCAFS Activity Planning" /]
[#assign jsIncludes = [""] /]
[#assign customCSS = ["${baseUrl}/css/home/login.css"] /]
[#assign currentSection = "home" /]

[#include "/WEB-INF/global/pages/header.ftl" /]
[#include "/WEB-INF/global/pages/main-menu.ftl" /]
[#import "/WEB-INF/global/macros/forms.ftl" as customForm /]

<article>
  <div class="content">
    <h1>[@s.text name="home.login.title" /]</h1>
    
    <p>
      [@s.text name="home.login.introduction" /]
    </p>
    <div id="loginFormContainer">
    [#if logged]
      <p class="alreadyLogged">[@s.text name="home.login.alreadyLogged" /]</p>
      <span class="alreadyLoggedEmail">${currentUser.email}</span>
    [#else]      
      [@s.form method="POST" action="login" cssClass="loginForm"]
        [@s.fielderror cssClass="fieldError" fieldName="loginMesage"/]    	
        [@customForm.input name="user.email" i18nkey="home.login.email" required=true /]
        [@customForm.input name="user.password" i18nkey="home.login.password" required=true type="password" /]
        [@s.submit key="home.login.button" name="login" /]    	
      [/@s.form]
    [/#if]
  	</div>
  	
  	<p>
      [@s.text name="home.login.followlink" /]
  	</p>
  	
  </div>
</article>
[#include "/WEB-INF/global/pages/footer.ftl"]