(ns cljs-nbb-express
  (:require ["express$default" :as express]))

(def app (express))
(def port 8092)

(.get app "/"
      (fn foo [_req res]
        (.send res "Hello World!")))

(defn go [n c]
  (if (>= 0 n) c
      (let [[_ bigb]
            (last
             (take-while
              (fn [[_ b]] (<= b n))
              (iterate
               (memoize (fn [[a b]] [b (+ a b)]))
               [0 1])))]
        (recur (- n bigb) (+ 1 c)))))

(defn fibs-lt [n]
  (go n 0))

(.get app "/fibs/:n"
      (fn foo [req res]
        (->> (aget (aget req "params") "n")
             (fibs-lt)
             (str)
             (.send res))))

(.listen app port (fn [] (println "Example app listening on port" port)))

