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
   [baskeboler.hsm.core :as hsm])
  (:refer [baskeboler.hsm.protocol]))

(deftest hsm-test 
  (testing "create-hms"
    (let [device (hsm/create-hms)]
      (is (some? device)) )))


