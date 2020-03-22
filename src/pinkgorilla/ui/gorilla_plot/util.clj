(ns pinkgorilla.ui.gorilla-plot.util)

(defn uuid [] (str (java.util.UUID/randomUUID)))

(defn count-in-range
  [data min max]
  (count (filter #(and (< % max) (>= % min)) data)))

(defn bin-counts
  [data min max bin-width]
  (let [bin-starts (range min max bin-width)]
    (map #(count-in-range data % (+ % bin-width)) bin-starts)))
