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

(deftest spider
  (let [spider (spider-card)]
    (testing "should be of type monster"
      (is (= :monster (card-type spider))))
    (testing "should have positive health"
      (let [spider-health (health spider)]
        (is (number? spider-health))
        (is (< 0 spider-health))))))
