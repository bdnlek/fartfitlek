function loadActivities() {
		$("#fileList").selectmenu();
		$("#fileUploadButton").button();
		$.get("rest/activities",
			function(data) {
				var nrOfFiles = data.length;
				$("#fileList option").each(function(index,option){
					$(option).remove();
				});
				$("#fileList").append("<option value=''>Choose one of " + nrOfFiles + " files</option>");
				for(var i = 0; i < nrOfFiles; i++) {
					var fitWrapper = data[i];
					
					$("#fileList").append("<option value='" + fitWrapper.file_id + "'>" + fitWrapper.name + "</option>");
				};
				$('#fileList').on('selectmenuchange', function(event,ui){
					removeMap();
					loadAvailableMetrics($('#fileList').val());
					addLoadMapToggle($('#fileList').val());
				});
				$("#fileList").selectmenu("refresh");
				$("#metrics").selectmenu();
			}
		).fail(function(error) {
			alert("error retrieving list of activities: " + error);
		});
};

function cleanMetrics(metrics) {
	var badMetrics = ['LONG','LAT','Pavg36000','','P','Pavg10','Pavg60','Pavg120'];
	var cleanedMetrics = [];
	$(metrics).each(function(index,value){
		if(!contains(badMetrics,value)) {
			cleanedMetrics.push(value);
		}
	});
	return cleanedMetrics;
}

function loadAllTimeSeries(activityIndex, metrics) {

	
	var metricMap = {};
	var cleanedMetrics = cleanMetrics(metrics);
	
	$(cleanedMetrics).each(function(index, metric) {
		var cleanedMetricsCount = cleanedMetrics.length;
		$.get("rest/activities/" + activityIndex + "/" + metric,
				function(data) {
					metricMap[metric] = data;
					var metricMapSize = Object.keys(metricMap).length;
					if(metricMapSize == cleanedMetricsCount) {
						drawTimeSeriesMap(metricMap);
					}
				}
		)
	});
}

function drawTimeSeriesMap(map) {
	var timeSeriesContainer = $("#timeSeriesContainer");
	timeSeriesContainer.empty();
	timeSeriesContainer.append($("<div>").attr("id","allTimeSeries"));
	var data = [];
	var layout = {
//			legend: {traceorder: 'reversed'},
	};
	var i = 0;
	$.each(map,function(index, metricMapEntry) {
		var j = "";
		if(i > 0) {
			j = i + 1;
		}
		var xValues = metricMapEntry['x'];
		var yValues = metricMapEntry['y'];
		var trace = {
				x: xValues,
				y: yValues,
				type: 'scatter',
				yaxis: 'y' + j,
				name: metricMapEntry['name'],
		};
		data.push(trace);
		var overlayingValue = "";
		if(i == 0) {
			layout['yaxis' + j] = {
					domain: [metricMapEntry['min'], metricMapEntry['max']],
					title: metricMapEntry['name']
			};			
		} else if (i == 1) {
			layout['yaxis' + j] = {
					domain: [metricMapEntry['min'], metricMapEntry['max']],
					title: metricMapEntry['name'],
					overlaying: 'y'
			};
		} else if (i > 1) {
			layout['yaxis' + j] = {
					domain: [metricMapEntry['min'], metricMapEntry['max']],
					title: metricMapEntry['name'],
					overlaying: "y" + (i - 1)
			};
		}
		i++;
	});
	Plotly.newPlot('allTimeSeries', data, layout);
}


function loadAvailableMetrics(activityIndex) {
		var timeSeriesContainer = $("#timeSeriesContainer");
		timeSeriesContainer.empty();
		timeSeriesContainer.append($("<div>").attr("id","powerTimeSeries"));
		timeSeriesContainer.append($("<div>").attr("id","nonPowerTimeSeries"));
		// remove existing checkboxes
		$("#metricsCheckboxes label").each(function(index,option){
			$(option).remove();
		});
		$.get("rest/activities/" + activityIndex + "/metrics",
				function(data) {
					loadAllTimeSeries(activityIndex,data);
					/*
					$(data).each(function(index, value){
						if(value != "" && value!="LONG" && value!="LAT" && value.substring(0,1) != 'P') {
							addTimeSeriesCheckBox('nonPowerTimeSeries', activityIndex, value);
						} else if (value == 'P') {
							addTimeSeriesCheckBox('powerTimeSeries', activityIndex, value);
						}
					});
					if(contains(data,"LONG") && contains(data,"LAT")) {
						addLoadMapToggle(activityIndex);
					}
					*/
				}
		).fail(function(jqXHR, textStatus, errorThrown){
			alert("error retrieving metrics for activity nr. " + activityIndex + ": " + textStatus + " in response: \n" + jqXHR.responseText);
            console.log('jqXHR:' + jqXHR);
            console.log('textStatus:' + textStatus);
            console.log('errorThrown:' + errorThrown);
		})
		
}

function drawTimeSeries(container, fileIndex, metric) {
	$.get("rest/activities/" + fileIndex + "/" + metric,
			function(data){
				var plotDiv = document.getElementById(container);
				var plotData = plotDiv.data;
				var min = Math.round(data.min);
				var max = Math.round(data.max);
				var trace = {
						x: data["x"],
						y: data["y"],
						type: 'scatter',
						name: metric,
						yaxis: 'y0'
				}
				var y_layout = {
						// linewidth: 6,
						visible: true,
						range: [min,max],
						type: 'linear',
						title: metric,
						spikemode: 'toaxis+across+marker',
						tickvals: [min,max],
						ticktext: [min,max],
						anchor: 'free'
				};
				if(plotData != undefined) {
					var count = plotData.length;
					trace['yaxis'] = 'y' + count;
					
					var layout = {};
					layout['yaxis' + count] = y_layout;
					layout['yaxis' + count]['side'] = 'right';
				    layout['yaxis' + count]['overlaying'] = 'y' + (count - 1);
					layout['yaxis' + count]['position'] = (1 - (count * 0.1));
					
					Plotly.update(container,{},layout);
					Plotly.addTraces(container,trace);
				} else {
					var layout = {
							  title: container,
							  autosize: false,
							  xaxis: {
							    // autorange: true,
							    type: 'seconds',
							    // linewidth: 6
							  },
							  // width: 1000,
							  height: 250,
							  // paper_bgcolor: '#7f7f7f',
							  plot_bgcolor: '#c7c7c7',
							  margin: {
								  l: 50,
								  r: 50,
								  t: 50,
								  b: 50
							  },

							  showlegend: true,
							  legend: {
								  "orientation": "h",
								  margin: {
									  t: 50,
									  b: 50
								  }
							  }
							};
					layout['yaxis'] = y_layout;
					Plotly.newPlot(container, [trace], layout);					
				}
				
		
	}).fail(function(jqXHR, textStatus, errorThrown){
		alert("error retrieving timeSeries " + metric + " for activity nr. " + fileIndex + ": " + textStatus + " in response: \n" + jqXHR.responseText);
        console.log('jqXHR: ' + jqXHR);
        console.log('textStatus: ' + textStatus);
        console.log('errorThrown: ' + errorThrown);
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

function addTimeSeriesCheckBox(container, activityIndex, metric) {
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
			drawTimeSeries(container, activityIndex, $(this).attr('name'));
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