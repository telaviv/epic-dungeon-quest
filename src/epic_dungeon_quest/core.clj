(ns epic-dungeon-quest.core)

(defn deal-damage [damage card]
  (assoc card :health (- (:health card) damage)))

(defn attack [target-type damage]
  (fn [card]
    (if (= card :target-type)
      target-type
      (deal-damage damage card))))

(defn passive-enemy [health]
  {:health 100, :type :monster})

(defn health [card]
  (:health card))

(defn spider-card []
  {:type :monster, :health 20})

(defn card-type [card]
  (:type card))
