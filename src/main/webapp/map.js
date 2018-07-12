
function loadMap(activityIndex) {
	var longitudes, lattitudes;
	var long_done=false;
	var lat_done=false;
	$('#mapContainer').append($('<div>').attr('id','mapId').attr('style','height: 100%'));

	$.get("/rest/activities/" + activityIndex + "/LONG",
			function(data) {
				longitudes = data;
				long_done = true;
				if(lat_done == true && long_done == true) {
					drawMap(lattitudes, longitudes);
				}
			}
	)
	$.get("/rest/activities/" + activityIndex + "/LAT",
			function(data) {
				lattitudes = data;
				lat_done = true;
				if(lat_done == true && long_done == true) {
					drawMap(lattitudes, longitudes);
				}
			}
	)
}


function drawMap(lattitudes, longitudes) {
	// Where you want to render the map.
	var element = document.getElementById('mapId');

	// Height has to be set. You can do this in CSS too.
	// element.style = 'height:300px;';

	// Create Leaflet map on map element.
	var map = L.map(element);

	// Add OSM tile leayer to the Leaflet map.
	L.tileLayer('http://{s}.tile.osm.org/{z}/{x}/{y}.png', {
	    attribution: '&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
	}).addTo(map);

	// Target's GPS coordinates.
	var target = L.latLng(longitudes.y[0], lattitudes.y[0]);

	// Set map's center to target with zoom 10.
	map.setView(target, 10);

	// Place a marker on the same location.
	L.marker(target).addTo(map);
	var coordinates = [];
	for(var i = 0; i < lattitudes.y.length; i++) {
		coordinates.push([longitudes.y[i], lattitudes.y[i]]);
	}
	var polygon = L.polyline(coordinates).addTo(map);
}
function addLoadMapToggle(activityIndex) {
	var label = $("<label>");
	var checkbox = $("<input type='checkbox'>");
	label.append(checkbox);
	checkbox.checkboxradio({
		  id: 'checkbox_map',
		  name: 'map',
		  label: 'map'
	});
//	checkbox.attr('id','checkbox_map');
//	checkbox.attr('name', metric);
	$("#metricsCheckboxes").append(label);
	checkbox.change(function() {
		if($(this).is(":checked")) {
			loadMap(activityIndex);
		} else {
			removeMap();
		}
	})
}

function removeMap() {
	$('#mapId').remove();
	$('#mapContainer').resizable();
}