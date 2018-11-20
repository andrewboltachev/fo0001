(ns fo0001.print)

(defn pprint [form pred & [level]]
  (let [level (or level 0)
        self (fn [new-form] (pprint new-form pred (inc level)))
        printl
        (fn [value]

          (print (repeat level "  ")) ; 2 spaces ident
          (print value))]

    (cond
      (list? form)
      (do
        (printl "(")
        (doall (map self form))
        (printl ")"))

      (map? form)
      (do
        (printl "{")
        (doseq [[k v] form]
          (self k)
          (newline)
          (self v)
          (newline)
          (newline))
        (printl "}")
        (newline))

      ;(instance? clojure.lang.IRecord form)
      ;(outer (reduce (fn [r x] (conj r (inner x))) form form))

      ;(coll? form) (outer (into (empty form) (map inner form)))

      (some true? ((juxt string? number? true? false? nil?) form))
      (pr-str form)

      :else (outer form))))
