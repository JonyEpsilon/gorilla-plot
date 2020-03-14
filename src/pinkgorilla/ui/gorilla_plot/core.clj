(ns pinkgorilla.ui.gorilla-plot.core
  "functions in .plot ns will only generate vega-specs.
   In .core ns we wrap the result of all functions into [:vega spec]"
  (:require
   [pinkgorilla.ui.gorilla-plot.plot :as p]
   [pinkgorilla.ui.gorilla-plot.multi :as m]))

(defn -vega! [spec]
  (with-meta [:vega spec] {:R true})) ; creates ^:R [:vega spec]

(defn -wrap-vega [f]
  (fn [& spec]
    (-vega!
     (apply f spec))))

(def plot (-wrap-vega p/plot))
(def list-plot (-wrap-vega p/list-plot))
(def bar-chart (-wrap-vega p/bar-chart))
(def histogram (-wrap-vega p/histogram))

(def timeseries-plot (-wrap-vega p/timeseries-plot))
(def multi-plot (-wrap-vega m/multi-plot))

(defn -unwrap [renderable]
  (second renderable)) ; [:vega data]

(defn compose
  [& plots]
  (let [plots-unwrapped (into [] (map -unwrap plots))]
    (->> plots-unwrapped
         (apply p/compose)
         (-vega!))))

