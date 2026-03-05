(ns docy.core
  (:require
   [taoensso.timbre :refer [info warn error]]
   [commonmark-hiccup.core :as cmh]
   [extension :as ext]
   [modular.writer :refer [write-edn-private]]
   [docy.namespace :refer [build-namespaces ns-seq->dict]]
   [docy.snippet :refer [build-fn-lookup]]
   [docy.markdown :as md])
  (:import
   [org.commonmark.ext.gfm.tables TableBlock TableHead TableBody TableRow TableCell]
   [org.commonmark.ext.gfm.tables TablesExtension]))

(defn docy-data [{:keys [data-a md-dict-a]}]
  (assoc @data-a :md-list (keys @md-dict-a))
  )

(defn calculate-namespaces [{:keys [namespaces snippets]}]
  (let [ns-seq (build-namespaces namespaces)
        ns-dict (ns-seq->dict ns-seq)
        snippet-dict (build-fn-lookup snippets)]
    {:ns-dict ns-dict
     :snippet-dict snippet-dict}))

(defn build! [{:keys [data-a] :as state}]
  (let [data (calculate-namespaces state)]
    (swap! data-a merge data)))

(def ^:private markdown-config
  "Config for commonmark-hiccup with GFM tables support."
  (-> cmh/default-config
      (update-in [:parser :extensions] conj (TablesExtension/create))
      (update-in [:renderer :nodes] merge
                 {TableBlock  [:table :content]
                  TableHead   [:thead :content]
                  TableBody   [:tbody :content]
                  TableRow    [:tr :content]
                  TableCell   [:td :content]})))

(defn markdown-doc [{:keys [md-dict-a]} markdown-name]
  (let [md-entry (get @md-dict-a markdown-name)]
    (cond
      md-entry
      (cmh/markdown->hiccup markdown-config (md/slurp-markdown @md-dict-a markdown-name))
      :else
      [:div [:p "md not found: " markdown-name]])))

(defn start-docy
  "start docy service. 
   :namespaces a vector of namespaces (in symbolic form) to document
   :snippets a vector of snippets that are linked to namespace docs"
  [{:keys [exts namespaces snippets]
    :or {namespaces []
         snippets []}}]
  (info "starting docy .. ")
  (assert (vector? namespaces))
  (assert (vector? snippets))
  (write-edn-private :docy-namespaces namespaces)
  (write-edn-private :docy-snippets snippets)
  (let [md-dict (md/get-markdown-dict)
        _ (write-edn-private :docy-markdown md-dict)
        state  {:namespaces namespaces
                :snippets snippets
                :md-dict-a (atom md-dict)
                :data-a (atom {:ns-dict {}
                               :snippet-dict {}})}]
    (info "starting docy namespaces: " (count namespaces)
          " snippets: " (count snippets) " markdown: " (count (keys md-dict)))
    ;(add-discovered-namespaces this exts)
    (future
      (build! state)
      (info "docy ns build finished!")) 
    (info "docy running!")
    state))

