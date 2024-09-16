(ns demo.notebook.movies
  (:require
   [clojure.pprint :refer [print-table]]))

(println "hello\nworld!")

(defn add3 [v]
  (+ 3 v))

(def movies
  [{:name "Terminator" :year 1984 :studio "Hemdale" :producer "Gale Anne Hurd"}
   {:name "Men in Black" :year 1997 :studio "Amblin Entertainment" :producer "Walter F. Parkes"}
   {:name "Matrix" :year 1999 :studio "Warner Bros" :producer "Joel Silver"}
   {:name "Dr. Strange" :year 2016 :studio "Marvel Studios" :producer "Scott Derrickson"}])

*ns*

(print-table movies)


(def more-movies
  (conj movies {:name "Harry Potter" :year 2020}))

(count more-movies)


