(ns epic-dungeon-quest.core-test
  (:require [clojure.test :refer :all]
            [epic-dungeon-quest.core :refer :all]))

(deftest attacking
  (testing "target type"
    (let [action (attack :single-enemy 30)]
      (is (= :single-enemy (action :target-type)))))
  (testing "executing the action should remove health"
    (let [enemy (passive-enemy 100)
          action (attack :single-enemy 30)
          attacked-enemy (action enemy)]
      (is (= 70 (health attacked-enemy))))))
