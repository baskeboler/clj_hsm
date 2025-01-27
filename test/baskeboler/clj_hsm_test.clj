;; ---------------------------------------------------------
;; baskeboler.clj-hsm.-test
;;
;; Example unit tests for baskeboler.clj-hsm
;;
;; - `deftest` - test a specific function
;; - `testing` logically group assertions within a function test
;; - `is` assertion:  expected value then function call
;; ---------------------------------------------------------


(ns baskeboler.clj-hsm-test
  (:require
   [clojure.test :refer [deftest is testing]]
   [baskeboler.clj-hsm :as clj-hsm]
   [baskeboler.hsm.core :as hsm]))


(deftest application-test
  (testing "TODO: Start with a failing test, make it pass, then refactor"

    ;; TODO: fix greet function to pass test
    (is (= "baskeboler application developed by the secret engineering team"
           (clj-hsm/greet)))

    ;; TODO: fix test by calling greet with {:team-name "Practicalli Engineering"}
    (is (= (clj-hsm/greet "Practicalli Engineering")
           "baskeboler service developed by the Practicalli Engineering team"))))
