(ns epic-dungeon-quest.side_test
  (:require [clojure.test :refer :all]
            [epic-dungeon-quest.side :refer :all]
            [epic-dungeon-quest.core :as core]
            [clojure.pprint :refer :all]))

(deftest test-player-side
  (let [attack-value 10
        character (core/character-card)
        battle (battle-state :character character)]
    (testing "attacking just a character."
      (let [attacked-player (attack-player attack-value battle)
            attacked-player-card (get-in attacked-player [:player :character :card])]
        (is (= (:health attacked-player-card)
               (- (:health character) attack-value)))
        (clojure.pprint/pprint (update-in battle [:player] dissoc :player))
        (clojure.pprint/pprint (update-in attacked-player [:player] dissoc :player))
        (is (= (update-in battle [:player] dissoc :character)
               (update-in attacked-player [:player] dissoc :character)))))))

(deftest test-enemy-side
  (testing "attacking the first enemy"
    (let [attack-value 3
          enemy (core/spider-card)
          initial-state (battle-state :enemies [enemy])
          attacked-state (attack-enemy attack-value 0 initial-state)]
      (is (= (- (:health enemy) attack-value)
             (get-in attacked-state [:enemy :played 0 :card :health]))))))

(deftest test-select-enemy
  (let [selected (select-enemy demo-battle-state 1)]
    (testing "selecting an enemy with no current selections"
      (let [enemies (get-in selected [:enemy :played])]
        (is (:selected (nth enemies 1)))
        (is (not (:selected (nth enemies 0))))))
    (testing "a reselection cancels any other selections."
      (let [reselected (select-enemy selected 0)
            enemies (get-in reselected [:enemy :played])]
        (is (:selected (nth enemies 0)))
        (is (not (:selected (nth enemies 1))))))
    (testing "the zero argument select-enemy defaults to 0 index."
      (is (= (select-enemy demo-battle-state 0)
             (select-enemy demo-battle-state))))))


(deftest test-enemy-selected?
  (testing "no enemy selection should be false"
    (let [unselected-battle-state demo-battle-state]
      (is (not (enemy-selected? unselected-battle-state)))))
    (testing "when an enemy is selected we should be true"
      (let [selected-battle-state (select-enemy demo-battle-state 0)]
        (is (enemy-selected? selected-battle-state)))))
