(ns docy.core
  (:require
   [taoensso.timbre :refer [info warn error]]
   [commonmark-hiccup.core :refer [markdown->hiccup]]
   [extension :as ext]
   [modular.writer :refer [write-edn-private]]
   [clj-service.core :refer [expose-functions]]
   [docy.namespace :refer [build-namespaces ns-seq->dict]]
   [docy.snippet :refer [build-fn-lookup]]
   [docy.markdown :as md]))

(defn docy-data [{:keys [data-a]}]
  @data-a)

(defn calculate-namespaces [{:keys [namespaces snippets]}]
  (let [ns-seq (build-namespaces namespaces)
        ns-dict (ns-seq->dict ns-seq)
        snippet-dict (build-fn-lookup snippets)]
    {:ns-dict ns-dict
     :snippet-dict snippet-dict}))

(defn build! [{:keys [data-a] :as state}]
  (let [data (calculate-namespaces state)]
    (swap! data-a merge data)))

(defn markdown-doc [{:keys [md-dict]} markdown-name]
  (let [md-entry (get md-dict markdown-name)]
    (cond
      md-entry
      (markdown->hiccup (md/slurp-markdown md-dict markdown-name))
      :else
      [:div [:p "md not found: " markdown-name]])))

(defn start-docy
  "start docy service. 
   :namespaces a vector of namespaces (in symbolic form) to document
   :snippets a vector of snippets that are linked to namespace docs
   :clj clj-service-exposing module
   :role the user-role (if any) required to access the clj-service"
  [{:keys [exts clj role namespaces snippets]}]
  (info "starting docy .. ")
  (assert (vector? namespaces))
  (assert (vector? snippets))
  (write-edn-private :docy-namespaces namespaces)
  (write-edn-private :docy-sippets snippets)
  (let [md-dict (md/get-markdown-dict)
        state  {:namespaces namespaces
                :snippets snippets
                :md-dict md-dict
                :data-a (atom {:ns-dict {}
                               :snippet-dict {}
                               :md-list (keys md-dict)})}]
    (info "starting docy namespaces: " (count namespaces)
          " snippets: " (count snippets) " markdown: " (count (keys md-dict)))
    ;(add-discovered-namespaces this exts)
    (future
      (build! state)
      (info "docy ns build finished!"))
    (if clj
      (do
        (info "starting docy clj-services..")
        (expose-functions clj
                          {:name "docy"
                           :symbols ['docy.core/docy-data
                                     'docy.core/markdown-doc]
                           :permission role
                           :fixed-args [state]}))
      (warn "docy starting without clj-services, perhaps you want to pass :clj key"))
    (info "docy running!")
    state))

