(ns cmap.components.actor-card
  (:require
   [cmap.async :refer [mouse-on-marker mouse-off-marker]]))

(def root-url "//www.capoupascap.info/")

(defn construct-url [path]
  (when (not (empty? path))
    (str root-url path)))

(def card-img-style {:width "300px"
                     :height "250px"
                     :object-fit "none"
                     :object-position "center"
                     :flex "0 0 auto"
                     })

(def main-container-style {:display "flex"
                           :padding "5px"
                           :margin "10px"
                           :border-radius "5px"})

(def card-data-style {:display "flex"
                      :flex-direction "column"
                      :padding "5px 50px"})

(defn actor-card [k [main-image-url name html-description email website address id]]
  [:div.actor-card {:style main-container-style :key k :on-mouse-over #(mouse-on-marker id) :on-mouse-out #(mouse-off-marker id)}
   (if (not-empty main-image-url)
     [:img {:style card-img-style :src (str root-url main-image-url)}]
     [:div {:style card-img-style}])
   [:div {:style card-data-style}
    [:h3 name]
    [:h4 (str "Email: " email)]
    [:h4 (str "Address: " address)]
    [:h4 [:a {:href (str website)} (str "Site web " website)]]]])
