(ns cmap.test-runner
  (:require
   [doo.runner :refer-macros [doo-tests]]
   [cmap.core-test]
   [cmap.common-test]))

(enable-console-print!)

(doo-tests 'cmap.core-test
           'cmap.common-test)
