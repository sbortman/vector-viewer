//= require jquery-3.3.1.min.js
//= require webjars/openlayers/4.6.5/ol.js
//= require webjars/ol-layerswitcher/dist/ol-layerswitcher.js
//= require_self

var MapWidget = (function () {
    function init(params) {
        var layers = [
            new ol.layer.Group({
                // A layer must have a title to appear in the layerswitcher
                title: 'Base maps',
                layers: [
                    new ol.layer.Tile({
                        // A layer must have a title to appear in the layerswitcher
                        title: 'Vector',
                        // Again set this layer as a base layer
                        type: 'base',
                        visible: true,
                        source: new ol.source.TileWMS({
                            url: 'https://omar-dev.ossim.io/omar-mapproxy/service',
                            params: {
                                'LAYERS': 'o2-basemap-basic'
                            }
                        })
                    }),
                    new ol.layer.Tile({
                        title: 'Raster',
                        type: 'base',
                        visible: false,
                        source: new ol.source.TileWMS({
                            url: 'https://ahocevar.com/geoserver/wms',
                            params: {
                                'LAYERS': 'ne:NE1_HR_LC_SR_W_DR',
                                'TILED': true
                            }
                        })
                    })
                ]
            }),
            new ol.layer.Group({
                // A layer must have a title to appear in the layerswitcher
                title: 'Overlays',
                layers: [
                    new ol.layer.Tile({
                        // A layer must have a title to appear in the layerswitcher
                        title: 'Car Detects',
                        // Again set this layer as a base layer
                        visible: true,
                        source: new ol.source.TileWMS({
                            url: '/mapWidget/getTile',
                            params: {
                                'LAYERS': 'army_demo_car_detects',
                                'VERSION': '1.1.1'
                            }
                        })
                    }),
                    new ol.layer.Tile({
                        // A layer must have a title to appear in the layerswitcher
                        title: 'X Band Radar Ellipses',
                        // Again set this layer as a base layer
                        visible: true,
                        source: new ol.source.TileWMS({
                            url: '/mapWidget/getTile',
                            params: {
                                'LAYERS': 'rfgeo_xband_radar_ellipses_multi',
                                'VERSION': '1.1.1'
                            }
                        })
                    }),
                    new ol.layer.Tile({
                        // A layer must have a title to appear in the layerswitcher
                        title: 'AIS Ellipses',
                        // Again set this layer as a base layer
                        visible: true,
                        source: new ol.source.TileWMS({
                            url: '/mapWidget/getTile',
                            params: {
                                'LAYERS': 'rfgeo_ais_ellipses_multi',
                                'VERSION': '1.1.1'
                            }
                        })
                    }),
                ]
            }),
        ];

        var map = new ol.Map({
            controls: ol.control.defaults().extend([
                new ol.control.ScaleLine({
                    units: 'degrees'
                })
            ]),
            layers: layers,
            target: 'map',
            view: new ol.View({
                projection: 'EPSG:4326',
                center: [0, 0],
                zoom: 2
            })
        });

        var layerSwitcher = new ol.control.LayerSwitcher({
            tipLabel: 'Légende', // Optional label for button
            groupSelectStyle: 'children' // Can be 'children' [default], 'group' or 'none'
        });
        map.addControl(layerSwitcher);
    }

    return {
        init: init
    };
})();