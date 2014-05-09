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

(deftest test-is-enemy-selected
  (testing "no enemy selection"
    (let [battle-state demo-battle-state]
      (is (not (is-enemy-selected battle-state))))))
