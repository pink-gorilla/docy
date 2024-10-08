(ns docy.namespace
  (:require
   [orchard.info :as orchard]
   [taoensso.timbre :refer [info warn error]]))

(defn docstring [symbol]
  (:doc (meta (resolve symbol))))

(defn describe-fun [nss fun]
  (let [data (orchard/info nss fun)]
    (merge
     data
     {:ns (str (:ns data))
      :name (str (:name data))})))

(defn describe-ns [nss]
  (require [nss])
  (let [symbols (keys (ns-publics nss))]
    {:ns (str nss)
     :names (->> symbols
                 (map #(describe-fun nss %))
                 (filter :doc)
                 (remove :deprecated)
                 (sort-by :name))}))

(defn build-namespaces [ns-symbol-seq]
  (info "building namespaces: " (count ns-symbol-seq))
  (let [r (->> (map describe-ns ns-symbol-seq)
               (sort-by :ns)
               ;(take 1)
               (into []))]
    ;(info "result: " r)
    r))

(defn ns-seq->dict [ns-seq]
  (->> ns-seq
       (map (fn [{:keys [ns names]}]
              [ns names]))
       (into {})))

(comment
  (orchard/info 'missionary.core 'amb>)
  (describe-fun 'missionary.core 'amb>)

  ; deprecated should be removed
  ; so amb and amb= should exist, but amb> should not
  (->> (describe-ns 'missionary.core)
       :names
       (map :name))

  (->> (describe-ns 'missionary.core)
       :names
       (filter #(= "amb" (:name %))))

  (describe-ns 'ta.calendar.core)

  (describe-ns 'ta.indicator.band)

  (require '[modular.system])

  (def d (modular.system/system :docy))

  d

  (build-namespaces d)

;
  )