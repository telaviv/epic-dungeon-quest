(ns epic-dungeon-quest.side
  (:require [epic-dungeon-quest.core :as core]))

(defn player [character]
  {:character character})

(defn attack-player [attack-value side]
  (assoc side
    :character (core/deal-damage attack-value
                                 (:character side))))

(defn attack-enemy [attack-value index side]
  (assoc side index (core/deal-damage attack-value
                                      (nth side index))))

(defn select-enemy [battle-state i]
  (assoc-in battle-state [:enemy :played i :selected] true))

(defn is-enemy-selected [battle-state]
  nil)

(def demo-battle-state
  {:enemy {:played [{:selected false :card (core/spider-card)}
                    {:selected false :card (core/spider-card)}]}
   :player {:player (core/character-card)
            :weapons [{:selected false :card (core/wooden-sword-card)}]}})
