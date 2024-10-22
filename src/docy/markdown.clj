(ns docy.markdown
  (:require
   [clojure.edn :as edn]
   [resauce.core :as rs]
   [modular.resource.classpath :refer [url-name]]))

; https://github.com/weavejester/resauce

(defn add-name [url]
  [(url-name url) url])

(defn get-markdown-dict []
  (let [urls (rs/resource-dir "docy")
        md? (fn [[name _]]
              (let [matcher (re-matcher #".*\.md" name)]
                (re-find matcher)))]
    (->> urls
         (map add-name)
         (filter md?)
         (into {}))))

(defn slurp-markdown [markdown-dict name]
  (-> (get markdown-dict name)
      slurp))

(comment
  (-> (get-markdown-dict)
      (slurp-markdown "hello.md")))
