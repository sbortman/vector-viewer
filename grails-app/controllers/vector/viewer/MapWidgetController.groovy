package vector.viewer

class MapWidgetController {
    MapWidgetService mapWidgetService

    def index() {
        [initParams: [:]]
    }

    def getTile() {
        render mapWidgetService.getTile(params)
    }
}
