(ns cmap.async
  (:require
   [goog.dom :as gdom]
   [goog.events :as events])
  (:import [goog.events EventType]))

(defn set-css-value [property value elem]
  (-> (.-style elem)
      (aset property value)))

(defn set-icon-css-values [id css]
  (let [elem (gdom/getElementByClass id)]
    (doseq [[property value] css]
      (set-css-value (name property) value elem))))

(defn mouse-on-marker [id]
  (set-icon-css-values id {:backgroundColor "red"}))

(defn mouse-off-marker [id]
  (set-icon-css-values id {:backgroundColor "skyblue"}))
