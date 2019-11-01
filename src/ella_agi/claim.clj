(ns ella-agi.claim)

; A claim is a bit string. The first bit represents
; the claim's truth value (1 = true, 0 = false).
; The rest of the bit string represents the claim's
; content. This content does not have any inherent
; meaning on its own. Its meaning is determined
; by theories that are created over time.
; The bit string can have an arbitrary length, as
; long as it's longer than 1 digit.

(defn generate-random-claim
  "Generates a random bit string."
  []
  (loop [result ""
         ; Mininum length of two
         length (+ (rand-int 100) 2)
         counter 0]
    (if (< counter length)
      (recur (str result (rand-int 2)) length (inc counter))
      result)))

(defn truth-bit
  "Returns a claim's truth bit as a string."
  [claim]
  (-> claim first str))

(defn ->boolean
  "Returns a boolean based on the claim's truth bit."
  [claim]
  (if (= (truth-bit claim) "1")
    true
    false))

(def true?
  "Alias for ->boolean"
  ->boolean)

(defn false?
  "Inverts ->boolean."
  [claim]
  (not (->boolean claim)))

(defn content
  "Returns the claim's content as a string."
  [claim]
  (->>
    claim
    rest
    (apply str)))

(defn conflict?
  "Returns true when the two claims' contents are the
  same while their truth bits differ, false otherwise."
  [claim-1 claim-2]
  (and
    (= (content claim-1) (content claim-2))
    (not= (truth-bit claim-1) (truth-bit claim-2))))
