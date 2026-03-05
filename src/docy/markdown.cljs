(ns docy.markdown
  (:require
   [promesa.core :as p]
   [reagent.core :as r]
   [clj-service.http :refer [clj]]))

(def md-loaded-a (r/atom nil))

(def md-hiccup-a (r/atom [:div "loading.."]))

(defn md-ui [_md-name]
  (fn [md-name]
    (when (not (= md-name @md-loaded-a))
      (println "loading md for md-name: " md-name)
      (-> (clj {:timeout 2000}  'docy.core/markdown-doc md-name)
          (p/then (fn [hiccup]
                    (println "help-hiccup loaded: " hiccup)
                    (reset! md-hiccup-a hiccup)))
          (p/catch (fn [err]
                     (reset! md-hiccup-a [:div "error: could not load hiccup-md"]))))
      (reset! md-loaded-a md-name)
      nil)
    [:div {:style {:margin "1.25rem"}}
     ;[:h1 "docs for: " md-name]
     @md-hiccup-a]))

(defn docy-markdown-page [{:keys [_path-params] :as route}]
  (println "docy-markdown-page: " route)
  (fn [{:keys [path-params]}]
    (let [{:keys [md]} path-params]
      [:<>
       [:link {:href "/r/docy/prose.css"
               :rel "stylesheet"
               :type "text/css"}]
       [:div {:style {:background "#dcfce7"
                     :width "100%"
                     :height "100%"
                     :overflow "auto"
                     :boxSizing "border-box"}
             :class "prose"}
       [md-ui md]] 
       ]
      )))