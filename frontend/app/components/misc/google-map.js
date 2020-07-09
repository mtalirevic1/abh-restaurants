import Component from "@ember/component";

export default Component.extend({
  editableMarker: false,
  editableBounds: false,

  classNames: ["map-canvas"],

  didInsertElement(...args) {
    this._super(...args);
    this.renderGoogleMap(this.element);
  },

  renderGoogleMap(container) {
    let options = {
      center: new window.google.maps.LatLng(
        parseFloat(this.get("latitude")),
        parseFloat(this.get("longitude"))
      ),
      zoom: 7,
    };

    let map = new window.google.maps.Map(container, options);

    const bounds =
      typeof this.get("bounds") !== "undefined"
        ? JSON.parse(this.get("bounds"))
        : null;
    const coords =
      bounds !== null && bounds.length > 0
        ? bounds
        : [
            {
              lat: parseFloat(this.get("latitude")),
              lng: parseFloat(this.get("longitude")),
            },
            {
              lat: parseFloat(this.get("latitude")),
              lng: parseFloat(this.get("longitude")),
            },
            {
              lat: parseFloat(this.get("latitude")),
              lng: parseFloat(this.get("longitude")),
            },
            {
              lat: parseFloat(this.get("latitude")),
              lng: parseFloat(this.get("longitude")),
            },
          ];

    const zoomBounds = new google.maps.LatLngBounds();
    for (let coordiante in coords) {
      if (coords.hasOwnProperty(coordiante)) {
        zoomBounds.extend(
          new google.maps.LatLng(coords[coordiante].lat, coords[coordiante].lng)
        );
      }
    }

    map.fitBounds(zoomBounds);

    let markerPosition;
    if (this.get("markerLat") === 0 && this.get("markerLng") === 0) {
      markerPosition = zoomBounds.getCenter();
      this.set("defaultMerkerPosition", markerPosition);
    } else {
      markerPosition = new google.maps.LatLng(
        this.get("markerLat"),
        this.get("markerLng")
      );
    }

    const marker = new google.maps.Marker({
      position: markerPosition,
      draggable: this.get("editableMarker"),
    });

    marker.setMap(map);
    this.set("marker", marker);

    const polygon = new google.maps.Polygon({
      paths: coords,
      strokeColor: "#fd6f60",
      strokeOpacity: 0.8,
      strokeWeight: 2,
      fillColor: "#fd6f60",
      fillOpacity: 0.1,
      editable: this.get("editableBounds"),
      draggable: this.get("editableBounds"),
      geodesic: true,
    });

    polygon.setMap(map);
    this.set("polygon", polygon);
  },
});
