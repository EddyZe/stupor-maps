package ru.edd.stupor_maps.view;


import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import ru.edd.stupor_maps.models.Coordinates;
import software.xdev.vaadin.maps.leaflet.MapContainer;
import software.xdev.vaadin.maps.leaflet.basictypes.LIcon;
import software.xdev.vaadin.maps.leaflet.basictypes.LIconOptions;
import software.xdev.vaadin.maps.leaflet.basictypes.LLatLng;
import software.xdev.vaadin.maps.leaflet.basictypes.LPoint;
import software.xdev.vaadin.maps.leaflet.layer.raster.LTileLayer;
import software.xdev.vaadin.maps.leaflet.layer.ui.LMarker;
import software.xdev.vaadin.maps.leaflet.layer.vector.LPolygon;
import software.xdev.vaadin.maps.leaflet.map.LMap;
import software.xdev.vaadin.maps.leaflet.registry.LComponentManagementRegistry;
import software.xdev.vaadin.maps.leaflet.registry.LDefaultComponentManagementRegistry;

import java.util.ArrayList;
import java.util.List;


@Route("")
public class Maps extends VerticalLayout {
    private final String ID = "myMap";
    private final LComponentManagementRegistry reg;

    private final List<Coordinates> pointsPolygonCoordinates = new ArrayList<>();
    private final List<LMarker> markers = new ArrayList<>();

    private final LMap map;

    public Maps() {
        this.setId(ID);

        this.setSizeFull();

        reg = new LDefaultComponentManagementRegistry(this);

        map = configureMap();

        final HorizontalLayout buttonLayout = creatingButtonLayout();

        this.add(buttonLayout);
    }

    private HorizontalLayout creatingButtonLayout() {
        String clickFuncReference =
                "e => document.getElementById('%s').$server.mapClicked(e.latlng.lat, e.latlng.lng)".formatted(ID);

        reg.execJs(clickFuncReference);

        final HorizontalLayout hlButtons = new HorizontalLayout();

        Button createPolygon = new Button("Create polygon", this::creatingPolygon);
        Button addPoint = new Button("Add point", ev -> map.on("click", clickFuncReference));
        Button cancelCreatingPolygon = new Button("Cancel creating polygon", this::cancelCreatingPolygon);
        Button deleteLastPoint = new Button("Delete last point", this::deleteLastPoint);

        hlButtons.add(addPoint, createPolygon, deleteLastPoint, cancelCreatingPolygon);
        hlButtons.setWidthFull();

        return hlButtons;
    }

    private void deleteLastPoint(ClickEvent<Button> buttonClickEvent) {
            pointsPolygonCoordinates.remove(pointsPolygonCoordinates.size()-1);
            map.removeLayer(markers.remove(markers.size()-1));
    }

    private LMap configureMap() {
        final LMap map;
        final MapContainer mapContainer = new MapContainer(reg);
        mapContainer.setSizeFull();
        this.add(mapContainer);

        map = mapContainer.getlMap();

        map.addLayer(LTileLayer.createDefaultForOpenStreetMapTileServer(reg));

        map.setView(new LLatLng(reg, 55.75, 37.6), 15);
        return map;
    }

    private void creatingPolygon(ClickEvent<Button> click) {
        System.out.println("Координаты созданного полигона: ");
        int count = 1;

        List<LLatLng> lLatLngs = new ArrayList<>();

        for (Coordinates coordinates : pointsPolygonCoordinates) {
            lLatLngs.add(new LLatLng(reg, coordinates.getX(), coordinates.getY()));

            System.out.printf("%d. x = %s, y = %s%n",
                    count,
                    coordinates.getX(),
                    coordinates.getY());
            count ++;
        }

        markers.forEach(map::removeLayer);

        LPolygon lPolygon = new LPolygon(reg, lLatLngs);
        map.addLayer(lPolygon);

        markers.clear();
        pointsPolygonCoordinates.clear();
    }
    private void cancelCreatingPolygon(ClickEvent<Button> click) {
        markers.forEach(map::removeLayer);
        markers.clear();
        pointsPolygonCoordinates.clear();
    }

    private LIcon createIconMarker() {
        String urlIcon = "https://catherineasquithgallery.com/uploads/posts/2021-02/1614285898_46-p-chernii-fon-s-beloi-ramkoi-48.png";

        LIconOptions iconOptions = new LIconOptions();
        iconOptions.setIconSize(new LPoint(reg, 10, 10));
        iconOptions.setIconUrl(urlIcon);
        return new LIcon(reg, iconOptions);
    }

    @ClientCallable
    public void mapClicked(double x, double y) {
        LLatLng point = new LLatLng(reg, x, y);
        LMarker lMarker = new LMarker(reg, point);
        lMarker.setIcon(createIconMarker());

        map.addLayer(lMarker);
        markers.add(lMarker);

        Coordinates coordinates = new Coordinates(x, y);

        if (!pointsPolygonCoordinates.contains(coordinates))
            pointsPolygonCoordinates.add(new Coordinates(x, y));
    }
}
