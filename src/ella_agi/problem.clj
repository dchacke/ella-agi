(ns ella-agi.problem
  (:require [ella-agi.claim :as claim]))

(defn find-problems
  "Iterates over given claims to
  check for conflicts."
  [claims]
  (->>
    ; This can be optimized. Rather than
    ; checking each claim against all claims,
    ; check each pair only once.
    (for [claim-a claims
          claim-b claims
          :when (claim/conflict? claim-a claim-b)]
      #{claim-a claim-b})
    (into #{})))
