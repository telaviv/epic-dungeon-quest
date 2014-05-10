(ns epic-dungeon-quest.side_test
  (:require [clojure.test :refer :all]
            [epic-dungeon-quest.side :refer :all]
            [epic-dungeon-quest.core :as core]))

(deftest test-player-side
  (let [attack-value 10
        character (core/character-card)
        side {:character character :attack [(core/wooden-sword-card)]}]
    (testing "attacking just a character."
      (let [attacked-player-side (attack-player attack-value side)
            {attacked-player-card :character} attacked-player-side]
        (is (= (:health attacked-player-card)
               (- (:health character) attack-value))
        (is (= (dissoc side :character)
               (dissoc attacked-player-side :character))))))))

(deftest test-enemy-side
  (testing "attacking the first enemy"
    (let [attack-value 3
          enemy (core/spider-card)
          side [enemy]
          attacked-side (attack-enemy attack-value 0 side)]
      (is (= (- (:health enemy) attack-value)
             (:health (first attacked-side)))))))

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
        (is (not (:selected (nth enemies 1))))))))

(deftest test-enemy-selected?
  (testing "no enemy selection should be false"
    (let [unselected-battle-state demo-battle-state]
      (is (not (enemy-selected? unselected-battle-state)))))
    (testing "when an enemy is selected we should be true"
      (let [selected-battle-state (select-enemy demo-battle-state 0)]
        (is (enemy-selected? selected-battle-state)))))
