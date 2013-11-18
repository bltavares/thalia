(ns thalia.doc
  (:require [clojure.pprint :as pp]
            [clojure.java.io :as io]
            [thalia.utils :refer [iprintf read-safely]]))


(defn- append-doc! [v additional-docstring]
  (let [m (meta v)
        ;; Save the original doc string if we have not done so earlier
        m (if (:thalia/original-doc meta)
            m
            (assoc m :thalia/original-doc (:doc m)))
        ^String orig-doc (:thalia/original-doc m)
        new-doc (str orig-doc
                     (if (.endsWith orig-doc "\n")
                       ""
                       "\n")
"--------- ^^^ original docs --------- VVV unofficial extra docs ---------\n"
                     additional-docstring)
        m (assoc m :doc new-doc)]
    (reset-meta! v m)))


;; TBD: Find somewhere appropriate to describe the behavior of
;; keywords looking themselves up in collections via (:keyword coll)
;; syntax.

;; TBD: Are the sequences returned by subseq and rsubseq lazy?


(defn- ns-if-loaded [ns-name-str]
  (try
    (the-ns (symbol ns-name-str))
    (catch Exception e
        nil)))


(defn add-extra-docs! [& args]
  (let [default-locale (str (java.util.Locale/getDefault))
        opts (merge {:language default-locale}
                    (apply hash-map args))
        rsrc-name (str (:language opts) ".clj")
        file (io/resource rsrc-name)]
    (if-not file
      (iprintf *err* "No resource file %s exists.  Either '%s' is not a language,
or no one has written thalia docs in that language yet.\n"
               rsrc-name (:language opts))
      (let [doc-data (read-safely file)]
        ;; TBD: Is there a 'standard' way to check for libraries other
        ;; than clojure.core what version is loaded?
        (doseq [[{:keys [library version]} data-for-symbols] doc-data]
          (doseq [one-sym-data data-for-symbols]
            (let [ns (:ns one-sym-data)
                  sym (:symbol one-sym-data)
                  extended-docstring (:extended-docstring one-sym-data)]
              (if (and ns sym extended-docstring)
                (if-let [var (try (resolve (symbol ns sym))
                                  (catch Exception e nil))]
                  (append-doc! var extended-docstring)
                  (iprintf *err* "No such var %s/%s\n" ns sym))
                ;; else
                (do
                  (binding [*out* *err*]
                    (iprintf "Resource file has this record that is missing one of the keys [:ns :symbol :extended-docstring]:\n")
                    (pp/pprint one-sym-data))
                  )))))))))


(comment
(in-ns 'user) (use 'clojure.repl) (require 'thalia.doc) (thalia.doc/add-extra-docs!)
)
