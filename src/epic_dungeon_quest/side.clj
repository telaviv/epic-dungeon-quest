(ns epic-dungeon-quest.side
  (:require [epic-dungeon-quest.core :as core]))

(defn player [character]
  {:character character})

(defn attack-player [attack-value battle]
  (update-in battle
             [:player :player :card]
             (partial core/deal-damage attack-value)))

(defn attack-enemy [attack-value index side]
  (let [original (get-in side [index :card])
        attacked (core/deal-damage attack-value original)]
    (assoc-in side [index :card] attacked)))

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

(def demo-battle-state
  {:enemy {:played [{:selected false :card (core/spider-card)}
                    {:selected false :card (core/spider-card)}]}
   :player {:player (core/character-card)
            :weapons [{:selected false :card (core/wooden-sword-card)}]}})

(defn battle-state [& {:as opts}]
  (let [opts (merge {:character (core/character-card)
                     :enemies (repeat 2 (core/spider-card))}
                    opts)]
    {:enemy {:played (vec (map #(assoc {:selected false} :card %)
                               (:enemies opts)))}
     :player {:player {:selected false :card (:character opts)}
              :weapons [{:selected false :card (core/wooden-sword-card)}]}}))
