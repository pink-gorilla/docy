(ns docy.snippet
  (:require
   [reval.frepl :refer [show-floating-repl show-floating-repl-namespace]]))

(defn snippet-item [{:keys [ns kernel label]}]
  [:a {:on-click #(show-floating-repl-namespace {:ns ns
                                                 :kernel kernel})}
   [:span.pr-1.text-blue-500 label]])

(defn snippet-list [snippets]
  (if snippets
    (into [:div]
          (map snippet-item snippets))
    [:div.text-blue-500 "no snippets"]))


