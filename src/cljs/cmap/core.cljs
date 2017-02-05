(ns cmap.core
  (:require
   [reagent.core :as r]
   [cmap.routes :as routes]
   [cmap.routes :refer [router]]
   [cmap.db :refer [*db*]]
   [mount.core :as m]))

(enable-console-print!)
(m/start)

(r/render [router @*db*] (js/document.getElementById "app"))
