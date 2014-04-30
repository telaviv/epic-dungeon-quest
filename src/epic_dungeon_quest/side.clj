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
