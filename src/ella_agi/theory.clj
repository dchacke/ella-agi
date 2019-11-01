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

(defn theory->fn
  "Turns a datastructure representing a program
  into an executable function"
  [theory]
  (eval (list 'fn '[claim] theory)))

(defn usable-theories
  "Filters the given theories down to those that
  return strings and don't throw an exception."
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
              (and
                (= (type result) java.lang.String)
                (not= claim result)))
            (catch Exception e
              false)))))))
