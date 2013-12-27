(ns epic-dungeon-quest.draw)

(defn create-sheet [w h]
  (repeat h (repeat w " ")))

(defn draw-card [card]
  (create-sheet 16 12))
