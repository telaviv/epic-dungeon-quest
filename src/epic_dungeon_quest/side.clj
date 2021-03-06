(ns epic-dungeon-quest.side
  (:require [epic-dungeon-quest.core :as core]))

(defn player [character]
  {:character character})

(defn attack-player [attack-value battle]
  (update-in battle
             [:player :character :card]
             (partial core/deal-damage attack-value)))

(defn attack-enemy [attack-value index battle]
  (update-in battle
             [:enemy :played index :card]
             (partial core/deal-damage attack-value)))

(defn- played-enemies [battle-state]
  (vec (get-in battle-state [:enemy :played])))

(defn- clear-enemy-selection [battle-state]
  (let [enemies (played-enemies battle-state)
        cleared-enemies (vec (map #(assoc % :selected false) enemies))]
    (assoc-in battle-state [:enemy :played] cleared-enemies)))

(defn select-enemy
  ([battle-state] (select-enemy battle-state 0))
  ([battle-state i]
     (assoc-in (clear-enemy-selection battle-state)
               [:enemy :played i :selected]
               true)))

(defn enemy-selected? [battle-state]
  (some :selected (played-enemies battle-state)))

(defn- selectify [card]
  {:selected false :card card})

(defn battle-state [& {:as opts}]
  (let [opts (merge {:character (core/character-card)
                     :enemies (vec (repeat 2 (core/spider-card)))
                     :weapons [(core/wooden-sword-card)]}
                    opts)]
    {:enemy {:played (vec (map selectify (:enemies opts)))}
     :player {:character (selectify (:character opts))
              :weapons (vec (map selectify (:weapons opts)))}}))

(def demo-battle-state
  (battle-state))
