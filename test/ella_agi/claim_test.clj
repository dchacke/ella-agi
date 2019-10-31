(ns ella-agi.claim-test
  (:require [clojure.test :refer :all]
            [ella-agi.claim :refer :all]))

(deftest claim-functions
  (let [claim (generate-random-claim)
        l (count claim)
        x "111"
        y "011"]
    (testing "#generate-random-claim"
      (is (>= l 2) "has length >= 2")
      (is (<= l 1000000000) "has length <= 1000000000")
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
        (is (= (conflict? x x) false) "returns false for a non-conflict")))))
