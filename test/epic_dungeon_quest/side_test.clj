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
  (testing "selecting an enemy with no current selections"
    (let [selected (select-enemy demo-battle-state 1)
          enemies (get-in selected [:enemy :played])]
      (is (:selected (nth enemies 1)))
      (is (not (:selected (nth enemies 0)))))))

(deftest test-is-enemy-selected
  (let [battle-state demo-battle-state]
    (testing "no enemy selection"
      (is (not (is-enemy-selected battle-state))))))
