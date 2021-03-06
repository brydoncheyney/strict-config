(ns strict-config.map)

(deftype StrictMap [inner pred]
  clojure.lang.IPersistentMap
  (assoc [this k v]
    (StrictMap. (.assoc inner k v) pred))
  (assocEx [this k v]
    (StrictMap. (.assocEx inner k v) pred))
  (without [this k]
    (StrictMap. (.without inner k) pred))

  java.lang.Iterable
  (iterator [this]
    (.iterator inner))

  clojure.lang.Associative
  (containsKey  [this k]
    (.containsKey inner k))
  (entryAt [this k]
    (.entryAt inner k))

  clojure.lang.IPersistentCollection
  (count [this]
    (.count inner))
  (cons [this x]
    (StrictMap. (.cons inner x) pred))
  (empty [this]
    (.empty inner))
  (equiv [this other]
    (and (map? other) (= (seq other) (seq inner))))

  clojure.lang.Seqable
  (seq [this]
    (.seq inner))

  clojure.lang.IFn
  (invoke [this k]
    (.invoke inner k))
  (invoke [this k default]
    (.invoke inner k default))
  (applyTo [this args]
    (.applyTo inner args))

  clojure.lang.ILookup
  (valAt [this k]
    (let [v (get inner k)]
      (cond (not (contains? inner k))
              (throw (new RuntimeException (str "Key '" k "' not found in strict map '" inner "'.")))
            (map? v)
              (StrictMap. v pred)
            (pred v)
              (throw (new RuntimeException (str "Key '" k "' has invalid value '" v "' in strict map '" inner "'.")))
            :otherwise
              v)))
  (valAt [this k default]
    (let [v (get inner k)]
      (cond (not (contains? inner k))
              default
            (map? v)
              (StrictMap. v pred)
            (pred v)
              (throw (new RuntimeException (str "Key '" k "' has invalid value '" v "' in strict map '" inner "'.")))
            :otherwise
              v))))

(defn ^:private branch?
  "Return true if the node coll contains a map as its key"
  [coll]
  (-> coll last map?))

(defn ^:private children
  "Returns a sequence of node paths for children"
  [coll]
  (let [path (butlast coll) m (last coll) ]
    (map #(concat path %) m)))

(defn ^:private node-paths
  "Returns a sequence of node paths for a given map"
  [m]
  (->> m
       (conj [])
       (tree-seq branch? children)
       (remove (comp map? last))))

(defn ^:private invalid-values
  "Returns a lazy sequence of the items in a coll of node paths for which (pred
  item) returns true"
  [coll pred]
  (filter (comp pred last) coll))

(def ^:private error-msg (partial str "Invalid values: "))

(defn restriction
  "Throws an Exception if the map contains values for which (pred item) returns true"
  [m pred]
  (let [nodes (-> m node-paths (invalid-values pred))]
    (if (-> nodes empty? not)
      (throw (new RuntimeException(error-msg (apply str nodes))))
      m)))

(defn strict
  "Convert an ordinary map into one that throws an Exception when
   - an unknown key is requested
   - a value returns true for (pred value)
   - map contains values for which (pred item) returns true.
   If not passed, pred is nil?"
  ([m pred] (-> m (restriction pred) (StrictMap. pred)))
  ([m] (-> (strict m nil?))))
