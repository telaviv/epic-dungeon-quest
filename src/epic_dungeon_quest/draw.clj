(ns epic-dungeon-quest.draw)

(def card-width 16)
(def card-height 12)

(defn create-sheet [w h]
  (vec (repeat h (vec (repeat w " ")))))

(defn draw-column [sheet column char]
  (let [positions (for [x (range card-height)] [x column])]
    (reduce #(assoc-in %1 %2 char) sheet positions)))

(defn draw-card [card]
  (-> (create-sheet card-width card-height)
      (draw-column 0 "│")
      (assoc-in [0 0] "╭")
      (assoc-in [0 15] "╮")
      (assoc-in [11 0] "╰")
      (assoc-in [11 15] "╯")))
