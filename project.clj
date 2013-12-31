(defproject epic-dungeon-quest "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "GNU General Public License v3"
            :url "http://www.gnu.org/licenses/gpl-3.0.txt"}
  :main epic-dungeon-quest.epic-dungeon-quest
  :plugins [[lein-git-deps "0.0.1-SNAPSHOT"]]
  :git-dependencies [["https://github.com/telaviv/clojure-lanterna.git"
                      "upgrade_lanterna_to_2.1.7"]]
  :source-paths ["src" ".lein-git-deps/clojure-lanterna/src/"]
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/math.numeric-tower "0.0.2"]
                 [com.googlecode.lanterna/lanterna "2.1.7"]])
