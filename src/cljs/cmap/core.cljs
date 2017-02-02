(ns cmap.core
  (:require
   [reagent.core :as r]
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

(def card-img-style {:width "300px"
                     :height "250px"
                     :object-fit "none"
                     :object-position "center"
                     :flex "0 0 auto"
                     :box-shadow
                     (str "0 2px 2px 0 rgba(0,0,0,0.14),"
                          "0 1px 5px 0 rgba(0,0,0,0.12),"
                          "0 3px 1px -2px rgba(0,0,0,0.2)")})

(defn actor-card [k [main-image-url name html-description email website]]
  [:div.actor-card {:style {:display "flex" :padding "5px" :margin "10px" :border-radius "5px"} :key k}
   (if (not-empty main-image-url)
     [:img {:style card-img-style
            :src (str "https://www.capoupascap.info/" main-image-url)}]
     [:div {:style {:width "300px" :height "250px" :background "grey" :flex "0 0 auto" :box-shadow
                     (str "0 2px 2px 0 rgba(0,0,0,0.14),"
                          "0 1px 5px 0 rgba(0,0,0,0.12),"
                          "0 3px 1px -2px rgba(0,0,0,0.2)")}}])
   [:div {:style {:display "flex" :flex-direction "column" :padding "5px 50px"}}
    [:h3 name]
    [:h4 (str "Email: " email)]
    [:h4 [:a {:href (str website)} (str "Site web " website)]]]])

(defn actors-sidebar [actors]
  (let [actor-cards (doall (map-indexed actor-card actors))]
    [:div#actors-sidebar {:style {:flex "0 60%" :overflow-y "scroll" :overflow-x "hidden" :z-index "1000" :margin-right "5px"}}
     (if-not (empty? actor-cards)
       actor-cards
       [:h2 "No data"])]))

(defn filters []
  [:div {:style {:padding "0px 50px" :box-shadow "0 2px 2px 0 rgba(0,0,0,0.14), 0 1px 5px 0 rgba(0,0,0,0.12), 0 3px 1px -2px rgba(0,0,0,0.2)" :flex "0 30%" :z-index "1000"}}
   [:h2 "Filters!"]])

(defn main-map []
  (let [data (db/get-actors-data db/conn)]
    [:div {:style {:display "flex" :align-items "stretch" :height "99.5vh"}}
     [actors-sidebar (take 10 @data)]
     [:div {:style {:display "flex" :flex-direction "column" :flex "0 40%" :justify-content "space-around"}}
      [filters]
      [l/open-map-outer db/conn]]]))

(r/render [main-map] (js/document.getElementById "app"))
