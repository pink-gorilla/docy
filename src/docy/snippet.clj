(ns docy.snippet)

(defn add-fn [snippet dict fn]
  (if (get dict fn)
    (update-in dict [fn] conj snippet)
    (assoc dict fn [snippet])))

(defn add-snippet [dict {:keys [fns] :as snippet}]
  (reduce (partial add-fn snippet) dict fns))

(defn build-fn-lookup [snippets]
  (reduce add-snippet {} snippets))

(comment

  (def demo-snippets
    [{:ns "demo.notebook.movies"
      :kernel :clj
      :label "print table (simple)"
      :fns ["tablecloth.api/aggregate"
            "tablecloth.api/columns"]}
     {:ns "demo.notebook.simple"
      :kernel :clj
      :label "range"
      :fns ["tablecloth.api/aggregate"
            "tablecloth.api/columns"
            "tablecloth.api/concat"
            "tablecloth.api/dataset"]}])

  (add-snippet {}
               {:ns "demo.notebook.movies"
                :kernel :clj
                :label "print table (simple)"
                :fns ["tablecloth.api/aggregate"
                      "tablecloth.api/columns"]})

  (def d (build-fn-lookup demo-snippets))

  (keys d)
;
  )
