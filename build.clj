(ns build
  (:require [clojure.tools.build.api :as tb]))


(def lib 'org.trivial.something/mylib)
(def version "0.1.0")


(def src-dirs ["src" "resources"])
(def classes "target/classes")
(def basis (tb/create-basis {:project "deps.edn"}))


(def jar-file     (format "target/%s-%s.jar" (name lib) version))
(def uberjar-file (format "target/%s-%s-standalone.jar" (name lib) version))


(defn clean [_]
  (tb/delete {:path "target"}))


(defn jar [_]
  (tb/write-pom {:class-dir classes
                 :lib lib
                 :version version
                 :basis basis
                 :src-dirs ["src"]})
  (tb/copy-dir {:src-dirs src-dirs
                :target-dir classes})
  (tb/jar {:class-dir classes
           :jar-file jar-file}))


(defn uber [_]
  (clean nil)
  (tb/copy-dir {:src-dirs src-dirs
                :target-dir classes})
  (tb/compile-clj {:basis basis
                   :src-dirs ["src"]
                   :class-dir classes})
  (tb/uber {:class-dir classes
            :uber-file uberjar-file
            :basis basis
            :main 'org.trivial.something.mylib.core}))
