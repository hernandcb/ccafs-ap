$(document).ready(function() {  
  
  function renameLeverages() {
    //getting the text of the index element.
    var itemText = $("#template").find(".itemIndex").text();
    $("#items .leverage").each(function(index, leverage) {
          // Changing attributes of each component in order to match
          // with the array order.
          // Main div.
          $(this).attr("id", "leverage-" + index);
          //Item index
          $(this).find(".itemIndex").text(itemText + " " + (index+1));
          // Remove link.
          $(this).find("[id^='removeLeverage-']").attr("id","removeLeverage-" + index);
          // Identifier
          $(this).find("[name$='id']").attr("name", "leverages[" + index + "].id");
          // Title.
          $(this).find("[id$='outputs']").attr("id", "leverages[" + index + "].title");
          $(this).find("[id$='outputs']").attr("name", "leverages[" + index + "].title");
          // Partner name.
          $(this).find("[id$='partnerName']").attr("id", "leverages[" + index + "].partnerName");
          $(this).find("[name$='partnerName']").attr("name", "leverages[" + index + "].partnerName");
          // Budget.
          $(this).find("[id$='budget']").attr("id", "leverages[" + index + "].budget");
          $(this).find("[name$='budget']").attr("name", "leverages[" + index + "].budget");
          // Start year.
          $(this).find("[id$='startYear']").attr("id", "leverages[" + index + "].startYear");
          $(this).find("[name$='startYear']").attr("name", "leverages[" + index + "].startYear");
          // How Used.
          $(this).find("[id$='endYear']").attr("id", "leverages[" + index + "].endYear");
          $(this).find("[name$='endYear']").attr("name", "leverages[" + index + "].endYear");
          // Evidence.
          $(this).find("[id$='theme']").attr("id", "leverages[" + index + "].theme");
          $(this).find("[name$='theme']").attr("name", "leverages[" + index + "].theme");
        }
    );
  }

  $(".addLeverage").click(function(event) {
    event.preventDefault();
    // Cloning template.
    var $newleverage = $("#leverage-9999").clone(true);
    $(".addLink").before($newleverage);
    $(".addLink").before("<hr />");
    renameLeverages();
    $newleverage.fadeIn("slow");
  });

  $('.removeLeverage').click(function(event) {
    event.preventDefault();
    // Getting the id.
    var removeId = $(event.target).attr("id").split("-")[1];
    $("#leverage-" + removeId).hide("slow", function() {
      // removing division line.
      $(this).next("hr").remove();
      // removing div.
      $(this).remove();
      renameLeverages();
    });
  });
  
  // Format the budget field
  $("[name$='budget']").on("keypress", isNumber);
  
});
