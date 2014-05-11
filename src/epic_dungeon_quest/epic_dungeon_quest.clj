(ns epic-dungeon-quest.epic-dungeon-quest (:gen-class)
    (:require [epic-dungeon-quest.core :as epic]
              [epic-dungeon-quest.side :as side]
              [epic-dungeon-quest.draw :as draw]
              [epic-dungeon-quest.input :as input]
              [lanterna.screen :as ls]))

(defn -main []
  (let [screen (ls/get-screen :text)
        input-stream (input/create-input-stream screen)
        battle-state {:enemy {:played [{:selected false :card (epic/spider-card)}]}
                      :player {:player (epic/character-card)
                               :weapons [{:selected false :card (epic/wooden-sword-card)}]}}
        attacked-player (side/attack-player 3 battle-state)
        attacked-enemy (side/attack-enemy 5 0 attacked-player)]
    (ls/start screen)
    (ls/put-sheet screen 0 0 (draw/draw-battle battle-state))
    (ls/redraw screen)
    (nth input-stream 0)
    (ls/put-sheet screen 0 0 (draw/draw-battle attacked-player))
    (ls/redraw screen)
    (nth input-stream 1)
    (ls/put-sheet screen 0 0 (draw/draw-battle attacked-enemy))
    (ls/redraw screen)
    (nth input-stream 2)
    (ls/stop screen)))
