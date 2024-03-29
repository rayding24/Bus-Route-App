package ca.ubc.cs.cpsc210.translink.ui;

import android.graphics.*;
import android.graphics.Paint.Align;
import org.osmdroid.ResourceProxy;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;
import org.osmdroid.views.overlay.Overlay;

import java.util.HashMap;
import java.util.Map;

/**
 * Text overlay to display a mapping of bus route numbers to colours at the top right corner of the map.
 */
public class BusRouteLegendOverlay extends Overlay {
    private static final int LINEWIDTH = 150;
    private static final int LINEHEIGHT = 30;
    private static final int TEXTHEIGHT = 40;
    private static final int[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.MAGENTA,
            Color.YELLOW, Color.CYAN, Color.DKGRAY};
    private int topSkip;
    private Map<String, Integer> routes = new HashMap<>();
    private Paint paint;
    private int textHeight;
    private int nextColor;

    /**
     * Constructor
     *
     * @param resourceProxy needed for all overlays
     * @param dpiFactor     scaling factor used for text and spacing
     */
    public BusRouteLegendOverlay(ResourceProxy resourceProxy, float dpiFactor) {
        super(resourceProxy);
        textHeight = (int) (TEXTHEIGHT * dpiFactor);
        topSkip = textHeight;
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setTextSize(textHeight);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        paint.setTextAlign(Align.RIGHT);
    }

    /**
     * Clear the mapping
     */
    public void clear() {
        routes.clear();
        nextColor = 0;
    }

    /**
     * Add a bus route to the legend, choosing a color for it
     *
     * @param route the bus route number
     * @return the colour selected for this bus route
     */
    public int add(String route) {
        int color = colors[nextColor];
        routes.put(route, color);
        nextColor = (nextColor + 1) % colors.length;
        return color;
    }

    /**
     * Return the colour used for plotting the given bus route number
     *
     * @param route the bus route number
     * @return the colour it should be drawn in
     */
    public int getColor(String route) {
        return routes.get(route);
    }

    /**
     * Draw the legend in the corner of the screen.
     *
     * @param canvas the canvas to draw on
     * @param view   the map view
     * @param shadow whether this is to draw the shadow (we don't do shadows).
     */
    @Override
    protected void draw(Canvas canvas, MapView view, boolean shadow) {
        if (shadow) {
            return;
        }

        final Projection pj = view.getProjection();
        Rect screenRect = pj.getScreenRect();
        Point screenPosition = new Point();

        screenPosition.x = screenRect.right;
        screenPosition.y = screenRect.top + topSkip;

        for (Map.Entry<String, Integer> me : routes.entrySet()) {
            addRouteToLegend(canvas, screenPosition, me);
        }
    }

    /**
     * Adds a route to the legend
     *
     * @param canvas         the canvas on which legend is drawn
     * @param screenPosition the screen position of the top-right corner of legend
     * @param me             map entry containing route and corresponding color to be added to legend
     */
    private void addRouteToLegend(Canvas canvas, Point screenPosition, Map.Entry<String, Integer> me) {
        paint.setColor(me.getValue());
        paint.setAlpha(192);

        canvas.drawRect(screenPosition.x - LINEWIDTH,
                screenPosition.y - textHeight + (textHeight - LINEHEIGHT) / 2 + 10,
                screenPosition.x, screenPosition.y - (textHeight - LINEHEIGHT) / 2 + 10, paint);
        canvas.drawText(me.getKey(), screenPosition.x - LINEWIDTH - 10, screenPosition.y, paint);
        screenPosition.y += textHeight;
    }
}
