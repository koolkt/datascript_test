(ns cmap.components.filters)

(def style-container {:padding "0px 50px"
                      :box-shadow "0 2px 2px 0 rgba(0,0,0,0.14), 0 1px 5px 0 rgba(0,0,0,0.12), 0 3px 1px -2px rgba(0,0,0,0.2)"
                      :flex "0 30%"
                      :z-index "1000"})
(def carts (partition 10 (range 1 21)))

(defn checkbox [cart]
  [:div {:key cart :style {:padding "2px"}}
   [:input {:type "checkbox" :name "cartier" :value (str cart)}] (str " " cart)])

(defn filters []
  [:div {:style style-container}
   [:h4 "Arrondisement"]
   [:div {:style {:overflow-y "scroll" :height "100px" :display "flex" :width "200px"}}
    [:div {:style {:flex "0 0 50%"}} (doall (map checkbox (first carts)))]
    [:div {:style {:flex "0 0 50%"}} (doall (map checkbox (second carts)))]]])
