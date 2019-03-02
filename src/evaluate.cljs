(ns evaluate)

(defn average [xs]
  (/ (reduce + 0 xs) (count xs)))

(defn in-range [x1 y1 x2 y2]
  (and (<= (.abs js/Math (- x1 x2)) 10)
       (<= (.abs js/Math (- y1 y2)) 10)))

(defn birds->neibs [birds]
  (let [ebirds (map vector (iterate inc 0) birds)

        near
        (fn [k1 {x1 :x y1 :y}]
          (fn [s [k2 {x2 :x y2 :y :as v}]]
            (if (or (= k1 k2) (not (in-range x1 y1 x2 y2)))
              s
              (assoc s k2 v))))

        inject
        (fn [s [k v]]
          (assoc s k (assoc v :neibs (reduce (near k v) {} ebirds))))]
    (reduce inject {} ebirds)))

(defn apply-rules [{:keys [x y a neibs]}]
  (let [angles (map :a (vals neibs))]
    {:x x
     :y y
     :a (average angles)}))

(defn neibs->birds [neibs]
  (map apply-rules (vals neibs)))

(defn evaluate-world [birds]
  (-> birds
      birds->neibs
      neibs->birds))
