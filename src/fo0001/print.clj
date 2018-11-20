(ns fo0001.print)

(defn find-and-pprint [form pred & [level]]
  (let [level (or level 0)
        self (fn [new-form] (find-and-pprint new-form pred (inc level)))
        printl
        (fn [value]

          (print (clojure.string/join (repeat level "  "))) ; 2 spaces ident
          (print value))

        print-map
        (fn [form]
          (let [l (count form)]
            (printl "{")
            (doseq [[index [k v]] (map-indexed list form)]
              (find-and-pprint k pred (if (zero? index) 0 (inc level)))
              (newline)
              (find-and-pprint v pred (inc level))
              (when (not= index (dec l))
                (newline)
                (newline)))
            (print "}")))

        print-list-or-vec
        (fn [form lbrace rbrace]
          (let [l (count form)]
            (printl lbrace)
            (doseq [[index value] (map-indexed list form)]
              (find-and-pprint value pred (if (zero? index) 0 (inc level)))
              (when (not= index (dec l))
                (newline)))
            (print rbrace)))]
    (when (pred form) (print "\u001b[31;1m"))

    (cond
      (list? form)
      (print-list-or-vec form "(" ")")

      (vector? form)
      (print-list-or-vec form "[" "]")

      (set? form)
      (print-list-or-vec form "#{" "}")

      (map? form)
      (print-map form)

      (some true? ((juxt
                    symbol? keyword?
                    string? number? true? false? nil?) form))
      (do
        (print (clojure.string/join (repeat level "  ")))
        (pr form))

      :else (throw (Exception. (str "Unknown form: " (pr-str form)))))
    
    
    (when (pred form) (print "\u001b[m"))))
