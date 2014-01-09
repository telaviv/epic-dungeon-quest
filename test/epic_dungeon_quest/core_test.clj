(ns epic-dungeon-quest.core-test
  (:require [clojure.test :refer :all]
            [epic-dungeon-quest.core :refer :all]))

(deftest test-attacking
  (testing "target type"
    (let [ability (attack :single-enemy 30)]
      (is (= :single-enemy (ability :target-type)))))
  (testing "executing the ability should remove health"
    (let [enemy (passive-enemy 100)
          ability (attack :single-enemy 30)
          attacked-enemy (ability enemy)]
      (is (= 70 (:health attacked-enemy))))))

(deftest test-spider
  (let [spider (spider-card)]
    (testing "should be of type monster"
      (is (= :monster (:type spider))))
    (testing "it should have a name"
      (is (string? (:name spider))))
    (testing "should have positive health"
      (let [spider-health (:health spider)]
        (is (number? spider-health))
        (is (< 0 spider-health))))
    (testing "should have a positive attack."
      (let [spider-attack (:attack spider)]
        (is (number? spider-attack))
        (is (< 0 spider-attack))))))

(deftest test-wooden-sword
  (let [sword (wooden-sword-card)]
    (testing "is of type weapon"
      (is (= :weapon (:type sword))))
    (testing "it should have a name"
      (is (string? (:name sword))))
    (testing "should have a positive attack."
      (let [sword-attack (:attack sword)]
        (is (number? sword-attack))
        (is (< 0 sword-attack))))))

(deftest test-character
  (let [character (character-card)]
    (testing "is of type character"
      (is (= :character (:type character))))
    (testing "it should have a name"
      (is (string? (:name character))))
    (testing "should have positive health."
      (let [character-health (:health character)]
        (is (number? character-health))
        (is (< 0 character-health))))))
