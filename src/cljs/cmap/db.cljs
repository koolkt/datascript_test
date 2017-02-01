(ns cmap.db
  (:require
   [datascript.core :as d]
   [posh.reagent :as p]
   #_[cmap.util :as util :refer [tempid]]))

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
;; main-image-url name html-description email website

(defn inside-bounds? [{ne :ne sw :sw} lat lng]
  (and (and (< lat (:lat ne)) (> lat (:lat sw)))
       (and (< lng (:lng ne)) (> lng (:lng sw)))))

(def schema {:mapne/lat {:db/cardinality :db.cardinality/one}
             :mapne/lng {:db/cardinality :db.cardinality/one}
             :mapsw/lat {:db/cardinality :db.cardinality/one}
             :mapsw/lng {:db/cardinality :db.cardinality/one}})
(def conn (d/create-conn schema))

(defn get-actosrs-inside-map [conn]
  (p/q  '[:find ?lat ?lng
          :where
          [_ :mapne/lat ?nelat]
          [_ :mapsw/lat ?swlat]
          [_ :mapne/lng ?nelng]
          [_ :mapsw/lng ?swlng]
          [?e :long_initiative ?lng]
          [?e :lat_initiative ?lat]
          [(< ?lat ?nelat)]
          [(> ?lat ?swlat)]
          [(< ?lng ?nelng)]
          [(> ?lng ?swlng)]
          ]
        conn))

(defn get-actors [conn]
  (p/q  '[:find ?lat ?lng ?name
          :where
          [?e :long_initiative ?lng]
          [?e :nom_initiative ?name]
          [?e :lat_initiative ?lat]]
        conn))

(defn get-actors-data [conn]
  (p/q '[:find ?main-image-url ?name ?html-description ?email ?website ?lng ?swlng
         :where
         [_ :mapne/lat ?nelat]
         [_ :mapsw/lat ?swlat]
         [_ :mapne/lng ?nelng]
         [_ :mapsw/lng ?swlng]
         [?eid :long_initiative ?lng]
         [?eid :lat_initiative ?lat]
         [(< ?lat ?nelat)]
         [(> ?lat ?swlat)]
         [(< ?lng ?nelng)]
         [(> ?lng ?swlng)]
         [?eid :presa_image ?main-image-url]
         [?eid :nom_initiative ?name]
         [?eid :objet_initiative ?html-description]
         [?eid :email_initiative ?email]
         [?eid :website_initiative ?website]]
       conn))

(defn update-bounds [bounds conn]
  (p/transact!
    conn
    [[:db/add 123 :mapne/lat (:lat (:ne bounds))]
     [:db/add 123 :mapne/lng (:lng (:ne bounds))]
     [:db/add 123 :mapsw/lat (:lat (:sw bounds))]
     [:db/add 123 :mapsw/lng (:lng (:sw bounds))]]))

(.then
 (js/fetch "https://raw.githubusercontent.com/koolkt/datascript_test/master/new_cap_data.json")
 #(.then
   (.json %)
   (fn [json] (d/transact! conn (js->clj json :keywordize-keys true)))))

;; (defn populate! [conn]
;;   (d/transact! conn fixture))
