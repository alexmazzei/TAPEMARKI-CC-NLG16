(ns clj_mark.core
  (:require [clojure.math.combinatorics :as combo])
  (:require [clojure.set :as clj-set])
  (:gen-class))


(defn fragment->units
  ""
  [fragment]
  (fragment
   {:A1 [:A11 :A12 :A13] :A2 [:A21 :A22] :A3 [:A31 :A32 :A33] :A4 [:A41 :A42] :A5 [:A51 :A52] :A6 [:A61 :A62 :A63]
    :B1 [:B11 :B12 :B13] :B2 [:B21 :B22] :B3 [:B31 :B32 :B33] :B4 [:B41 :B42 :B43] :B5 [:B51 :B52]
    :C1 [:C11 :C12 :C13] :C2 [:C21 :C22] :C3 [:C31 :C32 :C33] :C4 [:C41 :C42 :C43]}))


(defn unit->words
  ""
  [unit]
  (unit
   {:A11 "l'accecante"  :A12 "globo" :A13 "di fuoco"
    :A21 "si espande"   :A22 "rapidamente"
    :A31 "trenta volte" :A32 "piú luminoso" :A33 "del sole"
    :A41 "quando raggiunge" :A42 "la stratosfera"
    :A51 "la sommità" :A52 "della nuvola"
    :A61 "assume" :A62 "la ben nota forma" :A63 "di fungo"
    :B11 "la testa" :B12 "premuta" :B13 "sulla spalla"
    :B21 "i capelli" :B22 "tra le labbra"
    :B31 "giacquero" :B32 "immobili" :B33 "senza parlare"
    :B41 "finché non mosse" :B42 "le dita" :B43 "lentamente"
    :B51 "cercando" :B52 "di afferare"
    :C11 "mentre la moltitudine" :C12 "delle cose" :C13 "accade"
    :C21 "io contemplo" :C22 "il loro ritorno"
    :C31 "malgrado" :C32 "che le cose" :C33 "fioriscano"
    :C41 "esse tornano" :C42 "tutte" :C43 "alla loro radice"}))



(defn mark-sequence
  ""
  [sequence]
  (let
    [vet (vec (map unit->words (subvec (vec (apply concat (map fragment->units sequence))) 0 24)))]
    ;;(println vet)
    (concat
     (subvec vet 0 4) "\n"
     (subvec vet  4 8) "\n"
     (subvec vet 8 12) "\n"
     (subvec vet 12 16) "\n"
     (subvec vet 16 20) "\n"
     (subvec vet 20 24) "\n")))


(defn units->numbers
  [unit]
  (let [units-numbers {:A1 1 :A2 2 :A3 3 :A4 4 :A5 5 :A6 6 :B1 7 :B2 8 :B3 9 :B4 10 :B5 11 :C1 12 :C2 13 :C3 14 :C4 15}]
    (unit units-numbers)))



(defn count-units
  ""
  [sequence]
  (count (apply concat (map fragment->units sequence))))


(defn same-poetry?
  "Says if two framments belongs to the same poetry."
  [frag1 frag2]
  (let
      [A #{:A1 :A2 :A3 :A4 :A5 :A6}
       B #{:B1 :B2 :B3 :B4 :B5}
       C #{:C1 :C2 :C3 :C4}]
    (if
        (or
         (and (contains? A frag1) (contains? A frag2))
         (and (contains? B frag1) (contains? B frag2))
         (and (contains? C frag1) (contains? C frag2)))
      true
      false)))

(defn all-transitions
  ""
  []
  (let
      [
       constraints-out {:A1 #{2 3} :A2 #{3 4} :A3 #{2 4} :A4 #{1 2} :A5 #{2 3} :A6 #{3 4} :B1 #{2 4} :B2 #{2 4} :B3 #{2 3} :B4 #{1 3} :B5 #{1 2} :C1 #{1 2} :C2 #{3 4} :C3 #{2 3} :C4 #{1 4}}
       constraints-in  [#{} #{:A1 :A2 :A5 :B1 :B2 :C1 :C3}  #{:A2 :A3 :A6 :B3 :C1 :C2 :C3 :C4}  #{:A3 :A5 :B3 :B4 :B5 :C2 :C4}  #{:A1 :A4 :A6 :B1 :B2 :B4 :B5}] ]
    (map
     (fn [x]
       (str (units->numbers x) "->"
                (sort
                 (reduce clj-set/union
                         (map
                          (fn [y]  (set (map units->numbers (remove #(same-poetry? x %) (nth constraints-in y)))))
                          (x constraints-out))))))
     (sort (keys constraints-out)))))

(defn legal-concatenation?
  "Given two fragments says if they can be concatenated on the base of their in-out constraints."
  [frag-in frag-out]
  (let
      [
       constraints-out {:A1 #{2 3} :A2 #{3 4} :A3 #{2 4} :A4 #{1 2} :A5 #{2 3} :A6 #{3 4} :B1 #{2 4} :B2 #{2 4} :B3 #{2 3} :B4 #{1 3} :B5 #{1 2} :C1 #{1 2} :C2 #{3 4} :C3 #{2 3} :C4 #{1 4}}
       constraints-in  [#{} #{:A1 :A2 :A5 :B1 :B2 :C1 :C3}  #{:A2 :A3 :A6 :B3 :C1 :C2 :C3 :C4}  #{:A3 :A5 :B3 :B4 :B5 :C2 :C4}  #{:A1 :A4 :A6 :B1 :B2 :B4 :B5}] ]
    ;; (println (frag-in constraints-out))
    ;; (println (reduce clojure.set/union
    ;;                  (map (fn [x] (nth constraints-in x))
    ;;                       (frag-in constraints-out))))
    (if
        (and (not (same-poetry? frag-in frag-out))
             (contains? (reduce clj-set/union
                                (map (fn [x] (nth constraints-in x))
                                     (frag-in constraints-out)))
                        frag-out))
      true
      false) ))

(defn test-sequence-tape-mark-rules?
  "Given a sequence of fragments, test if they satisfies the rules"
  [sequence-frags]
  (if (= (count sequence-frags) 1)
    true
    (and (legal-concatenation? (first sequence-frags) (second sequence-frags))
          (recur (rest sequence-frags)))))

(defn combinations-10
  "This function returns all the possible ten combination of the fragments."
  [frammenti]
  ;[poesie [:A1 :A2 :A3 :A4 :A5 :A6 :B1 :B2 :B3 :B4 :B5 :C1 :C2 :C3 :C4 ]]
       ;(combo/combinations (shuffle poesie) 10))) ;;Ballestrini
                                        ;(map combo/permutations (combo/combinations poesie 5))))
  (combo/combinations frammenti 10))

(defn permutations
  ""
  [frammenti]
  (combo/permutations frammenti))


(defn check-number
  ""
  []
  (def sequences-number 0)
  (doseq  [sequenza-x (combinations-10
                       ;;[:A4 :C3 :B3 :C4 :A6 :B2 :B4 :A5 :C1 :A2 :C2 :B1 :A1 :B5 :A3]
                       [:B5 :C2 :B4 :A5 :C1 :B3 :A6 :C4 :A2 :B1 :A4 :B2 :A1 :C3 :A3])]
    (if (test-sequence-tape-mark-rules? sequenza-x)
      (do
        (def sequences-number (+ 1 sequences-number))
        (println sequenza-x))))
  (println "Total number=" sequences-number))

(defn random-initial-legal-sequences
  ""
  []
  (doseq  [sequenza-x (combinations-10
                       ;;[:A4 :C3 :B3 :C4 :A6 :B2 :B4 :A5 :C1 :A2 :C2 :B1 :A1 :B5 :A3])]
                       (shuffle [:A1 :A2 :A3 :A4 :A5 :A6 :B1 :B2 :B3 :B4 :B5 :C1 :C2 :C3 :C4 ]))]
    (if (test-sequence-tape-mark-rules? sequenza-x)
      (println
       (mark-sequence sequenza-x)
       "units:" (count-units sequenza-x)))))

(defn legal-sequences
  ""
  []
  (def tot-sequences-number 0)
  (doseq  [sequenza-x
           (combinations-10 [:A1 :A2 :A3 :A4 :A5 :A6 :B1 :B2 :B3 :B4 :B5 :C1 :C2 :C3 :C4 ])]
           ;;(combo/combinations [:A1 :A2 :B1 :B2 :C1 :C2] 4)]
    ;;(println ">>ANALIZZO PERMUTAZIONI DI:" sequenza-x)
    (def sequences-number 0)
    (doseq [sequenza-y (permutations sequenza-x)]
      (if (test-sequence-tape-mark-rules? sequenza-y);; ???(> (count-units sequenza-x) 24))
        (do
          ;;(println sequenza-y)
          ;; (spit "all-legal-sequences.txt" (print-str sequenza-y) :append true)
          ;; (spit "all-legal-sequences.txt" "\n" :append true))))
          (def sequences-number (+ 1 sequences-number)))))
    (spit "all-legal-sequences.txt" (str "Analizzo permutazioni di " (print-str sequenza-x) " Numero sequenze corrette >>" sequences-number "<<\n") :append true)
    (def tot-sequences-number (+ tot-sequences-number sequences-number)) )
  (spit "all-legal-sequences.txt" (str  "Numero totale sequenze corrette <<" tot-sequences-number ">>\n") :append true))



(defn generate-random-poetry
  ""
  []
  (def correct-sequences [])
  (def final-sequences [])
  (while (<= (count correct-sequences) 6)
    (doseq  [sequenza-x (combinations-10
                         (shuffle [:A1 :A2 :A3 :A4 :A5 :A6 :B1 :B2 :B3 :B4 :B5 :C1 :C2 :C3 :C4]))]
      (if (and
           (test-sequence-tape-mark-rules? sequenza-x)
           (not-any? (fn [x] (= (first x) (first sequenza-x)));;#(= sequenza-x %)
                     correct-sequences))
        (def correct-sequences (cons sequenza-x correct-sequences)))))
  (while (<= (count final-sequences) 6)
    (let [candidate (rand-nth correct-sequences)]
      (if (not-any? (fn [x] (= (first x) (first candidate)))
                    final-sequences)
        (def final-sequences (cons  candidate final-sequences)))))
  (clojure.string/join
   "\n\n"
   (map
    (fn [x] (clojure.string/capitalize (clojure.string/join " " (mark-sequence x))))
    final-sequences)))

(defn generate-a-poetry
  ""
  []
  (println (generate-random-poetry)))

(defn -main
  ""
  []
  (generate-a-poetry))
