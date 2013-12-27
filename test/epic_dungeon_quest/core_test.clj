(ns epic-dungeon-quest.core-test
  (:require [clojure.test :refer :all]
            [epic-dungeon-quest.core :refer :all]))

(deftest attacking
  (testing "target type"
    (let [ability (attack :single-enemy 30)]
      (is (= :single-enemy (ability :target-type)))))
  (testing "executing the ability should remove health"
    (let [enemy (passive-enemy 100)
          ability (attack :single-enemy 30)
          attacked-enemy (ability enemy)]
      (is (= 70 (:health attacked-enemy))))))

(deftest spider
  (let [spider (spider-card)]
    (testing "should be of type monster"
      (is (= :monster (:type spider))))
    (testing "should have positive health"
      (let [spider-health (:health spider)]
        (is (number? spider-health))
        (is (< 0 spider-health))))))

(deftest wooden-sword
  (let [sword (wooden-sword-card)]
    (testing "is of type weapon"
      (is (= :weapon (:type sword))))
    (testing "it should have an ability"
      (let [sword-abilities (:abilities sword)]
        (is (sequential? sword-abilities))
        (is (= 1 (count sword-abilities)))))
    (testing "it's ability should be an attack."
      (let [[ability] (:abilities sword)]
        (is (= :single-enemy (ability :target-type)))
        (let [old-enemy (spider-card)
              hit-enemy (ability old-enemy)]
          (is (> (:health old-enemy) (:health hit-enemy))))))))
