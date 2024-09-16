(ns demo.page.snippets
  (:require
   [reval.frepl :refer [show-floating-repl show-floating-repl-namespace]]
   [docy.snippet :refer [snippet-list]]))


(def snippets
  [{:ns "demo.notebook.movies"
    :kernel :clj
    :label "print table (simple)"}
   {:ns "demo.notebook.simple"
    :kernel :clj
    :label "range"}])


(defn snippets-page [{:keys [route-params query-params handler] :as route}]
  [:div
   [:h1 "I am a normal reagent page. But I can add a floating repl."]

   ;; just code
   [:a {:on-click #(show-floating-repl {:code "(+ 1 2 3)"})}
    [:p "show code (floating)"]]

   ;; code with a example result saved.
   [:a {:on-click #(show-floating-repl {:code "(+ 1 2 3)"
                                        :render-fn 'reval.viz.render-fn/reagent
                                        :data ^{:hiccup true}
                                        [:span {:style {:color "blue"}} "25"]})}
    [:p "show code (eval result)"]]

   ;; load a namespace
   [:a {:on-click #(show-floating-repl-namespace {:ns "notebook.study.movies"
                                                  :kernel :clj})}
    [:p "show code (namespace)"]]


   [:a {:on-click #(show-floating-repl-namespace {:ns "demo.notebook.highcharts"
                                                  :kernel :clj})}
    [:p "show code (highcharts)"]]

   [:h1 "now as a list:"]
   [snippet-list snippets]])
