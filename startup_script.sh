#!/bin/bash
world="world" # The default world
userdata="/plugins/Essentials/userdata" # Essentials' userdata directory
spigot="spigot.jar" # Spigot jar
delay=5 # Delay between the MC server stopping and the script restarting it again.

while :
do
if [ ! -f "$spigot" ]; then
echo "No $spigot found."
./scripts/updateSpigot.sh "$spigot"
fi
if [ -d "$world" ]; then
echo "Removing $world"
rm -rf "$world"
fi

if [ -d "$userdata" ]; then
echo "Removing $userdata"
rm -rf "$userdata"
fi

echo "Starting server ($spigot)"
java -jar "$spigot"
echo "Restarting in $delay seconds. Press CTRL+C to abort."
sleep $delay
done