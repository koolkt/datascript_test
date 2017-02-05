(ns cmap.maps.leaflet.leaflet)

(def *hidelberg-tile* "http://korona.geog.uni-heidelberg.de/tiles/roads/x={x}&y={y}&z={z}")
(def *base-tile* "http://{s}.tile.osm.org/{z}/{x}/{y}.png")
(def *paris* #js[48.856614, 2.3522219])

(def *markers* (atom []))

(defn add-pop-up [lmap lat lng content]
  (-> js/L
      (.popup)
      (.setLatLng #js {"lat" lat "lon" lng})
      (.setContent content)
      (.addTo lmap)))

(defn add-layer
  ([lmap]
   (add-layer lmap *base-tile*))
  ([lmap layer-url]
   (let [attr "&copy; <a href=\"http://osm.org/copyright\">OpenStreetMap</a> contributors"]
     (-> js/L
         (.tileLayer layer-url #js {:attribution attr})
         (.addTo lmap)))))

(defn on-idle [lmap callback]
  (-> lmap
      (.on "moveend" callback)))

(defn get-bounds [lmap]
  (if-not (nil? lmap)
    (let [ne (-> lmap .getBounds .getNorthEast)
          sw (-> lmap .getBounds .getSouthWest)]
      {:ne {:lat (aget ne "lat")
            :lng (aget ne "lng")}
       :sw {:lat (aget sw "lat")
            :lng (aget sw "lng")}})
    {}))

(defn add-marker-to-map [marker lmap]
  (.addTo marker lmap))

(defn add-popup-to-marker [marker popup]
  (.bindPopup marker popup))

(defn create-marker [[lat  lng _ id]]
  (let [options #js {"lat" lat "lon" lng}
        icon (js/L.divIcon #js {"className" (str "my-icon " id)
                                "iconSize" #js [20 20]
                                "iconAnchor" #js [10 10]
                                "popupAnchor" #js [10 5]
                                "shadowSize" #js [0 0]})]
    (js/L.marker. options #js {"icon" icon})))

(defn create-popup-from-data [d]
  (str "<h3>" (nth d 2) "</h3>"))

(defn create-and-add-marker [d lmap]
  (-> (create-marker d)
      (add-popup-to-marker (create-popup-from-data d))
      (add-marker-to-map lmap)))

(defn add-markers! [data lmap]
  (reset! *markers* (doall (map #(create-and-add-marker % lmap) data))))

(defn init []
  (-> (js/L.Map. "map-canvas")
      (.setView *paris* 13)))
