(ns cmap.components.filters)

(def style-container {:padding "0px 50px"
                      :box-shadow "0 2px 2px 0 rgba(0,0,0,0.14), 0 1px 5px 0 rgba(0,0,0,0.12), 0 3px 1px -2px rgba(0,0,0,0.2)"
                      :flex "0 30%"
                      :z-index "1000"})
(defn filters []
  [:div {:style style-container}
   [:h2 "Filters!"]])
