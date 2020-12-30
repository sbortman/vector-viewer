package vector.viewer

import geoscript.geom.Bounds
import geoscript.workspace.PostGIS

import java.awt.Color
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import org.apache.commons.io.output.ByteArrayOutputStream as FastByteArrayOutputStream

import geoscript.render.Map as GeoscriptMap
import static geoscript.style.Symbolizers.*

class MapWidgetService {
  def postgis = new PostGIS( 'geodata-dev', user: 'postgres' )

  static enum RenderMode {
    BLANK, GEOSCRIPT
  }

  def getTile( def params ) {

    int width = params.find { it.key.equalsIgnoreCase( 'WIDTH' ) }?.value?.toInteger()
    int height = params.find { it.key.equalsIgnoreCase( 'HEIGHT' ) }?.value?.toInteger()
    String format = params.find { it.key.equalsIgnoreCase( 'FORMAT' ) }?.value
    String type = format?.split( '/' )?.last()
    String projName = params?.find { it.key.equalsIgnoreCase( 'SRS' ) }?.value
    String bbox = params?.find { it.key.equalsIgnoreCase( 'BBOX' ) }?.value
    String[] layers = params?.find { it.key.equalsIgnoreCase( 'LAYERS' ) }?.value?.split( ',' )
    println params

    RenderMode renderMode = RenderMode.GEOSCRIPT
    def ostream = new FastByteArrayOutputStream()

    switch ( renderMode ) {
    case RenderMode.BLANK:
      def image = new BufferedImage( width, height, BufferedImage.TYPE_INT_ARGB )
      def g2d = image.createGraphics()

      g2d.color = Color.red
      g2d.drawRect( 0, 0, width - 1, height - 1 )
//    g2d.drawLine( 0, 0, width - 1, height - 1 )
//    g2d.drawLine( 0, height - 1, width - 1, 0 )
      g2d.dispose()

      ImageIO.write( image, type, new BufferedOutputStream( ostream ) )
      break
    case RenderMode.GEOSCRIPT:
      def coords = bbox?.split( ',' )?.collect { it.toDouble() }
      def bounds = new Bounds( *( coords ), projName )

//      def postgis = new PostGIS( 'geodata-dev', user: 'postgres' )

      def layer = postgis[ layers[ 0 ] ]
      def color

      switch ( layers[ 0 ] ) {
      case 'army_demo_car_detects':
        color = 'red'
        break
      case 'rfgeo_xband_radar_ellipses_multi':
        color = 'green'
        break
      case 'rfgeo_ais_ellipses_multi':
        color = 'blue'
        break
      }

      layer.style = stroke( color: color ) + fill( opacity: 0 )

      def map = new GeoscriptMap(
          type: type,
          width: width,
          height: height,
          bounds: bounds,
          proj: projName,
          fixAspectRatio: false,
          layers: [
              layer
          ]
      )

      map.render( new BufferedOutputStream( ostream ) )
      map?.close()
//      postgis?.close()
      break
    }


    [ contentType: format, file: new BufferedInputStream( ostream.toInputStream() ) ]
  }
}
