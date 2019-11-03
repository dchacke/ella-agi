(ns ella-agi.problem-test
  (:require [clojure.test :refer :all]
            [ella-agi.problem :refer :all]))

(deftest problem-functions
  (testing "#find-problems"
    ; The first two pairs conflict
    (let [claims ["101" "001" "110" "010" "100"]]
      (is
        (= (find-problems claims) #{#{"101" "001"} #{"110" "010"}})
        "finds the conflicting claims"))))
