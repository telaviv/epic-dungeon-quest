(ns epic-dungeon-quest.epic-dungeon-quest (:gen-class)
    (:require [epic-dungeon-quest.core :as epic]
              [epic-dungeon-quest.side :as side]
              [epic-dungeon-quest.draw :as draw]
              [epic-dungeon-quest.input :as input]
              [lanterna.screen :as ls]))

(defn -main []
  (let [screen (ls/get-screen :text)
        input-stream (input/create-input-stream screen)
        enemy-side [(epic/spider-card)]
        player-side {:character (epic/character-card)
                     :attack [(epic/wooden-sword-card)]}
        attacked-player-side (side/attack-player 3 player-side)
        attacked-enemy-side (side/attack-enemy 5 0 enemy-side)]
    (ls/start screen)
    (ls/put-sheet screen 0 0 (draw/draw-enemy-side enemy-side))
    (ls/put-sheet screen 0 18 (draw/draw-player-side player-side))
    (ls/redraw screen)
    (nth input-stream 0)
    (ls/put-sheet screen 0 0 (draw/draw-enemy-side enemy-side))
    (ls/put-sheet screen 0 18 (draw/draw-player-side attacked-player-side))
    (ls/redraw screen)
    (nth input-stream 1)
    (ls/put-sheet screen 0 0 (draw/draw-enemy-side attacked-enemy-side))
    (ls/put-sheet screen 0 18 (draw/draw-player-side attacked-player-side))
    (ls/redraw screen)
    (nth input-stream 2)
    (ls/stop screen)))
