(ns epic-dungeon-quest.draw)

(defn create-sheet [w h]
  (vec (repeat h (vec (repeat w " ")))))

(defn draw-card [card]
  (-> (create-sheet 16 12)
      (assoc-in [0 0] "╭")
      (assoc-in [0 15] "╮")
      (assoc-in [11 0] "╰")
      (assoc-in [11 15] "╯")))
