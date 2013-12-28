(ns epic-dungeon-quest.epic-dungeon-quest (:gen-class)
    (:require [epic-dungeon-quest.core :as epic]
              [epic-dungeon-quest.draw :as draw]
              [lanterna.screen :as ls]))

(defn -main []
  (let [screen (ls/get-screen :text)]
    (ls/start screen)
    (ls/put-sheet screen 0 0 (draw/draw-card (epic/spider-card)))
    (ls/redraw screen)
    (ls/get-key-blocking screen)
    (ls/stop screen)))
