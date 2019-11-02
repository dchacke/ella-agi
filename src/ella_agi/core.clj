(ns ella-agi.core
  (:require [ella-agi.claim :as claim]
            [ella-agi.theory :as theory])
  (:gen-class))

; A set of claims including their lineages.
; A claim's lineage is list of previously
; invoked theories and claims. The whole atom
; is map that looks like this:
; {"1011" #{()} --> this claim has no origin
;  "010"  #{(a (b "101"))} --> this claim can
; be generated by invoking theory a with the
; claim resulting from invoking theory b with
; claim "101"
;  "0010" #{(c "10") (d "100")} --> this claim
; can be generated by invoking either of those
; two theories and the associated claims.
(def claims (atom {}))

; A set of theories that have been used to
; generate claims.
(def theories (atom #{}))

(defn store-claim
  "Safely stores the claim in the claims atom.
  Takes an optional path argument to store
  alongside the associated claim; if not given,
  will associate an empty list as path. The path
  is composed of theories and claims that can
  be used to generate the claim."
  ([claim]
   (store-claim claim '()))
  ([claim path]
   (if (contains? @claims claim)
     (swap! claims update claim conj path)
     (swap! claims assoc claim #{path}))))

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

(defn -main
  "I don't do a whole lot ... yet."
  []
  ; Start out with a random claim that we're going
  ; to pass as input to every theory.
  (let [claim (claim/generate-random-claim)]
    ; Store this claim.
    (store-claim claim)
    (->>
      ; Create a bunch of random theories.
      (repeatedly 100
        #(theory/generate-random-theory 10 theory/functions theory/terminals))
      ; Filter the created theories down to those that
      ; we can use.
      theory/usable-theories
      ; For each theory...
      (map
        (fn [theory]
          ; Store the theory.
          (swap! theories conj theory)
          ; Turn the theory into a runnable function and run it.
          (let [fun (theory/theory->fn theory)
                result (fun claim)]
            ; Store the function's result as a new claim
            ; and its lineage.
            (store-claim result (list theory claim)))))
      doall)
    (find-problems @claims)))
