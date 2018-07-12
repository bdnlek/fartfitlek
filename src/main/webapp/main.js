function loadActivities() {
		$("#fileList").selectmenu();
		$("#fileUploadButton").button();
		$.get("/rest/activities",
			function(data) {
				var nrOfFiles = data.fileNames.length;
				$("#fileList option").each(function(index,option){
					$(option).remove();
				});
				$("#fileList").append("<option value=''>Choose one of " + nrOfFiles + " files</option>");
				$(data.fileNames).each(function(index, fileName){
					$("#fileList").append("<option value='" + index + "'>" + fileName + "</option>");
				});
				$('#fileList').on('selectmenuchange', function(event,ui){
					loadAvailableMetrics($('#fileList').val());
				});
				$("#fileList").selectmenu("refresh");
				$("#metrics").selectmenu();
			}
		).fail(function(error) {
			alert("error retrieving list of activities: " + error);
		});
};

function loadAvailableMetrics(activityIndex) {
		var timeSeriesContainer = $("#timeSeriesContainer");
		timeSeriesContainer.empty();
		timeSeriesContainer.append($("<div>").attr("id","timeSeries"));
		// remove existing checkboxes
		$("#metricsCheckboxes label").each(function(index,option){
			$(option).remove();
		});
		$.get("/rest/activities/" + activityIndex + "/metrics",
				function(data) {
					$(data).each(function(index, value){
						if(value != "" && value!="LONG" && value!="LAT") {
							addTimeSeriesCheckBox(activityIndex, value);
						}
					});
					if(contains(data,"LONG") && contains(data,"LAT")) {
						loadMap(activityIndex);
					}
				}
		).fail(function(jqXHR, textStatus, errorThrown){
			alert("error retrieving metrics for activity nr. " + activityIndex + ": " + textStatus + " in response: \n" + jqXHR.responseText);
            console.log('jqXHR:');
            console.log(jqXHR);
            console.log('textStatus:');
            console.log(textStatus);
            console.log('errorThrown:');
            console.log(errorThrown);
		})
		
}

function loadMap(activityIndex) {
	var longitudes, lattitudes;
	var long_done=false;
	var lat_done=false;
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
	var element = document.getElementById('mapid');

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
	var polygon = L.polygon(coordinates).addTo(map);
}

// no longer used
function drawPlotlyMap(lattitudes, longitudes) {
	
		  var data = [{
		      type: 'scattergeo',
		      lon: lattitudes.y,
		      lat: longitudes.y,
		      mode: 'lines',
		      line:{
		          width: 2,
		          color: 'blue'
		      }
		    }];

		  var layout = {
			title: 'map',
			showlegend: false,
			geo1: {
			    resolution: 50,
			    showland: true,
			    showlakes: true,
			    landcolor: 'rgb(204, 204, 204)',
			    countrycolor: 'rgb(204, 204, 204)',
			    lakecolor: 'rgb(255, 255, 255)',
			    projection: {
			      type: 'equirectangular'
			    },
			    coastlinewidth: 2,
			    lataxis: {
			      range: [ 48, 52 ],
			      showgrid: true,
			      tickmode: 'linear',
			      dtick: 10
			    },
			    lonaxis:{
			      range: [-10, 20],
			      showgrid: true,
			      tickmode: 'linear',
			      dtick: 20
			    }
			  },
			geo:{
			    scope: 'west europe',
			projection: {
			    type: 'azimuthal equal area'
			},
			showland: true,
			landcolor: 'rgb(243,243,243)',
			countrycolor: 'rgb(204,204,204)'
			}
		  };

		    Plotly.newPlot('map', data, layout);
};

function loadTimeSeries(fileIndex, metric) {
	$.get("/rest/activities/" + fileIndex + "/" + metric,
			function(data){
				var plotDiv = document.getElementById('timeSeries');
				var plotData = plotDiv.data;
				if(plotData != undefined) {
					var trace = {
							x: data["x"],
							y: data["y"]
					}
					Plotly.addTraces('timeSeries',[trace]);
				} else {
					var trace = {
							x: data["x"],
							y: data["y"],
							type: 'scatter'
					}
					var layout = {
							  title: 'Time Series with Rangeslider',
							  xaxis: {
							    autorange: true,
							    // range: ['2015-02-17', '2017-02-16'],
							    rangeselector: {buttons: [
							        {
							          count: 1,
							          label: '1m',
							          step: 'month',
							          stepmode: 'backward'
							        },
							        {
							          count: 6,
							          label: '6m',
							          step: 'month',
							          stepmode: 'backward'
							        },
							        {step: 'all'}
							      ]},
							    // rangeslider: {range: ['2015-02-17', '2017-02-16']},
							    type: 'date'
							  },
							  yaxis: {
							    autorange: true,
							    range: [86.8700008333, 138.870004167],
							    type: 'linear'
							  }
							};
					Plotly.newPlot('timeSeries', [trace], layout);					
				}
				
		
	}).fail(function(jqXHR, textStatus, errorThrown){
		alert("error retrieving timeSeries " + metric + " for activity nr. " + fileIndex + ": " + textStatus + " in response: \n" + jqXHR.responseText);
        console.log('jqXHR:');
        console.log(jqXHR);
        console.log('textStatus:');
        console.log(textStatus);
        console.log('errorThrown:');
        console.log(errorThrown);
	});
}

function uploadFile(fileInput) {
	var file = fileInput.files[0];
	console.log("uploading " + file.name + ": " + file.size + " bytes");
	var formData = new FormData();
	
	formData.append("file", file, file.name);
	formData.append("upload_file", true);
	$.ajax({
        url: 'rest/activities',
        type: 'POST',
        data: formData,
        cache: false,
        dataType: false,
        async: false,
        processData: false, // Don't process the files
        contentType: false, // 'multipart/form-data', // Set content type to false as jQuery will tell the server its a query string request
        success: function(data, textStatus, jqXHR)
        {
            if(data == undefined || typeof data.error === 'undefined')
            {
                // Success so call function to process the form
                // submitForm(event, data);
            	loadActivities();
            }
            else
            {
                // Handle errors here
                console.log('ERRORS: ' + data.error);
            }
        },
        error: function(jqXHR, textStatus, errorThrown)
        {
            // Handle errors here
            console.log('ERRORS: ' + textStatus);
            // STOP LOADING SPINNER
        }
    });
}

function addTimeSeriesCheckBox(activityIndex, metric) {
	var label = $("<label>");
	var checkbox = $("<input type='checkbox'>");
	label.append(checkbox);
	checkbox.checkboxradio({
		  id: 'checkbox_metric_' + metric,
		  name: metric,
		  label: metric
	});
	checkbox.attr('id','checkbox_metric_' + metric);
	checkbox.attr('name', metric);
	$("#metricsCheckboxes").append(label);
	checkbox.change(function() {
		if($(this).is(":checked")) {
			loadTimeSeries(activityIndex, $(this).attr('name'));
		} else {
			removeTimeSeries($(this).attr('name'));
		}
	})
}

function contains(a, obj) {
    for (var i = 0; i < a.length; i++) {
        if (a[i] === obj) {
            return true;
        }
    }
    return false;
}