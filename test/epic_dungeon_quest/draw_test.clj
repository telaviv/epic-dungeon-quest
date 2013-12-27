(ns epic-dungeon-quest.draw-test
  (:require [clojure.test :refer :all]
            [epic-dungeon-quest.draw :refer :all]))

(defn width [buffer] (count (first buffer)))
(defn height [buffer] (count buffer))

(deftest test-draw-card
  (testing "size should be 16x12"
    (let [card {:name "Pickachu"}
          buffer (draw-card card)]
      (is (= 16 (width buffer))
          (= 12 (height buffer))))))
