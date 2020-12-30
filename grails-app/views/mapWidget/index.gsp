<html>
    <head>
        <asset:stylesheet src="mapWidget.css"/>
    </head>
    <body>
        <div id="map"></div>
        <asset:javascript src="mapWidget.js"/>
        <asset:script>
        $(document).ready(function() {
            var initParams = ${raw(initParams?.encodeAsJSON()?.toString())};
            MapWidget.init();
        });
        </asset:script>
        <asset:deferredScripts/>
    </body>
</html>