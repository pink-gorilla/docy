(ns docy.core
  (:require
   [taoensso.timbre :refer [info warn error]]
   [extension :as ext]
   [clj-service.core :refer [expose-functions]]
   [docy.namespace :refer [build-namespaces ns-seq->dict]]
   [docy.snippet :refer [build-fn-lookup]]))

(defn docy-data [{:keys [namespaces snippets]}]
  (let [ns-seq (build-namespaces namespaces)
        ns-dict (ns-seq->dict ns-seq)
        snippet-dict (build-fn-lookup snippets)]
    {:ns-dict ns-dict
     :snippet-dict snippet-dict}))

(defn start-docy [{:keys [exts clj role namespaces snippets]}]
  (info "starting docy .. ")
  (assert (vector? namespaces))
  (assert (vector? snippets))
  (ext/write-target-webly :docy-namespaces namespaces)
  (ext/write-target-webly :docy-sippets snippets)
  (info "starting docy namespaces: " (count namespaces)
        " snippets: " (count snippets))
    ;(add-discovered-namespaces this exts)
  (if clj
    (do
      (info "starting docy clj-services..")
      (expose-functions clj
                        {:name "docy"
                         :symbols ['docy.core/docy-data]
                         :permission role
                         :fixed-args [{:namespaces namespaces
                                       :snippets snippets}]}))
    (warn "docy starting without clj-services, perhaps you want to pass :clj key"))
  (info "docy running!")
  {:namespaces namespaces
   :snippets snippets})

