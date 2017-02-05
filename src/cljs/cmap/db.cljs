(ns cmap.db
  (:require
   [datascript.core :as d]
   [posh.reagent :as p]
   [mount.core :as m]))

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

(def *test-data-url* "https://raw.githubusercontent.com/koolkt/datascript_test/master/new_cap_data.json")

(def schema {:mapne/lat {:db/cardinality :db.cardinality/one}
             :mapne/lng {:db/cardinality :db.cardinality/one}
             :mapsw/lat {:db/cardinality :db.cardinality/one}
             :mapsw/lng {:db/cardinality :db.cardinality/one}
             :route/handler {:db/cardinality :db.cardinality/one}
             :route/params {:db/cardinality :db.cardinality/one}})

(defn set-route! [route-info conn]
  (p/transact!
    conn
    [[:db/add 200 :route/handler (:handler route-info)]
     [:db/add 200 :route/params (or (:route-params route-info) "")]]))

(defn get-route-info [conn]
  (p/q  '[:find [?handler ?params]
          :where
          [?id :route/handler ?handler]
          [?id :route/params ?params]]
        conn))

(defn get-actors [conn]
  (p/q  '[:find ?lat ?lng ?name ?id
          :where
          [?id :long_initiative ?lng]
          [?id :nom_initiative ?name]
          [?id :lat_initiative ?lat]]
        conn))

(defn get-actors-inside-bounds [conn]
  (p/q '[:find ?main-image-url ?name ?html-description ?email ?website ?address ?id
         :where
         [_ :mapne/lat ?nelat]
         [_ :mapsw/lat ?swlat]
         [_ :mapne/lng ?nelng]
         [_ :mapsw/lng ?swlng]
         [?id :long_initiative ?lng]
         [?id :lat_initiative ?lat]
         [(< ?lat ?nelat)]
         [(> ?lat ?swlat)]
         [(< ?lng ?nelng)]
         [(> ?lng ?swlng)]
         [?id :presa_image ?main-image-url]
         [?id :nom_initiative ?name]
         [?id :objet_initiative ?html-description]
         [?id :email_initiative ?email]
         [?id :website_initiative ?website]
         [?id :adresse_initiative ?address]]
       conn))

(defn update-bounds [bounds conn]
  (p/transact!
    conn
    [[:db/add 123 :mapne/lat (:lat (:ne bounds))]
     [:db/add 123 :mapne/lng (:lng (:ne bounds))]
     [:db/add 123 :mapsw/lat (:lat (:sw bounds))]
     [:db/add 123 :mapsw/lng (:lng (:sw bounds))]]))

(defn populate-db! [conn data-url]
  (-> (js/fetch data-url)
      (.then (fn [res]
               (-> res
                   (.json)
                   (.then (fn [json]
                            (let [data (js->clj json :keywordize-keys true)]
                              (d/transact! conn data)))))))))

(defn init-db [data-url]
  (let [conn (d/create-conn schema)]
    (populate-db! conn data-url)
    (p/posh! conn)
    conn))

(m/defstate *db* :start (init-db *test-data-url*))
