(ns docy.util
  (:require
   [clojure.string :as str]
   [reitit.frontend.easy :as rfe]))

;; links

(defn goto [{:keys [page opts] :as to}]
  (println "going to: " to)
  (if opts 
    (rfe/navigate page opts)  
    (rfe/navigate page)))


(defn link [{:keys [to class style] :as opts
             :or {class "cursor-pointer hover:bg-red-700 m-1"
                  style {}}} & body]
  (let [goto-link (fn [& _]
                    (goto to))]
    (into [:a {:class class
               :style style
               :on-click goto-link}] body)))

(defn line-with-br [t]
  [:div
   [:span.font-mono.text-xs.whitespace-pre t]
   [:br]])

(defn text
  "Render text (as string) to html
   works with \\n (newlines)
   Needed because \\n is meaningless in html"
  [t]
  (let [lines (str/split t #"\n")]
    (into [:div] (map line-with-br lines))))

