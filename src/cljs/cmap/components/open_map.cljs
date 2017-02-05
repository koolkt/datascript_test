(ns cmap.components.open-map
  (:require [reagent.core :as r]
            [posh.reagent :as p]
            [cmap.db :as db]
            [cmap.maps.leaflet.leaflet :as lmap]))

(defonce omap (atom nil))

(defn open-map-inner []
  (r/create-class
   {:reagent-render
    (fn []
      [:div {:style {:flex "0 70%"}}
       [:div#map-canvas {:style {:height "100%"}} "map"]])

    :component-did-mount
    (fn [this]
      (let [data (:data (r/props this))
            conn (:conn (r/props this))
            mp (lmap/init)]
        (reset! omap mp)
        (lmap/add-layer mp)
        (lmap/add-markers! data mp)
        (db/update-bounds (lmap/get-bounds mp) conn)
        (lmap/on-idle mp #(db/update-bounds (lmap/get-bounds @omap) conn))))

    :component-did-update
    (fn [this]
      (when-let [data (:data (r/props this))]
        (println "placing markers!")
        (lmap/add-markers! data @omap)))

    :display-name
    "Leaflet map"}))

(defn open-map-outer [conn]
  (let [data (db/get-actors conn)]
    (fn []
      [open-map-inner {:data @data :conn conn}])))
