[#ftl]
<script type="text/javascript">
  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
  })(window,document,'script','//www.google-analytics.com/analytics.js','ga');

  ga('create', 'UA-42082138-1', 'cgiar.org');
  
  [#if currentUser?has_content]
    // Create a custom variable to track the user who enter into the platform
    // This variable is create with session-level to send this data once per session
    // not in every page.
    
    [#-- Dimension1: Users --]
    ga('set', 'dimension1', '${currentUser.email}');
    
    [#-- metric1: Visits --]
    ga('set', 'metric1', metricValue);      
  [/#if]
    
  ga('send', 'pageview');

</script>