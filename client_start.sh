#!/bin/bash
for i in {1..6}
do
	java Client_Main cui-$i &
done
