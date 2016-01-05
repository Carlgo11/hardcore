#!/bin/bash
# This script requires maven. Install it by doing "apt-get install maven"
git clone https://github.com/carlgo11/hardcore.git .hardcore
cd .hardcore
mvn install
cp target/*.jar ../plugins/
cd ..
rm -rf ./.hardcore
echo "Done!"
