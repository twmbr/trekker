/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
var geocoder;
var map;
var wayPoints;
var directionsDisplay;
var directionsService = new google.maps.DirectionsService();
function initialize() {

    wayPoints = document.getElementById('createTrip:wayPoints');

    geocoder = new google.maps.Geocoder();
    var latlng = new google.maps.LatLng(-34.397, 150.644);
    var mapOptions = {
      zoom: 8,
      center: latlng,
      mapTypeId: google.maps.MapTypeId.ROADMAP
    }
    map = new google.maps.Map(document.getElementById("map_canvas"), mapOptions);
    
    directionsDisplay = new google.maps.DirectionsRenderer();
    directionsDisplay.setMap(map);
    
}
      

function getNewWayPointAddress() {
    var address = document.getElementById("createTrip:wp_address").value;
    var stop = document.getElementById("stopThere").checked;
    var loc=null;
    geocoder.geocode( { 'address': address}, function(results, status) {
        if (status == google.maps.GeocoderStatus.OK) {
            loc = results[0].geometry.location;
            
            //Get the reverse geocode address of the given address (assuming partial address)
            geocoder.geocode({'latLng': loc}, function(results, status) {
            if (status == google.maps.GeocoderStatus.OK) {
                if (results[0]) {
                    map.panTo(loc);
                    map.setZoom(12);
                   
                    if(stop == true){
                         var opt = new Option("StopOver: "+results[0].formatted_address,results[0].formatted_address);
                         document.getElementById("createTrip:wp_address").value = (String)(opt.text);            
                    }
                    else{
                         var opt = new Option("Not StopOver: "+results[0].formatted_address,results[0].formatted_address);
                         document.getElementById("createTrip:wp_address").value = (String)(opt.text);            
                    }
                }
            } else {
                alert("Geocoder failed due to: " + status);
            }
            });
            
        } else {
            alert("Geocode was not successful for the following reason: " + status);
            return;
        }
    });
    
  
    
}

function moveUpWayPointAddress() {
    var checkboxArray = document.getElementById("createTrip:wayPoints");
     var temp = new Array();
     var temp2;
     var temp3;
    for (var i = 0; i < checkboxArray.length; i++) {
        if (checkboxArray.options[i].selected == true) {
            temp.push(i);
        }
    }
    for (i=0;i<=temp.length-1;i++)
    {
        if (temp[i] != 0){
            if (checkboxArray.options[temp[i]-1].selected == false){
                temp2 = checkboxArray.options[temp[i]-1].value;
                temp3 = checkboxArray.options[temp[i]-1].text;
                checkboxArray.options[temp[i]-1] = new Option(checkboxArray.options[temp[i]].text,checkboxArray.options[temp[i]].value);
                checkboxArray.options[temp[i]] = new Option(temp3,temp2);
            }           
        }
    }
}

function moveDownWayPointAddress() {
    var checkboxArray = document.getElementById("createTrip:wayPoints");
     var temp = new Array();
     var temp2;
     var temp3;
    for (var i = 0; i < checkboxArray.length; i++) {
        if (checkboxArray.options[i].selected == true) {
            temp.push(i);
        }
    }
    for (i=temp.length-1;i>=0;i--)
    {
        if (temp[i] != checkboxArray.length-1){
            if (checkboxArray.options[temp[i]+1].selected == false){
                temp2 = checkboxArray.options[temp[i]].value;
                temp3 = checkboxArray.options[temp[i]].text;
                checkboxArray.options[temp[i]] = new Option(checkboxArray.options[temp[i]+1].text,checkboxArray.options[temp[i]+1].value);
                checkboxArray.options[temp[i]+1] = new Option(temp3,temp2);
            }           
        }
    }
}

function calcRoute() {  
    var start;
    var end;
    
    var address = document.getElementById("createTrip:start_address1").value;
    var loc=null;
    geocoder.geocode( { 'address': address}, function(results, status) {
        if (status == google.maps.GeocoderStatus.OK) {
            loc = results[0].geometry.location;
            
            //Get the reverse geocode address of the given address (assuming partial address)
            geocoder.geocode({'latLng': loc}, function(results, status) {
            if (status == google.maps.GeocoderStatus.OK) {
                if (results[0]) {                 
                    start = new Option(results[0].formatted_address);
                    document.getElementById("createTrip:start_address1").value = (String)(start.text);             
                }
            } else {
                alert("Geocoder failed due to: " + status);
            }
            });
            
        } else {
            alert("Geocode was not successful for the following reason: " + status);
            return;
        }
    });
    
    //end
    var address2 = document.getElementById("createTrip:end_address").value;
    var loc2=null;
    geocoder.geocode( { 'address': address2}, function(results, status) {
        if (status == google.maps.GeocoderStatus.OK) {
            loc2 = results[0].geometry.location;
            
            //Get the reverse geocode address of the given address (assuming partial address)
            geocoder.geocode({'latLng': loc2}, function(results, status) {
            if (status == google.maps.GeocoderStatus.OK) {
                if (results[0]) {
                    end = new Option(results[0].formatted_address);
                    document.getElementById("createTrip:end_address").value = (String)(end.text);              
                }
            } else {
                alert("Geocoder failed due to: " + status);
            }
            });
            
        } else {
            alert("Geocode was not successful for the following reason: " + status);
            return;
        }
    });
        
    var waypts = [];
    var checkboxArray = document.getElementById("createTrip:wayPoints");
    var parts = "";
    var parts1 = "";
    for (var i = 0; i < checkboxArray.length; i++) {
        parts1= checkboxArray.options[i].text.split('/');
        parts = parts1[1].split(':');
        if (checkboxArray.options[i]) {
            if (parts[0]=="StopOver")
            waypts.push({
                location:checkboxArray[i].value,
                stopover:true
            });
            else{
                 waypts.push({
                location:checkboxArray[i].value,
                stopover:false
            });
            }
        }
       
    }

    var request = {
        origin: address,
        destination: address2,
        waypoints: waypts,
        optimizeWaypoints: true,
        travelMode: google.maps.TravelMode.DRIVING
    };
    
    directionsService.route(request, function(response, status) {
        if (status == google.maps.DirectionsStatus.OK) {
            directionsDisplay.setDirections(response);
            var route = response.routes[0];
            var summaryPanel = document.getElementById("directions_panel");
            summaryPanel.innerHTML = "";
            var totaldistance = 0;
            var totalduration = 0;
            //find the total distance and duration
             for (var i = 0; i < route.legs.length; i++) {
                 totaldistance = totaldistance + route.legs[i].distance.value;
                 totalduration = totalduration + route.legs[i].duration.value;
             }
             
             //calculates the distance in miles
             totaldistance = totaldistance*.000621371;
             
             //calculates the duration in terms of days and hours and minutes
             var day = 0;
             var hour = 0;
             var minute = 0;
             day = Math.floor(totalduration/86400);
             totalduration = totalduration - (day*86400);
             hour = Math.floor(totalduration/3600);
             totalduration=totalduration - (hour*3600);
             minute = (Math.floor(totalduration/60)) +1;
             
             summaryPanel.innerHTML += "<b>Total Trip Distance: </b>" + totaldistance + "Miles<br />";
             summaryPanel.innerHTML += "<b>Total Trip Duration(non-stop): </b>" + day + "Days " + hour + "Hours " + minute + "Minutes" + "<br /><br />";
            // For each route, display summary information.
            for (var i = 0; i < route.legs.length; i++) {
                var routeSegment = i+1;
                summaryPanel.innerHTML += "<b>Route Segment: " + routeSegment + "</b><br />";
                summaryPanel.innerHTML += route.legs[i].start_address + " to ";
                summaryPanel.innerHTML += route.legs[i].end_address + "<br />";
                summaryPanel.innerHTML += "<b>Distance: </b>" + route.legs[i].distance.text + "<br />";
                summaryPanel.innerHTML += "<b>Estimated Duration: </b>" + route.legs[i].duration.text + "<br /><br />";
                for (var j = 0; j < route.legs[i].steps.length; j++) {
                summaryPanel.innerHTML += "<b>"+route.legs[i].steps[j].distance.text + "</b> " + route.legs[i].steps[j].instructions + "<br />";
                }
            }
        }
    });
    
    
}

