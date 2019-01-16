<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="jquery/jquery-ui-1.12.1/jquery-ui.css">
<link rel="stylesheet" href="main.css" ></link>
<link rel="stylesheet" href="https://unpkg.com/leaflet@1.2.0/dist/leaflet.css"
   integrity="sha512-M2wvCLH6DSRazYeZRIm1JnYyh22purTM+FDB5CsyxtQJYeKq83arPe5wgbNmcFXGqiSH2XR8dT/fJISVA1r/zQ=="
   crossorigin=""/>
<script src="jquery/core-1.12.14/jquery-1.12.4.js"></script>
<script src="jquery/jquery-ui-1.12.1/jquery-ui.min.js"></script>
<script src="https://cdn.plot.ly/plotly-latest.min.js"></script>
<script src="main.js"></script>
<script src="map.js"></script>
 <!-- Make sure you put this AFTER Leaflet's CSS -->
 <script src="https://unpkg.com/leaflet@1.2.0/dist/leaflet.js"
   integrity="sha512-lInM/apFSqyy1o6s89K4iQUKg6ppXEgsVxT35HbzUupEVRh2Eu9Wdl4tHj7dZO0s1uvplcYGmt3498TtHq+log=="
   crossorigin=""></script>


</head>
<body>

	<div style="z-index:5;">
		<select name="files" id="fileList"></select> 
		<input type="file" name="file" size="45" id="fileUploadInput" style="display:none;" onchange="uploadFile(this)"></input>
		<input type="button" onclick="$('#fileUploadInput').trigger('click')" value="+" id="fileUploadButton"></input>
		<span id="metricsCheckboxes"></span>
	</div>
	<div id="timeSeriesContainer">
		<div id="nonPowertimeSeries"></div>
		<div id="powertimeSeries"></div>
	</div>
	<div id="hoverinfo"></div>
	<div id="positions"></div>
	<div id="mapContainer"  style="height: 500px; z-index:10;">

	</div>
<script>
	loadActivities();
	$('#fileUploadInput').hide(); 
	$('#mapContainer').resizable();
</script>

</body>
<footer>
	<p>
		<a href="rest/activities">List of activities</a> | 
		<a href="rest/activities/0/metrics">metrics of activity 0</a>  | 
		<a href="rest/activities/0/HR">metric HR of activity 0</a> | 
		<a href="#" onclick="javascript:useCaseDoubleYAxis();">timeSeries</a>
	<p>
</footer>
</html>

