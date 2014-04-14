(ns epic-dungeon-quest.draw-test
  (:require [clojure.test :refer :all]
            [epic-dungeon-quest.draw :refer :all]
            [epic-dungeon-quest.core :refer :all]))

(defn width [buffer] (count (first buffer)))
(defn height [buffer] (count buffer))

(defn buffer-contains? [parent child x y]
  (every? (fn [[cx cy px py]]
            (= (get-in child [cy cx])
               (get-in parent [py px])))
          (for [cx (range (width child))
                cy (range (height child))
                :let [px (+ cx x) py (+ cy y)]]
            [cx cy px py])))

(deftest test-draw-card
  (let [card {:name "Pikachu" :health 100 :attack 20}
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
    (testing "it should draw the name."
      (let [name (->> (nth buffer 3) rest drop-last (apply str))]
        (is (= "   Pikachu    " name))))
    (testing "it should draw the health."
      (let [health (->> (nth buffer 1) drop-last (take-last 5) (apply str))]
        (is (= "100☗ " health))))
    (testing "if it doesn't have health, nothing should be drawn"
      (let [healthless (draw-card (dissoc card :health))
            health (->> (nth healthless 1) drop-last (take-last 5) (apply str))]
        (is (= "     " health))))
    (testing "it should draw the attack."
      (let [attack (->> (nth buffer 1) rest (take 3) (apply str))]
        (is (= "20⚔" attack))))
    (testing "if it doesn't have attack, nothing should be drawn."
      (let [attackless (draw-card (dissoc card :attack))
            attack (->> (nth attackless 1) rest (take 3) (apply str))]
        (is (= "   " attack))))))

(deftest test-draw-enemy-side
  (testing "a single card side should just look like the card."
    (let [enemy (spider-card)]
      (is (buffer-contains? (draw-enemy-side [enemy])
                            (draw-card enemy)
                            0 0)))))

(deftest test-draw-player-side
  (let [character (character-card)
        sword (wooden-sword-card)
        side {:character character :attack [sword]}]
    (testing "player is drawn on the second row."
      (is (buffer-contains? (draw-player-side side)
                            (draw-card character)
                            0 15)))
    (testing "attack cards are on the first row."
      (is (buffer-contains? (draw-player-side side)
                            (draw-card sword)
                            0 0)))))
