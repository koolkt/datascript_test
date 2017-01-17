(ns cmap.core
  (:require [reagent.core :as r :refer [atom]]
            [posh.reagent :as p :refer [pull q posh!]]
            [datascript.core :as d]
            [cmap.db :as db]))

(enable-console-print!)

(db/populate! db/conn)
(posh! db/conn)

(defn construct-url [path]
  (str "https://www.capoupascap.info/" path))

(defn cap-actor [{:keys [main-image-url name html-description email website]}]
  [:div {:style {:display "flex"}}
   [:img {:style {:width "500px" :height "500px"} :src main-image-url}]
   [:div {:style {:display "flex" :flex-direction "column" :padding "0px 30px"}}
    [:h2 name]
    [:div {:style {:max-width "700px"} :dangerouslySetInnerHTML {:__html html-description}}]
    [:h4 (str "Email: " email)]
    [:h4 (str "Site web " website)]]])

(defn cap-actor-wrap []
  (let [actor @(pull db/conn '[:presa_image :nom_initiative :objet_initiative :email_initiative :website_initiative] 1)
        props {:main-image-url (construct-url (:presa_image actor))
               :name (:nom_initiative actor)
               :html-description (:objet_initiative actor)
               :email (:email_initiative actor)
               :website (:website_initiative actor)}]
    [cap-actor props]))
(defn ccc []
  [:div
   [cap-actor-wrap]])

(r/render [ccc] (js/document.getElementById "app"))
