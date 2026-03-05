(ns docy.core
  (:require
   [docy.page :refer [docy-page docy-ns-page docy-fun-page]]
   [docy.markdown :refer [docy-markdown-page]]))

(def docy-routes-reitit-frontend
  [["/docy"
    ["" {:name :docy :view docy-page}]
    ["/ns/:nss" {:name :docy-ns :view docy-ns-page}]
    ["/ns/:nss/:fun" {:name :docy-fun :view docy-fun-page}]
    ["/md/:md" {:name :docy-md :view docy-markdown-page}]]])


