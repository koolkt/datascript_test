(ns cmap.components.card-sidebar
  (:require
      [cmap.db :as db]
      [cmap.components.actor-card :refer [actor-card]]))

(def style-container
  {:flex "0 60%"
   :overflow-y "scroll"
   :overflow-x "hidden"
   :z-index "1000"
   :margin-right "5px"})

(defn cards-sidebar
  ([conn]
   (cards-sidebar conn style-container))
  ([conn style]
   (let [actors (db/get-actors-inside-bounds conn)
         actor-cards (doall (map-indexed actor-card (take 10 @actors)))]
     [:div#actors-sidebar {:style style}
      (if-not (empty? actor-cards)
        actor-cards
        [:h2 "No data"])])))
