(ns cmap.core
  (:require [reagent.core :as r]
            [posh.reagent :as p]
            [datascript.core :as d]
            [cmap.db :as db]
            [cmap.components.open-map :as l]))

(enable-console-print!)

;; (db/populate! db/conn)

(p/posh! db/conn)

(defn construct-url [path]
  (when (not (empty? path))
    (str "https://www.capoupascap.info/" path)))

(defn actor-card [k [main-image-url name html-description email website lng swlng]]
  [:div {:style {:display "flex" :flex-Wrap "wrap"} :key k}
   (if (not-empty main-image-url)
     [:img {:style {:width "500px" :height "350px"} :src (str "https://www.capoupascap.info/" main-image-url)}]
     [:div {:style {:width "500px" :height "350px" :background "grey"}}])
   [:div {:style {:display "flex" :flex-direction "column" :padding "5px 0px" :flex "0 100%"}}
    [:h3 name]
    [:h4 (str "Email: " email)]
    [:h4 (str lng " < " swlng)]
    [:h4 [:a {:href (str "http://" website)} (str "Site web " website)]]]])

(defn actors-sidebar [actors]
  [:div {:style {:flex "0 60%"}}
   (or (doall (map-indexed actor-card actors))
       [:h2 "No data"])])

(defn main-map []
  (let [data (db/get-actors-data db/conn)]
    [:div {:style {:display "flex"}}
     [actors-sidebar (take 10 @data)]
     [l/open-map-outer db/conn]]))

(r/render [main-map] (js/document.getElementById "app"))
