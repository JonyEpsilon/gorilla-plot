(ns pinkgorilla.ui.gorilla-plot.vega)

;; Constants for padding and offsets are chosen so
;; that simple axis titles are visible and do not
;; obstruct axis labels, for common ranges. A smarter
;; dynamic approach is probably possible, but for most
;; practical cases this is sufficient.

(defn container
  [plot-size aspect-ratio]
  {:$schema "https://vega.github.io/schema/vega/v5.json"
   :width   plot-size
   :height  (float (/ plot-size aspect-ratio))
   :padding {:top 10, :left 55, :bottom 40, :right 10}})

(defn data-from-list
  [data-key data]
  {:data [{:name   data-key
           :values (map (fn [[x y]] {:x x :y y}) data)}]})


(defn default-plot-axes
  [x-title y-title]
  {:axes [(merge {:orient "bottom" :scale "x"}
                 (when x-title {:title x-title :titleOffset 30}))
          (merge {:orient "left" :scale "y"}
                 (when y-title {:title y-title :titleOffset 45}))]})

;;; Scatter/list plots

(defn- domain-helper
  [data-key axis-plot-range axis]
  (if (= axis-plot-range :all) 
    {:data data-key, :field (str axis)} 
    axis-plot-range))

(defn default-list-plot-scales
  [data-key plot-range]
  {:scales [{:name   "x"
             :type   "linear"
             :range  "width"
             :zero   false
             :domain (domain-helper data-key (first plot-range) "x")}
            {:name   "y"
             :type   "linear"
             :range  "height"
             :nice   true
             :zero   false
             :domain (domain-helper data-key (second plot-range) "y")}]})


(defn timeseries-list-plot-scales
  [data-key plot-range]
  {:scales [{:name   "x"
             :type   "time"
             :range  "width"
             :zero   false
             :domain (domain-helper data-key (first plot-range) "x")}
            {:name   "y"
             :type   "linear"
             :range  "height"
             :nice   true
             :zero   false
             :domain (domain-helper data-key (second plot-range) "y")}]})






(defn list-plot-marks
  [data-key colour #_shape size opacity]
  {:marks [{:type        "symbol"
            :from       {:data data-key}
            :encode     {:enter  {:x           {:scale "x", :field "x"}
                                  :y           {:scale "y", :field "y"}
                                  :fill        {:value (or colour "steelblue")}
                                  :fillOpacity {:value opacity}}
                         :update {:shape #_shape "circle"
                                  :size        {:value size}
                                  :stroke      {:value "transparent"}}
                         :hover  {:size   {:value (* 3 size)}
                                  :stroke {:value "white"}}}}]})

(defn line-plot-marks
  [data-key color opacity]
  {:marks [{:type       "line"
            :from       {:data data-key}
            :encode     {:enter {:x             {:scale "x", :field "x"}
                                 :y             {:scale "y", :field "y"}
                                 :stroke        {:value (or color "#FF29D2")}
                                 :strokeWidth   {:value 2}
                                 :strokeOpacity {:value opacity}}}}]})


;;; Bar charts



(defn default-bar-chart-scales
  [data-key plot-range]
  {:scales [{:name   "x"
             :type   "band" ; "ordinal"
             :range  "width"
             :domain (domain-helper data-key (first plot-range) "x")}
            {:name   "y"
             :range  "height"
             :nice   true
             :domain (domain-helper data-key (second plot-range) "y")}]})


(defn bar-chart-marks
  [data-key color opacity]
  {:marks [{:type       "rect"
            :from       {:data data-key}
            :encode     {:enter {:x     {:scale "x", :field "x"}
                                 :width {:scale "x", :band 1, :offset -1}  ; :band true
                                 :y     {:scale "y", :field "y"}
                                 :y2    {:scale "y", :value 0}}
                         :update {:fill    {:value (or color "steelblue")}
                                  :opacity {:value opacity}}
                         :hover  {:fill {:value "#FF29D2"}}}}]})


;;; Histograms


(defn histogram-marks
  [data-key colour opacity fillOpacity]
  {:marks [{:type       "line"
            :from       {:data data-key}
            :encode     {:enter {:x             {:scale "x", :field "x"}
                                 :y             {:scale "y", :field "y"}
                                 :interpolate   {:value "step-before"}
                                 :fill          {:value (or colour "steelblue")}
                                 :fillOpacity   {:value fillOpacity}
                                 :stroke        {:value (or colour "steelblue")}
                                 :strokeWidth   {:value 2}
                                 :strokeOpacity {:value opacity}}}}]})