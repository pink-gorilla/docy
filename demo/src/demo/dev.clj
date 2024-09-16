(ns demo.dev
  (:require
   [clojure.string :as string]
   [modular.system]
   [docy.core :refer [docy-data]]))

  ;; first lets get the running reval  instance
(def s (modular.system/system :docy))



(def data 
  (docy-data s)
  )

data

(get-in data [:snippet-dict "tablecloth.api/concat"])
