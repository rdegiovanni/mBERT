#!/bin/bash -x

TOOLDIR=$(pwd)

# one directory before the BASEDIR: fore example, if the project is in /home/user/project, then the base dir is /home/user
ROOTDIR=$1"/"..

BASEDIR=$1 # root of the project
#SRC_DIR=$BASEDIR"/src/main/resources/data-structures/queues"
SRC_DIR=$BASEDIR"/"$2  # source path or the package that needs to be mutated
OUT_DIR=$ROOTDIR/mutants_codebert
mkdir -p "$OUT_DIR" # make dir if it does not exist
mutant_execution_file=$OUT_DIR"/"killing_matrix.csv
touch $mutant_execution_file

for source_file in $(find $SRC_DIR -type f -name "*java"); do
	  echo "<<< INFO: Mutation Targered Source File path:" $source_file
	  SRC_REAL_PATH=$(realpath --relative-to="$BASEDIR" $source_file) # resolve any potential symbolic links
	  DIR_REAL_PATH=$(dirname $SRC_REAL_PATH)
	  SOURCE_FILE_DIRECTORY=$(dirname $source_file)

	  BASE_NAME=$(basename $source_file)
	  MUTANT_NAME=${BASE_NAME%%.*}_mutants

    cp $source_file /tmp/

	  MUTANT_PATH=$OUT_DIR/$DIR_REAL_PATH/$MUTANT_NAME
	  echo "<<< INFO: Mutant Source File path: " $MUTANT_PATH
    java -Xmx8g -Djava.library.path=/usr/local/lib -Dfile.encoding=UTF-8 -classpath "target/classes/:$PATH" Main -in=$source_file -out=$MUTANT_PATH

    for mutant_source_file_path in $(ls -d $MUTANT_PATH/*/); do

      MUTANT_BASE_NAME=$(basename $mutant_source_file_path)
      mutant_source_file=$mutant_source_file_path$BASE_NAME
      echo ""
      echo "<<< INFO: Mutant ID:" $MUTANT_BASE_NAME
      echo "<<< INFO: Mutant source file path:" $mutant_source_file
      echo ""

      rm $source_file
      cp $mutant_source_file $SOURCE_FILE_DIRECTORY/

      python3 extract_failing_tests.py --project_path=$BASEDIR --mutant_name=$MUTANT_BASE_NAME --killing_matrix_file=$mutant_execution_file --source_file_name=$BASE_NAME --mutant_path=$mutant_source_file

    done

    rm $source_file
    cp /tmp/$BASE_NAME $SOURCE_FILE_DIRECTORY/
    rm /tmp/$BASE_NAME

done
