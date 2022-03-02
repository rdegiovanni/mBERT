#!/bin/bash

PROJECT_CP=./target/classes/

MUTANTS_DIR=$1
PROJECT=$2

log_file=$MUTANTS_DIR/$PROJECT-compilation.log
> $log_file
csv_file=$MUTANTS_DIR/$PROJECT-compilation.csv
> $csv_file
echo 'id,compile' >> $csv_file

aux_file=$MUTANTS_DIR/aux.log
processed_mutants=0
compilable_mutants=0
echo '> Processing mutants' 
for dir in $MUTANTS_DIR/*/ 
do
  echo '> Processing mutant: '$dir >> $log_file
  dir2=${dir%*/}
  number=${dir2##*/}
  # base_name=${mutant_file/%$".java"}
  echo '> Compiling mutant:' $number
  > $aux_file #empty aux file before compiling
  javac -cp $PROJECT_CP -g $dir/*.java -d $dir 2> $aux_file
  if grep -q error "$aux_file"; then
    echo '> Mutant Incorrect Syntax' >> $log_file
    echo $number',0' >> $csv_file
  else
    echo '> Mutant Compiled' >> $log_file
    echo $number',1' >> $csv_file
    compilable_mutants=$((compilable_mutants+1))
  fi

  cat $aux_file >> $log_file
  
  processed_mutants=$((processed_mutants+1))
  # echo '' 

done
echo 'Mutants: '$processed_mutants 
echo 'Compilable: '$compilable_mutants 
echo '' >> $log_file
echo '' >> $log_file
echo 'Mutants: '$processed_mutants >> $log_file
echo 'Compilable: '$compilable_mutants >> $log_file
echo '> Done!'

