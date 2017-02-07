#!/bin/bash

build_path="resources/public/js/compiled/cmap.js"

rm -rf docs/*
lein trampoline cljsbuild once min
cp -r resources/public/{js,index.html,css} docs/
md5=($(md5sum "$build_path"))
build_hash=${md5:0:9}

sed -i "s/cmap.js/cmap-$build_hash.js/g" docs/index.html
sed -i "s/style.css/style-$build_hash.css/g" docs/index.html
mv docs/js/compiled/cmap-prod.js docs/js/compiled/cmap-$build_hash.js
mv docs/css/style.css docs/css/style-$build_hash.css
rm docs/js/compiled/cmap.js
