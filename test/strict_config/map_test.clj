(ns strict-config.map-test
  (:require [midje.sweet :refer :all]
            [strict-config.map :refer [strict restriction]]))

(fact "Attempting to get a missing key from a strict map throws an exception."
      (-> {:key :value} strict :key) => :value
      (-> {:key :value} strict map?) => true
      (-> {:key :value} strict (:foo :default)) => :default
      (-> {:key :value} strict (apply [:key])) => :value
      (-> {:key :value} strict (get :key)) => :value
      (-> {:key :value} strict (assoc :foo :bar) :foo) => :bar
      (-> {:key :value} strict vals) => [:value]
      (-> {:key :value} strict keys) => [:key]
      (-> {:key :value} strict seq) => [[:key :value]]
      (-> {:key :value} strict (= {:key :value})) => true
      (-> {:key :value} strict (conj {:foo :bar}) :foo) => :bar
      (-> {:key :value} strict (merge {:foo :bar}) :foo) => :bar
      (-> {:key :value} strict (conj {:foo :bar}) :baz) => (throws RuntimeException)
      (-> {:key :value} strict (merge {:foo :bar}) :baz) => (throws RuntimeException)
      (-> {:key :value} strict :foo) => (throws RuntimeException)
      (-> {:key :value :nested {:foo :bar}} strict :nested :baz) => (throws RuntimeException)
      (-> {:key :value :nested {:foo :bar}} strict (:nested :default) :baz) => (throws RuntimeException))

(facts "Attempting to apply a strict map to a map containing invalid values throws an exception"
       (facts "default predicate - nil?"
              (-> {:key :value} strict) => {:key :value}
              (-> {:key :value :nested {:foo :bar}} strict) => {:key :value :nested {:foo :bar}}
              (-> {:key nil} strict) => (throws RuntimeException)
              (-> {:key :value :nested {:foo nil}} strict) => (throws RuntimeException))
       (facts "predicate - odd?"
              (def strict-odd #(strict % odd?))
              (-> {:key 2} strict-odd) => {:key 2}
              (-> {:key 2 :nested {:foo 4}} strict-odd) => {:key 2 :nested {:foo 4}}
              (-> {:key 1} strict-odd) => (throws RuntimeException)
              (-> {:key 2 :nested {:foo 3}} strict-odd) => (throws RuntimeException)))

(fact "Attempting to get an invalid value from a strict map throws an exception"
      (-> {:key :value :nested {:foo :bar}} strict (assoc :key nil) :key) => (throws RuntimeException))
