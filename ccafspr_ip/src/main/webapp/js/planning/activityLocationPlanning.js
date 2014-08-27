// Global vars
var map;
var markers = [];
var countID;
$(document).ready(init);

function init(){
  countID = $("#locationsBlock .location").length;
  attachEvents();
  loadMap();
  setLocationsMarkers();
}

function attachEvents(){
  // Locations add/remove Events
  $("#addLocationLink").on("click", addLocationEvent);
  $("#locationsBlock .removeButton").on("click", removeLocationEvent);
  // Change Location type
  $("[name$='type.id']").on("change", changeTypeEvent);
  
  // isGlobale Change
  $("#isGlobal").on("change", changeGlobalState);
  
  $("[name$='geoPosition.latitude'], [name$='geoPosition.longitude']").on("keyup", reloadMarkers);
  $("[name$='geoPosition.latitude'], [name$='geoPosition.longitude']").on("focus", selectMarker);
  
  $("#fileBrowserLauncher").click(function(event){
    event.preventDefault();
    $("#excelTemplate").trigger("click");
  });
  
}

function changeGlobalState(e){
  if ($(e.target).is(':checked')) {
    $("#locationsBlock").fadeOut("slow");
    disableLocations(true);
    clearMarkers();
  } else {
    $("#locationsBlock").fadeIn("slow");
    disableLocations(false);
    showMarkers();
  }
  
}

function disableLocations(state){
  $("#locationsBlock .location").each(function(index,location){
    $(location).find("input,select").attr("disabled", state);
  });
}

function addLocationEvent(e){
  e.preventDefault();
  $newElement = $("#locationTemplateExisting").clone(true).removeAttr("id").attr("id", "location-" + (countID++)).addClass("location");
  $newElement.find(".removeButton").click(removeLocationEvent);
  $(e.target).parent().parent().find("#fields").append($newElement);
  $newElement.fadeIn("slow");
  setLocationIndex();
}
function removeLocationEvent(e){
  e.preventDefault();
  var locationId = $(e.target).parent().attr("id").split("-")[1];
  $(e.target).parent().hide("slow", function(){
    $(this).remove();
    removeMarker(locationId);
    setLocationIndex();
  });
  
}

function setLocationIndex(){
  var elementName;
  $("#locationsBlock .location").each(function(index,location){
    
    var locationTypeID = $(location).find("[name$='type.id']").val();
    $(location).find(".locationIndex strong").text((index + 1) + ".");
    // $(location).attr("id", "location-" + index);
    
    if (locationTypeID == 1) {
      // If Location is of type region
      
      elementName = "regionsSaved[" + index + "].";
      $(location).find("[name$='].id']").attr("name", elementName + "id");
      $(location).find("[name$='name']").attr("name", "name");
      $(location).find("[name$='type.id']").attr("name", "type.id");
      $(location).find("[name$='geoPosition.id']").attr("name", "geoPosition.id");
      $(location).find("[name$='geoPosition.latitude']").attr("name", "geoPosition.latitude").attr("placeholder", "Latitude");
      $(location).find("[name$='geoPosition.longitude']").attr("name", "geoPosition.longitude").attr("placeholder", "Longitude");
      
    } else if (locationTypeID == 2) {
      // If location is of type country
      
      elementName = "countriesSaved[" + index + "].";
      $(location).find("[name$='].id']").attr("name", elementName + "id");
      $(location).find("[name$='name']").attr("name", "name");
      $(location).find("[name$='type.id']").attr("name", "type.id");
      $(location).find("[name$='geoPosition.id']").attr("name", "geoPosition.id");
      $(location).find("[name$='geoPosition.latitude']").attr("name", "geoPosition.latitude").attr("placeholder", "Latitude");
      $(location).find("[name$='geoPosition.longitude']").attr("name", "geoPosition.longitude").attr("placeholder", "Longitude");
    } else {
      // If location is of other type
      
      elementName = "otherLocationsSaved[" + index + "].";
      $(location).find("[name$='].id']").attr("name", elementName + "id");
      $(location).find("[name$='name']").attr("name", elementName + "name");
      $(location).find("[name$='type.id']").attr("name", elementName + "type.id");
      $(location).find("[name$='geoPosition.id']").attr("name", elementName + "geoPosition.id");
      $(location).find("[name$='geoPosition.latitude']").attr("name", elementName + "geoPosition.latitude").attr("placeholder", "Latitude");
      $(location).find("[name$='geoPosition.longitude']").attr("name", elementName + "geoPosition.longitude").attr("placeholder", "Longitude");
    }
    
  });
}

function changeTypeEvent(e){
  var valId = $(e.target).val();
  var $parent = $(e.target).parent().parent().parent().parent();
  var $selectType = $("#selectTemplate-" + valId);
  if ($selectType.exists()) {
    var $newSelectType = $selectType.clone(true);
    $parent.find(".locationName").empty().html($newSelectType.html());
    $parent.find("[name$='geoPosition.latitude'], [name$='geoPosition.longitude']").attr("disabled", true).val($("#notApplicableText").val());
    
    if (typeof markers[$parent.attr("id").split("-")[1]] !== 'undefined') {
      removeMarker($parent.attr("id").split("-")[1]);
    }
    
  } else {
    var $newInputType = $("#inputNameTemplate").clone(true);
    $parent.find(".locationName").empty().html($newInputType.html());
    $parent.find("[name$='geoPosition.latitude'], [name$='geoPosition.longitude']").attr("disabled", false).val("0.0");
    $parent.find("[name$='name']").attr("placeholder", "Name");
    
    var latitude = $parent.find("[name$='geoPosition.latitude']").val();
    var longitude = $parent.find("[name$='geoPosition.longitude']").val();
    
    // if marker doesn't exist create the marker
    if (typeof markers[$parent.attr("id").split("-")[1]] === 'undefined') {
      // checks whether a coordinate is valid
      if (isCoordinateValid(latitude, longitude)) {
        makeMarker({
          latitude : latitude,
          longitude : longitude,
          name : $parent.find("[name$='name']").val(),
          id : $parent.attr("id").split("-")[1]
        });
      }
    }
    ;
    
  }
  setLocationIndex();
}

function loadMap(){
  geocoder = new google.maps.Geocoder();
  var style = [
      {
        "featureType" : "water",
        "stylers" : [
            {
              "visibility" : "on"
            }, {
              "color" : "#b5cbe4"
            }
        ]
      }, {
        "featureType" : "landscape",
        "stylers" : [
          {
            "color" : "#efefef"
          }
        ]
      }, {
        "featureType" : "road.highway",
        "elementType" : "geometry",
        "stylers" : [
          {
            "color" : "#83a5b0"
          }
        ]
      }, {
        "featureType" : "road.arterial",
        "elementType" : "geometry",
        "stylers" : [
          {
            "color" : "#bdcdd3"
          }
        ]
      }, {
        "featureType" : "road.local",
        "elementType" : "geometry",
        "stylers" : [
          {
            "color" : "#ffffff"
          }
        ]
      }, {
        "featureType" : "poi.park",
        "elementType" : "geometry",
        "stylers" : [
          {
            "color" : "#e3eed3"
          }
        ]
      }, {
        "featureType" : "administrative",
        "stylers" : [
            {
              "visibility" : "on"
            }, {
              "lightness" : 33
            }
        ]
      }, {
        "featureType" : "road"
      }, {
        "featureType" : "poi.park",
        "elementType" : "labels",
        "stylers" : [
            {
              "visibility" : "on"
            }, {
              "lightness" : 20
            }
        ]
      }, {}, {
        "featureType" : "road",
        "stylers" : [
          {
            "lightness" : 20
          }
        ]
      }
  ];
  map = new google.maps.Map(document.getElementById("activityLocations-map"), {
    center : new google.maps.LatLng(14.41, -12.52),
    zoom : 2,
    mapTypeId : 'roadmap',
    styles : style
  });
}

function setLocationsMarkers(){
  $("#locationsBlock .location").each(function(index,location){
    var latitude = $(location).find("[name$='geoPosition.latitude']").val();
    var longitude = $(location).find("[name$='geoPosition.longitude']").val();
    // checks whether a coordinate is valid
    if (isCoordinateValid(latitude, longitude)) {
      makeMarker({
        latitude : latitude,
        longitude : longitude,
        name : $(location).find("[name$='name']").val(),
        id : $(location).attr("id").split("-")[1]
      });
    }
  });
}

function selectMarker(e){
  var locationId = $(e.target).parent().parent().parent().attr("id").split("-")[1];
  var marker = markers[locationId];
  marker.setAnimation(google.maps.Animation.BOUNCE);
  setTimeout(function(){
    marker.setAnimation(null);
  }, 1000);
}

function makeMarker(data){
  var point = new google.maps.LatLng(data.latitude, data.longitude);
  var marker = new google.maps.Marker({
    id : data.id,
    map : map,
    position : point,
    icon : '../../../images/global/otherSite-marker.png',
    draggable : true,
    animation : google.maps.Animation.DROP
  });
  var html = "<strong>" + data.name + "</strong>";
  var infoWindow = new google.maps.InfoWindow;
  // Event when marker is clicked
  google.maps.event.addListener(marker, 'click', function(){
    infoWindow.setContent(html);
    infoWindow.open(map, marker);
  });
  // Event when marker is mouseover
  google.maps.event.addListener(marker, 'mouseover', function(){
    $("#location-" + marker.id).addClass("selected");
  });
  // Event when marker is mouseout
  google.maps.event.addListener(marker, 'mouseout', function(){
    $("#location-" + marker.id).removeClass("selected");
  });
  // Event when marker is dragged
  google.maps.event.addListener(marker, 'dragend', function(){
    $("#location-" + marker.id).find("[name$='geoPosition.longitude']").val(marker.position.B);
    $("#location-" + marker.id).find("[name$='geoPosition.latitude']").val(marker.position.k);
    
  });
  markers[data.id] = marker;
}

function reloadMarkers(){
  deleteMarkers();
  setLocationsMarkers();
}

function deleteMarkers(){
  setAllMap(null);
  markers = [];
}

// Removes the markers from the map, but keeps them in the array.
function clearMarkers(){
  setAllMap(null);
}

// Shows any markers currently in the array.
function showMarkers(){
  setAllMap(map);
}

function removeMarker(id){
  marker = markers[id];
  marker.setMap(null);
  delete markers[id];
  ;
}

// Sets the map on all markers in the array.
function setAllMap(map){
  $.each(markers, function(index,marker){
    console.log(marker);
    marker.setMap(map);
  });
}

function isCoordinateValid(latitude,longitude){
  if (latitude > -90 && latitude < 90 && longitude > -180 && longitude < 180) {
    return true;
  } else {
    return false;
  }
}
