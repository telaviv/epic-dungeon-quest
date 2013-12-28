(ns epic-dungeon-quest.draw)

(def card-width 16)
(def card-height 12)
(def card-left 0)
(def card-right (dec card-width))
(def card-top 0)
(def card-bottom (dec card-height))

(defn create-sheet [w h]
  (vec (repeat h (vec (repeat w " ")))))

(defn draw-column [sheet column char]
  (let [height (count sheet)
        positions (for [x (range height)] [x column])]
    (reduce #(assoc-in %1 %2 char) sheet positions)))

(defn draw-row [sheet row char]
  (let [width (count (sheet row))]
    (assoc sheet row (vec (repeat width char)))))

(defn draw-card [card]
  (-> (create-sheet card-width card-height)
      (draw-column card-left "│")
      (draw-column card-right "│")
      (draw-row card-top "─")
      (draw-row card-bottom "─")
      (assoc-in [card-top card-left] "╭")
      (assoc-in [card-top card-right] "╮")
      (assoc-in [card-bottom card-left] "╰")
      (assoc-in [card-bottom card-right] "╯")))
