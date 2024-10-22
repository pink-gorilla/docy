(ns docy.markdown
  (:require
   [promesa.core :as p]
   [reagent.core :as r]
   [goldly.service.core :refer [clj]]))

(def md-loaded-a (r/atom nil))

(def md-hiccup-a (r/atom [:div "loading.."]))

(defn md-ui [md-name]
  (fn [md-name]
    (when (not (= md-name @md-loaded-a))
      (println "loading md for md-name: " md-name)
      (-> (clj {:timeout 2000}  'docy.core/markdown-doc md-name)
          (p/then (fn [hiccup]
                    (println "help-hiccup loaded: " hiccup)
                    (reset! md-hiccup-a hiccup)))
          (p/catch (fn [err]
                     (reset! md-hiccup-a [:div "erorr: could not load hiccup-md"]))))
      (reset! md-loaded-a md-name)
      nil)
    [:div
     [:h1 "docs for: " md-name]
     @md-hiccup-a]))

(defn docy-markdown-page [{:keys [route-params] :as route}]
  (fn [{:keys [route-params] :as route}]
    (let [{:keys [md]} route-params]
      [:div.p-5
       [:h1.text-xxl.text-blue-800 "MD: " (str md)]
       [md-ui md]])))