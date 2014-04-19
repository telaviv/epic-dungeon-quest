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
