(ns epic-dungeon-quest.draw-test
  (:require [clojure.test :refer :all]
            [epic-dungeon-quest.draw :refer :all]))

(defn width [buffer] (count (first buffer)))
(defn height [buffer] (count buffer))

(deftest test-draw-card
  (let [card {:name "Pikachu"}
        buffer (draw-card card)]
    (testing "size should be 16x12"
      (is (= 16 (width buffer))
          (= 12 (height buffer))))
    (testing "it should use unicode box corners."
      (is (= "╭" (get-in buffer [0 0])))
      (is (= "╮" (get-in buffer [0 15]))
      (is (= "╰" (get-in buffer [11 0]))
      (is (= "╯" (get-in buffer [11 15]))))))
    (testing "it should have unicode box sides."
      (let [left (for [y (range 1 11)] (get-in buffer [y 0]))
            right (for [y (range 1 11)] (get-in buffer [y 15]))
            top (for [x (range 1 15)] (get-in buffer [0 x]))
            bottom (for [x (range 1 15)] (get-in buffer [11 x]))]
        (is (apply = (conj left "│")))
        (is (apply = (conj right "│")))
        (is (apply = (conj top "─")))
        (is (apply = (conj bottom "─")))))
    (testing "it should draw it's name."
      (let [name-buffer (->> (nth buffer 2) rest drop-last (apply str))]
        (is (= "   Pikachu    " name-buffer))))))
