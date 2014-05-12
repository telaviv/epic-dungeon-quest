(ns epic-dungeon-quest.draw-test
  (:require [clojure.test :refer :all]
            [epic-dungeon-quest.draw :refer :all]
            [epic-dungeon-quest.core :refer :all]))

;; Here we enable a new "is" form for testing.
;; it allows you to ask "buffer-contains?" in an "is"
;; and appropriately prints out a cleaned up version of the buffers.

(defn width [buffer] (count (first buffer)))
(defn height [buffer] (count buffer))

(defn buffer-contains?- [parent child x y]
  (every? (fn [[cx cy px py]]
            (= (get-in child [cy cx])
               (get-in parent [py px])))
          (for [cx (range (width child))
                cy (range (height child))
                :let [px (+ cx x) py (+ cy y)]]
            [cx cy px py])))


(defn buffer-string [buffer]
  "Makes a beautified version of the buffer."
  (reduce (fn [string line]
            (str string "\n" (reduce str line)))
          ""
          buffer))

(defmethod assert-expr 'buffer-contains? [msg form]
  `(let [parent# ~(nth form 1)
         child# ~(nth form 2)
         x# ~(nth form 3)
         y# ~(nth form 4)]
     (let [result# (buffer-contains?- parent# child# x# y#)]
       (if result#
         (do-report {:type :pass, :message ~msg,
                     :expected parent#, :actual child#})
         (do-report {:type :buffer-contains-fail, :message ~msg,
                     :expected parent#, :actual child#}))
       result#)))

(defmethod report :buffer-contains-fail [m]
  (with-test-out
    (inc-report-counter :fail)
    (println "\nFAIL in" (testing-vars-str m))
    (when (seq *testing-contexts*) (println (testing-contexts-str)))
    (when-let [message (:message m)] (println message))
    (println "expected:" (buffer-string (:expected m)))
    (println "to contain:" (buffer-string (:actual m)))))


;; tests begin

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

(deftest test-draw-played-enemies
  (testing "a single card side should just look like the card."
    (let [enemy {:card (spider-card) :selected false}]
      (is (buffer-contains? (draw-played-enemies [enemy])
                            (draw-card (spider-card))
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

(deftest test-draw-battle
  (let [enemy {:played [{:card (spider-card) :selected false}]}
        player {:character (character-card) :attack [(wooden-sword-card)]}
        battle {:enemy enemy :player player}
        drawn-battle (draw-battle battle)]
    (testing "draws the played-enemies"
      (is (buffer-contains? drawn-battle
                            (draw-played-enemies (:played enemy))
                            0 0)))
    (testing "draws the player side"
      (is (buffer-contains? drawn-battle
                            (draw-player-side player)
                            0 18)))))
