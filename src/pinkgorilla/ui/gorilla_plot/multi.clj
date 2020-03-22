(ns pinkgorilla.ui.gorilla-plot.multi
  "multi-plots
     -multiple plots in one chart
     -multiple plots horizontal"
  (:require
   ;[pinkgorilla.ui.gorilla-plot.vega :refer [container]]
   [pinkgorilla.ui.gorilla-plot.util :as util :refer [uuid]]))

(defn container-lite
  "vega-lite container (version 4)"
  [];plot-size aspect-ratio]
  {:$schema "https://vega.github.io/schema/vega-lite/v4.json"
   ;:width   plot-size
   ;:height  (float (/ plot-size aspect-ratio))
   ;:padding {:top 10, :left 55, :bottom 40, :right 10}
   })


(defn convert-series
  "converts a series [vector of number]
   to a vega data spec with name"
  [series]
  (let [time (range (count series))]
    {:values (into [] (map
                       (fn [x y] {:x x :y y})
                       time series))}))

(defn build-series 
  "builds a vega lite plot-spec for a single series"
  [width last? m]
  ;(println "build-series " m)
  (let [;name (uuid)
        {:keys [data color title orient height #_width type scale]
         :or {color "#85C5A6"
              title nil
              orient "left"
              height nil
              ;width nil
              type "point"
              scale nil}} m]
    (merge
     (when height {:height height})
     (when width {:width width})

     {:mark {:type type :color color}
      :data (convert-series data)
      :encoding {:x {:field "x"
                     :type "quantitative"
                     :axis {:title  ""
                            :labels last?
                            }}
                 :y (merge
                     (when title
                       {:axis {:title title :titleColor "black" :orient orient}})
                     (when scale 
                       {:scale {:type scale}})
                     {:field "y"
                      :type "quantitative"})}})))


(defn- build-plot
  "builds a plot that can contain one or more series"
  [width last? series]
  (if (vector? series)
    (if (> (count series) 1)
      ; multiple series on one plot
      {:layer (into [] (map (partial build-plot width last?) series))
       :resolve {:scale {:y "independent"}}}
      ;list of series, but only one plot
      (build-series width last? (first series)))
    ; plot with only single series
    (build-series width last? series)))

(defn- build-plots
  "builds one or more plots.
   multiple plots are organized vertically"
  [width & plots]
  (if (= (count plots) 1)
    (build-plot width true (first plots)) ; no vertical plots
    ; vconcat multiple plots
    {:vconcat (conj 
               (into [] (map (partial build-plot width false) (butlast plots)))
               (build-plot width true (last plots)))
     ;:resolve {:scale {:y "independent"}}
     }))


(defn multi-plot
  "plots one or more plots (aligned vertically)
   each plot can contain one or more series

   example
     - first plot is single line, therefore plot params have to be set {}
    (as we do below), or first plot is wrapped with []

   series-plot map
      :data - vector of (length n)
      all other parameter r¡are optional:
      :color
      :orient   :left :right
      :title of the axis    
      :height
      :width

   (def a [1 2 4 3 2])
   (def b [-1 1 -2 3 0])
   (def c [6 5 1 7 5])

   (multi-plot {} 
      {:data c :orient :left :title \"C\" :color \"blue\" :height 20 :width 100} 
      [{:data a :orient :right :title \"A\" :color \"red\" :height 50 :width 100} 
      {:data b :orient :left :title \"B\" :height 50 :width 100}]))  "
  [& plots]
  (let [; optional parameter plot settings as map in first parameter
        [args plots] (if (map? (first plots))
                       [(first plots) (rest plots)]
                       [nil plots])
        ;_ (println "args: " args)
        ;_ (println "plots: " plots)
        {:keys [width]
         :or   {width    800}} args]
    (merge
     (container-lite); plot-size aspect-ratio)
     (apply (partial build-plots width) plots))))



(comment

  (def a [1 2 4 3 2])
  (def b [-1 1 -2 3 0])
  (def c [6 5 1 7 5])

  (convert-series a)
  (build-series 500 {:data a :orient "right"})

  (vector? {:data a :orient "right"})

  (build-plot 500 {:data a :orient "right"})
  (build-plot 500 [{:data a :orient "right" :title "A"}
                   {:data b :orient "left" :title "B"}])

  (build-plots 500 {:data a :orient "right" :title "A"}
               {:data b :orient "left" :title "B"})

  (multi-plot  [{:data a :orient "right" :title "A"}
                {:data b :orient "left" :title "B"}])

  (multi-plot {:i 88}
              [{:data c :orient "left" :title "C" :height 500 :scale "log"}]
              [{:data a :orient "right" :title "A"}
               {:data b :orient "left" :title "B"}])

  )

