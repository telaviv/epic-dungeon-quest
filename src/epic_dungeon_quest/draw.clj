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
(def card-attack-row 1)

(def player-enemy-offset 6)
(def player-row-offset 15)


(defn- create-sheet [w h]
  (vec (repeat h (vec (repeat w " ")))))

(defn- sheet-width [sheet] (count (first sheet)))
(defn- sheet-height [sheet] (count sheet))

(defn- draw-column [sheet column char]
  (let [height (sheet-height sheet)
        positions (for [x (range height)] [x column])]
    (reduce #(assoc-in %1 %2 char) sheet positions)))

(defn- draw-row [sheet row char]
  (let [width (sheet-width sheet)]
    (assoc sheet row (vec (repeat width char)))))

(defn- center-string [sheet string row]
  (let [width (sheet-width sheet)
        str-width (count string)
        row-array (sheet row)
        padding (- width str-width)
        left-padding (math/floor (/ padding 2))
        right-padding (- padding left-padding)]
    (assoc sheet row
      (vec (concat (take left-padding row-array)
                   (map str (vec string))
                   (take-last right-padding row-array))))))

(defn- right-align-string [sheet string row]
  (let [str-width (count string)
        row-array (sheet row)]
    (assoc sheet row
      (vec (concat (drop-last (+ 2 str-width) row-array)
                   (map str (vec string))
                   (take-last 2 row-array))))))

(defn- left-align-string [sheet string row]
  (let [str-width (count string)
        row-array (sheet row)]
    (assoc sheet row
      (vec (concat (take 1 row-array)
                   (map str (vec string))
                   (drop (+ 1 str-width) row-array))))))

(defn- draw-card-health [sheet card]
  (if-not (contains? card :health)
    sheet
    (right-align-string sheet (str (:health card) "☗") card-health-row)))

(defn- draw-card-attack [sheet card]
  (if-not (contains? card :attack)
    sheet
    (left-align-string sheet (str (:attack card) "⚔") card-attack-row)))

(defn- draw-card-attributes [sheet card]
  (-> sheet
      (center-string (:name card) card-name-row)
      (draw-card-attack card)
      (draw-card-health card)))

(defn draw-card [card]
  (-> (create-sheet card-width card-height)
      (draw-card-attributes card)
      (draw-column card-left "│")
      (draw-column card-right "│")
      (draw-row card-top "─")
      (draw-row card-bottom "─")
      (assoc-in [card-top card-left] "╭")
      (assoc-in [card-top card-right] "╮")
      (assoc-in [card-bottom card-left] "╰")
      (assoc-in [card-bottom card-right] "╯")))

(defn- blit-sheet [parent child x y]
  (let [positions (for [cx (range (sheet-width child))
                        cy (range (sheet-height child))
                        :let [px (+ cx x) py (+ cy y)]]
                    [cx cy px py])]
        (reduce (fn [sheet [cx cy px py]]
                  (assoc-in sheet [py px]
                            (get-in child [cy cx])))
                parent positions)))

(defn draw-played-enemies [side]
  (draw-card (:card (first side))))

(defn draw-player-side [side]
  (-> (create-sheet card-width (+ card-height player-row-offset))
      (blit-sheet (draw-card (first (:attack side))) 0 0)
      (blit-sheet (draw-card (:character side)) 0 player-row-offset)))

(defn draw-battle [battle-state]
  (let [enemy (draw-played-enemies (get-in battle-state [:enemy :played]))
        player (draw-player-side (:player battle-state))
        width (apply max (map sheet-width [enemy player]))
        height (+ player-enemy-offset
                  (sheet-height enemy)
                  (sheet-height player))]
    (-> (create-sheet width height)
        (blit-sheet enemy 0 0)
        (blit-sheet player 0 (- height (sheet-height player))))))
