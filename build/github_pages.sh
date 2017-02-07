#!/bin/bash

js_build_path="docs/js/compiled/cmap-prod.js"
css_build_path="docs/css/style.css"

rm -rf docs/*
lein trampoline cljsbuild once min
cp -r resources/public/{js,index.html,css} docs/
js_md5=($(md5sum "$js_build_path"))
css_md5=($(md5sum "$css_build_path"))
js_build_hash=${js_md5:0:9}
css_build_hash=${css_md5:0:9}

sed -i "s/cmap.js/cmap-$js_build_hash.js/g" docs/index.html
sed -i "s/style.css/style-$css_build_hash.css/g" docs/index.html
mv docs/js/compiled/cmap-prod.js docs/js/compiled/cmap-$js_build_hash.js
mv docs/css/style.css docs/css/style-$css_build_hash.css
rm -rf docs/js/compiled/cmap.js
