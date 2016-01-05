#!/bin/bash
filename="$1"
if [ -z "$1" ]
 then
 echo "No file name supplied"
 exit
fi
echo "Getting the latest spigot jar..."
curl http://getspigot.org/spigot18/spigot_server.jar -o $filename
echo "New server jar downloaded!"