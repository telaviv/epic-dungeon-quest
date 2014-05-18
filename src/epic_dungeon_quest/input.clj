(ns epic-dungeon-quest.input
  (:require [lanterna.screen :as ls]))

(defn create-input-stream [screen]
  (repeatedly #(ls/get-key-blocking screen)))
