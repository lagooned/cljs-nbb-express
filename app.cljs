(ns cljs-nbb-express
  (:require ["express$default" :as express]))

(def app (express))
(def port 8092)

(.get app "/"
      (fn foo [_req res]
        (.send res "Hello World!")))

(defn go [n c]
  (if (>= 0 n) c
      (let [[_ b'] (->> [0 1]
                        (iterate (memoize (fn [[a b]] [b (+ a b)])))
                        (take-while (fn [[_ b]] (<= b n)))
                        (last))]
        (recur (- n b') (+ 1 c)))))

(defn fibs< [n]
  (go n 0))

(.get
 app "/fibs/:n"
 (fn foo [req res]
   (->> (aget (aget req "params") "n")
        (fibs<)
        (str)
        (.send res))))

(.listen app port (fn [] (println "Example app listening on port" port)))

