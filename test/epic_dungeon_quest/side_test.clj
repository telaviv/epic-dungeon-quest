(ns epic-dungeon-quest.side_test
  (:require [clojure.test :refer :all]
            [epic-dungeon-quest.side :refer :all]
            [epic-dungeon-quest.core :as core]))

(deftest test-player-side
  (let [attack-value 10
        character (core/character-card)
        side (player character)]
    (testing "attacking just a character."
      (let [{attacked :character} (attack-player attack-value side)]
        (is (= (:health attacked)
               (- (:health character) attack-value)))))))
