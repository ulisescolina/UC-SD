#! /bin/bash
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
hosts="$DIR/hosts.txt"
array=null

#termite -e "java -jar $DIR/Clasificador.jar" & 

while read -r linea 
do
	IFS=':'
	array=( $linea )
	termite -e "java -jar $DIR/Proceso.jar $array[1]"
done < "$hosts"


