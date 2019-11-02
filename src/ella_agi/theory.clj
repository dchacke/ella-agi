(ns ella-agi.theory
  (:require [ella-agi.claim :as claim]
            [clojure.string :as s]))
  
; A theory is a function that takes zero or more
; claims and returns a new claim. Theories always
; return the same output for the same inputs, but
; otherwise they don't have to be pure.

(def functions
  "Functions that theories can use internally."
  '([and 2]
    [not 1]
    [if  3]
    [claim/generate-random-claim 0]
    [claim/rand-subs 1]
    ; This function is mainly responsible
    ; for the ability to detect problems
    ; between long claims. This suggests
    ; those problems are not "real".
    [claim/flip 1]
    [s/includes? 2]
    [s/starts-with? 2]
    [s/reverse 1]
    [s/replace 3]))

(def terminals
  "Terminals used to build up leaves of functions."
  '[claim])

(defn generate-random-theory 
  "Generates a random theory that takes 0 or 1 claims."
  [depth functions terminals]
  (cond
    (zero? depth) (rand-nth terminals)
    (< (rand) 0.2) (rand-nth terminals)
    :else
    (let [[fun arity] (rand-nth functions)]
      (cons fun
        (repeatedly arity
          #(generate-random-theory (dec depth) functions terminals))))))

; Theories are kept in list form only for debugging
; purposes. Turning them into invokable functions
; in different places leads to isses such as having
; to make sure the requisite namespaces are included.
; Eventually, they should be turned into invokable
; functions at the time they're created so that
; their closures work properly.
(defn theory->fn
  "Turns a datastructure representing a program
  into an executable function"
  [theory]
  ; TODO: For now, every theory takes a claim as input.
  ; In the future, some theories should take zero
  ; arguments.
  (eval (list 'fn '[claim] theory)))

(defn usable-theories
  "Filters the given theories down to those distinct
  ones that return strings and don't throw an exception."
  [theories]
  (->>
    theories
    distinct
    (filter
      (fn [theory]
        (let [fun (theory->fn theory)
              claim (claim/generate-random-claim)]
          (try
            (let [result (fun claim)]
              (claim/claim? result))
            (catch Exception e
              false)))))))
