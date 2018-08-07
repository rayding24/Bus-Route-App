package ca.ubc.cs.cpsc210.translink.ui;

import android.content.Context;
import ca.ubc.cs.cpsc210.translink.BusesAreUs;
import ca.ubc.cs.cpsc210.translink.model.Route;
import ca.ubc.cs.cpsc210.translink.model.RoutePattern;
import ca.ubc.cs.cpsc210.translink.model.Stop;
import ca.ubc.cs.cpsc210.translink.model.StopManager;
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
        //TODO: complete the implementation of this method (Task 7)
       // Polyline polyLine = new Polyline(context);

        
//
//        Stop s = StopManager.getInstance().getSelected();
//        List<GeoPoint> path = new ArrayList<>();
//
//        updateVisibleArea();
//        busRouteOverlays.clear();
//        busRouteLegendOverlay.clear();
//        if (!(s == null)) {
//            for (Route r : s.getRoutes()) {
//
//
//                Polyline line;
//
//                for (RoutePattern rp : r.getPatterns()) {
//                    for (int i = 0, size = rp.getPath().size(); i < size - 1; i++) {
//                        if (rectangleIntersectsLine(northWest, southEast, rp.getPath().get(i), rp.getPath().get(i + 1))) {
//                            path.add(new GeoPoint(rp.getPath().get(i).getLatitude(), rp.getPath().get(i).getLongitude()));
//                            path.add(new GeoPoint(rp.getPath().get(i + 1).getLatitude(), rp.getPath().get(i + 1).getLongitude()));
//                            line = new Polyline(context);
//                            line.setColor( busRouteLegendOverlay.add(r.getNumber()));
//                            line.setWidth(getLineWidth(zoomLevel));
//                            line.setPoints(path);
//                            busRouteOverlays.add(line);
//                            path.clear();
//                        }
//                    }
//                }
//            }
//        }
//

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
