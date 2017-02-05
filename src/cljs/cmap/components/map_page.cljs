(ns cmap.components.map-page
  (:require
   [cmap.db :refer [*db*]]
   [cmap.components.card-sidebar :refer [cards-sidebar]]
   [cmap.components.filters :refer [filters]]
   [cmap.components.open-map :refer [open-map-outer]]))

(defn main-map [params]
  [:div {:style {:display "flex" :align-items "stretch" :height "99.5vh"}}
   [:h2 (str params)]
   [cards-sidebar @*db*]
   [:div {:style {:display "flex" :flex-direction "column" :flex "0 40%" :justify-content "space-around"}}
    [filters]
    [open-map-outer @*db*]]])
