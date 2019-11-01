(ns ella-agi.theory-test
  (:require [clojure.test :refer :all]
            [ella-agi.theory :refer :all]))

(deftest theory-functions
  (let [theories
        '((and 1 2) ; does not return a claim
          "101101" ; returns a claim
          (str "01010011" "10") ; returns a claim
          "123" ; does not return a claim
          (inc "1"))] ; throws an exception
    (testing "#usable-theories"
      (is (= (count (usable-theories theories)) 2))
      "filters theories down to only those that
      return claims and do not throw exceptions")))
