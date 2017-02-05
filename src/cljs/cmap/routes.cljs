(ns cmap.routes
  (:import goog.History)
  (:require [mount.core :as m]
            [bidi.bidi :as bidi]
            [cmap.components.map-page :refer [main-map]]
            [cmap.db :refer [*db* get-route-info set-route!]]
            [goog.events :as events]
            [goog.history.EventType :as EventType]))

(def app-routes
  ["" {"" :index
       ["actor-" :actor-id] :actor-main
       true :not-found}])

(defn handle-route-change [conn url-path]
  (let [path (bidi/match-route app-routes url-path)]
    (set-route! path conn)))

(defn hook-browser-navigation! [conn]
  (doto (History.)
    (events/listen
     EventType/NAVIGATE
     (fn [event]
       (handle-route-change conn (.-token event))))
    (.setEnabled true)))

(defmulti component-map identity)
(defmethod component-map [:default ""] [] [:div "No page found"])
(defmethod component-map [:index ""] [[path params]] [main-map params])

(defn router [conn]
  (let [url-path (get-route-info conn)]
    (fn []
      [component-map @url-path])))

(defn init-routes [conn]
  (hook-browser-navigation! conn)
  app-routes)

(m/defstate routes :start (init-routes @*db*))
