(ns epic-dungeon-quest.epic-dungeon-quest (:gen-class)
    (:require [epic-dungeon-quest.core :as epic]
              [epic-dungeon-quest.side :as side]
              [epic-dungeon-quest.draw :as draw]
              [lanterna.screen :as ls]))

(defn -main []
  (let [screen (ls/get-screen :text)
        enemy-side [(epic/spider-card)]
        player-side {:character (epic/character-card)
                     :attack [(epic/wooden-sword-card)]}
        attacked-player-side (side/attack-player 3 player-side)]
    (ls/start screen)
    (ls/put-sheet screen 0 0 (draw/draw-enemy-side enemy-side))
    (ls/put-sheet screen 0 18 (draw/draw-player-side player-side))
    (ls/redraw screen)
    (ls/get-key-blocking screen)
    (ls/put-sheet screen 0 0 (draw/draw-enemy-side enemy-side))
    (ls/put-sheet screen 0 18 (draw/draw-player-side attacked-player-side))
    (ls/redraw screen)
    (ls/get-key-blocking screen)
    (ls/stop screen)))
