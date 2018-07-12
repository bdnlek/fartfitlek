<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<style></style>
<link rel="stylesheet" href="https://unpkg.com/leaflet@1.2.0/dist/leaflet.css"
   integrity="sha512-M2wvCLH6DSRazYeZRIm1JnYyh22purTM+FDB5CsyxtQJYeKq83arPe5wgbNmcFXGqiSH2XR8dT/fJISVA1r/zQ=="
   crossorigin=""/>
<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script src="https://cdn.plot.ly/plotly-latest.min.js"></script>
<script src="main.js"></script>
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
		<div id="timeSeries"></div>	
	</div>
	<div id="mapContainer"  style="height: 500px; z-index:10;">
		 <div id="mapid" style="height: 100%;"></div>
	</div>
<script>
	loadActivities();
	$('#fileUploadInput').hide(); 
	$('#mapContainer').resizable();
</script>

</body>
<footer>
	<p>
		<a href="rest/activities">List of activities</a> | <a href="rest/activities/0/metrics">metrics of activity 0</a>
	<p>
</footer>
</html>

