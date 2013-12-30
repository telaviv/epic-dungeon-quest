(ns epic-dungeon-quest.draw
  (:require [clojure.math.numeric-tower :as math]))

(def card-width 16)
(def card-height 12)
(def card-left 0)
(def card-right (dec card-width))
(def card-top 0)
(def card-bottom (dec card-height))
(def card-name-row 3)
(def card-health-row 1)

(defn create-sheet [w h]
  (vec (repeat h (vec (repeat w " ")))))

(defn draw-column [sheet column char]
  (let [height (count sheet)
        positions (for [x (range height)] [x column])]
    (reduce #(assoc-in %1 %2 char) sheet positions)))

(defn draw-row [sheet row char]
  (let [width (count (sheet row))]
    (assoc sheet row (vec (repeat width char)))))

(defn center-string [sheet string row]
  (let [sheet-width (count (sheet row))
        str-width (count string)
        row-array (sheet row)
        padding (- sheet-width str-width)
        left-padding (math/floor (/ padding 2))
        right-padding (- padding left-padding)]
    (assoc sheet row
      (vec (concat (take left-padding row-array)
                   (map str (vec string))
                   (take-last right-padding row-array))))))

(defn right-align-string [sheet string row]
  (let [str-width (count string)
        row-array (sheet row)]
    (assoc sheet row
      (vec (concat (drop-last (+ 2 str-width) row-array)
                   (map str (vec string))
                   (take-last 2 row-array))))))

(defn draw-card [card]
  (-> (create-sheet card-width card-height)
      (center-string (:name card) card-name-row)
      (right-align-string (str (:health card) "☗") card-health-row)
      (draw-column card-left "│")
      (draw-column card-right "│")
      (draw-row card-top "─")
      (draw-row card-bottom "─")
      (assoc-in [card-top card-left] "╭")
      (assoc-in [card-top card-right] "╮")
      (assoc-in [card-bottom card-left] "╰")
      (assoc-in [card-bottom card-right] "╯")))
