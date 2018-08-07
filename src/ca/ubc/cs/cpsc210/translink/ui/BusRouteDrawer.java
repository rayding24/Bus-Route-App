package ca.ubc.cs.cpsc210.translink.ui;

import android.content.Context;
import ca.ubc.cs.cpsc210.translink.BusesAreUs;
import ca.ubc.cs.cpsc210.translink.model.Route;
import ca.ubc.cs.cpsc210.translink.model.RoutePattern;
import ca.ubc.cs.cpsc210.translink.model.Stop;
import ca.ubc.cs.cpsc210.translink.model.StopManager;
import ca.ubc.cs.cpsc210.translink.util.LatLon;
import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.ResourceProxy;
import org.osmdroid.bonuspack.overlays.Polyline;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static ca.ubc.cs.cpsc210.translink.util.Geometry.rectangleIntersectsLine;

// A bus route drawer
public class BusRouteDrawer extends MapViewOverlay {
    /**
     * overlay used to display bus route legend text on a layer above the map
     */
    private BusRouteLegendOverlay busRouteLegendOverlay;
    /**
     * overlays used to plot bus routes
     */
    private List<Polyline> busRouteOverlays;

    /**
     * Constructor
     *
     * @param context the application context
     * @param mapView the map view
     */
    public BusRouteDrawer(Context context, MapView mapView) {
        super(context, mapView);
        busRouteLegendOverlay = createBusRouteLegendOverlay();
        busRouteOverlays = new ArrayList<>();
    }

    /**
     * Plot each visible segment of each route pattern of each route going through the selected stop.
     */
    public void plotRoutes(int zoomLevel) {
        //: complete the implementation of this method (Task 7)
        // Polyline polyLine = new Polyline(context);


        Stop s = StopManager.getInstance().getSelected();
        List<GeoPoint> points = new ArrayList<>();

        initialize();

        if (!(s == null)) {
            for (Route r : s.getRoutes()) {
                int colorNum = busRouteLegendOverlay.add(r.getNumber());


                for (RoutePattern pattern : r.getPatterns()) {
                    plotEachRoutePattern(pattern, colorNum, zoomLevel, points);


                }
            }
        }


    }

    private void initialize() {
        updateVisibleArea();

        busRouteLegendOverlay.clear();

        busRouteOverlays.clear();

    }

    public void plotEachRoutePattern(RoutePattern pattern, int colorNum, int zoomLevel, List<GeoPoint> points) {
        int max = pattern.getPath().size() - 1;
        for (int i = 0; i < max; i++) {
            if (rectangleIntersectsLine(northWest, southEast, pattern.getPath().get(i), pattern.getPath().get(i + 1))) {

                LatLon latlon = pattern.getPath().get(i);
                LatLon latLon1 = pattern.getPath().get(i + 1);

                points.add(new GeoPoint(latlon.getLatitude(), latlon.getLongitude()));
                points.add(new GeoPoint(latLon1.getLatitude(), latLon1.getLongitude()));

                Polyline polyline = new Polyline(context);
                polyline.setColor(colorNum);

                polyline.setPoints(points);
                polyline.setWidth(getLineWidth(zoomLevel));
                busRouteOverlays.add(polyline);

                points.clear();
            }
        }
    }

    public List<Polyline> getBusRouteOverlays() {
        return Collections.unmodifiableList(busRouteOverlays);
    }

    public BusRouteLegendOverlay getBusRouteLegendOverlay() {
        return busRouteLegendOverlay;
    }


    /**
     * Create text overlay to display bus route colours
     */
    private BusRouteLegendOverlay createBusRouteLegendOverlay() {
        ResourceProxy rp = new DefaultResourceProxyImpl(context);
        return new BusRouteLegendOverlay(rp, BusesAreUs.dpiFactor());
    }

    /**
     * Get width of line used to plot bus route based on zoom level
     *
     * @param zoomLevel the zoom level of the map
     * @return width of line used to plot bus route
     */
    private float getLineWidth(int zoomLevel) {
        if (zoomLevel > 14) {
            return 7.0f * BusesAreUs.dpiFactor();
        } else if (zoomLevel > 10) {
            return 5.0f * BusesAreUs.dpiFactor();
        } else {
            return 2.0f * BusesAreUs.dpiFactor();
        }
    }
}
