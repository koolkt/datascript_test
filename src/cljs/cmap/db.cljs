(ns cmap.db
  (:require [datascript.core :as d]
            [cmap.util :as util :refer [tempid]]
            [cheshire.core :refer :all]))

;; (def schema {:picto_typologie
;;              :objectID
;;              :rs_initiative
;;              :tel_initiative
;;              :credit_picto
;;              :mail_user
;;              :nom_quartier
;;              :modification_date
;;              :presa_image
;;              :custom_location
;;              :website_initiative
;;              :objet_initiative
;;              :titre_typologie
;;              :lieu_initiative
;;              :id_typologie
;;              :info_initiative
;;              :id_quartier
;;              :creation_date
;;              :email_initiative
;;              :sous_typo_initiative
;;              :long_initiative
;;              :orga_initiative
;;              :nom_initiative
;;              :admin_notes
;;              :lat_initiative
;;              :contact_initiative
;;              :statut_initiative
;;              :adresse_initiative
;;              :arrond_initiative
;;              :horaires_initiative
;;              })

(def schema {:cap/actors {:db/cardinality :db.cardinality/many}})
(def conn (d/create-conn schema))

(def fixture (parse-string (slurp "/home/koolkat/Projects/clojure/clojurescript/cmap/new_cap_data.json") true))

(defn populate! [conn]
  (d/transact! conn fixture))
