(ns ella-agi.claim-test
  (:require [clojure.test :refer :all]
            [ella-agi.claim :refer :all]
            [clojure.string :as s]))

(deftest claim-functions
  (let [claim (generate-random-claim)
        l (count claim)
        x "111"
        y "011"]
    (testing "#generate-random-claim"
      (is (>= l 2) "has length >= 2")
      (is (<= l 100) "has length <= 1000000000")
      (is (every?
            (fn [char]
              (or
                (= char \0)
                (= char \1)))
            claim)
          "is a binary string"))

    (testing "#truth-bit"
      (is (= (truth-bit x) "1") (str "truth bit of \"" x "\" is \"1\""))
      (is (= (truth-bit y) "0") (str "truth bit of \"" y "\" is \"0\"")))

    (testing "#->boolean"
      (is (= (->boolean x) true) (str "\"" x "\" has a truthy truth bit"))
      (is (= (->boolean y) false) (str "\"" y "\" has a falsey truth bit")))

    (testing "#true?"
      (is (= (true? x) (->boolean x)) "is an alias for ->boolean"))

    (testing "#false?"
      (is (= (false? x) false) "returns false for a truthy truth bit")
      (is (= (false? y) true) "returns true for a falsey truth bit"))

    (testing "#content"
      (let [x "110"]
        (is (= (content x) "10") (str "the content of \"" x "\" is \"10\""))))

    (testing "#conflict?"
      (let [z "110"]
        (is (= (conflict? x y) true) "returns true for a conflict")
        (is (= (conflict? x z) false) "returns false for a non-conflict")
        (is (= (conflict? x x) false) "returns false for a non-conflict")))

    (testing "#rand-subs"
      (let [x "1100011"]
        (is (s/includes? x (rand-subs x)) "returns a random substring of a claim")))

    (testing "#flip"
      (is (= (flip x) y))
      (is (= (flip y) x)))

    (testing "#claim?"
      (is (= (claim? "1011") true) "is true for a valid true claim")
      (is (= (claim? "0011") true) "is true for a valid false claim")
      (is (= (claim? 123) false) "is false for numbers")
      (is (= (claim? []) false) "is false for vectors")
      (is (= (claim? {}) false) "is false for maps")
      (is (= (claim? '()) false) "is false for lists")
      (is (= (claim? #{}) false) "is false for sets")
      (is (= (claim? "1asdjklh") false) "is false for random strings")
      (is (= (claim? "123") false) "is false for stringified numbers"))))
