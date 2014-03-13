# create the svg-s through the class MyTest first
convert '*.svg' -set filename:prevname '%t' 'tn/%[filename:prevname].png'
